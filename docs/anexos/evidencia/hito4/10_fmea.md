# FMEA — Módulo "Registrar Asistencia"

**Proyecto:** Power Strike  
**Materia:** Ingeniería de Software II — IUA  
**Hito:** 4  
**Técnica:** Failure Mode and Effects Analysis (FMEA)  
**Responsable:** Fabricio Posada  
**Fecha:** 01/06/2026

---

## 1. Funcionalidad analizada

**Endpoint:** `POST /api/attendances`

Registrar la asistencia de un usuario al gimnasio. La operación recibe un `userId` y opcionalmente notas, y persiste el registro con la fecha/hora del servidor.

Se eligió esta funcionalidad porque:
- Es transversal: cruza autenticación, validación, lógica de servicio y persistencia.
- Tiene impacto directo sobre el negocio: una falla puede alterar el conteo de presencias.
- Manipula datos sensibles del usuario (identidad y fecha).

---

## 2. Marco conceptual

**FMEA** (Failure Mode and Effects Analysis) es una técnica sistemática que enumera, para cada componente, los posibles modos de falla, sus efectos sobre el sistema y sus causas raíces.

Cada modo se cuantifica con tres escalas del 1 al 10:

| Variable | Descripción |
|---|---|
| **S** (Severidad) | Qué tan grave es el efecto si la falla ocurre |
| **P** (Probabilidad) | Qué tan probable es que la falla suceda |
| **D** (Detectabilidad) | Qué tan difícil es detectarla antes de llegar al usuario (10 = imposible) |

**RPN = S × P × D** — se priorizan los modos con mayor RPN.

---

## 3. Tabla de modos de falla

| # | Modo de fallo | Efecto | S | P | D | RPN |
|---|---|---|---|---|---|---|
| 1 | Falla de base de datos durante el guardado | La asistencia no se registra y se pierde información sobre la concurrencia | 8 | 5 | 8 | **320** |
| 2 | Doble registro accidental del mismo usuario | El usuario aparece más de una vez, alterando estadísticas e informes | 5 | 6 | 7 | **210** |
| 3 | Error de red durante el registro | El usuario cree que la asistencia fue registrada, pero el dato no llega al servidor | 7 | 5 | 6 | **210** |
| 4 | Registro de asistencia para un usuario inexistente | Se generan datos inconsistentes o errores en los reportes del sistema | 7 | 3 | 7 | **147** |
| 5 | Registro de asistencia con fecha futura | Se almacenan asistencias inválidas que afectan estadísticas y controles | 4 | 4 | 5 | **80** |

---

## 4. Orden de prioridad (Mayor RPN → Menor)

| Prioridad | Modo de fallo | RPN |
|---|---|---|
| 1 | Falla de base de datos durante el guardado | 320 |
| 2 | Doble registro accidental de asistencia | 210 |
| 3 | Error de red durante el registro | 210 |
| 4 | Registro para usuario inexistente | 147 |
| 5 | Registro con fecha futura | 80 |

---

## 5. Mitigaciones priorizadas

### Mitigación 1 — Falla de base de datos (RPN 320)

- **Estrategia:** Detección + Recuperación
- **Implementación:** Registrar errores mediante logs estructurados y devolver un mensaje controlado al usuario. Implementar reintentos automáticos de conexión y monitoreo de disponibilidad de la base de datos para detectar fallas tempranamente.
- **Impacto esperado:** D baja de 8 a 4 (el sistema detecta la falla antes de que el usuario lo note). Nuevo RPN estimado: 8 × 5 × 4 = **160**.

### Mitigación 2 — Doble registro accidental (RPN 210)

- **Estrategia:** Programación defensiva
- **Implementación:** Validar en el backend que no exista una asistencia previa del mismo usuario para la misma fecha calendario. Si existe, rechazar el registro con HTTP 409 y un mensaje informativo.
- **Impacto esperado:** P baja de 6 a 2 (la validación lo previene). Nuevo RPN estimado: 5 × 2 × 7 = **70**.
- **Nota:** Este modo está documentado como defecto EXP-02 en el reporte de defectos exploratorios.

### Mitigación 3 — Error de red durante el registro (RPN 210)

- **Estrategia:** Defensa en profundidad
- **Implementación:** Confirmar desde el servidor que la asistencia fue almacenada correctamente antes de mostrar éxito al usuario. Si ocurre una falla de comunicación, informar el error y permitir reintentar sin duplicar registros (idempotencia).
- **Impacto esperado:** D baja de 6 a 3. Nuevo RPN estimado: 7 × 5 × 3 = **105**.

---

## 6. Relación con casos de prueba

| Modo de fallo | Caso de prueba relacionado |
|---|---|
| Doble registro | AT-04 (userId válido) — actualmente pasa sin restricción (bug) |
| Usuario inexistente | AT-01, AT-02, AT-03 (userId inválido → HTTP 400) |
| Fecha futura | No aplica — la fecha es asignada server-side con `LocalDateTime.now()` |
