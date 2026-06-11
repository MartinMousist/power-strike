# Plan de pruebas y reporte de defectos — Power Strike (Hito 5)

Actualización final del plan de pruebas y el reporte de defectos del Hito 4. El detalle completo
(casos, técnicas, FMEA, máquina de estados) vive en `docs/Hito_4_PowerStrike.docx`.

## 1. Estrategia de pruebas

| Nivel / tipo | Qué prueba | Herramientas |
|---|---|---|
| Unitario de servicio (**caja blanca**) | Lógica de `UserService`, `ActivityService`, `AttendanceService` con dobles de prueba | JUnit 5 + Mockito |
| Capa web / slice (**caja negra**) | Validaciones y manejo de errores de los controllers vía HTTP | `@WebMvcTest` + MockMvc |
| Exploratorio | Sesiones manuales sobre el sistema desplegado | Navegador + Postman |

**Técnicas de diseño aplicadas:** partición de equivalencia, valores límite, tabla de decisión,
máquina de estados (sobre la entidad Asistencia).

## 2. Resultados de ejecución (cierre)

| Métrica | Valor |
|---|---|
| Tests automatizados | **72** (50 caja negra + 22 caja blanca) |
| Resultado | 72/72 ✅ (0 fallos, 0 errores) |
| Cobertura módulos principales | 100 % |
| Cobertura global | 53,0 % |
| Comando | `mvn verify` (con reporte JaCoCo) |
| CI | GitHub Actions — verde en cada push |

## 3. Reporte de defectos

### 3.1 Defectos documentados (TDD / análisis)

| ID | Descripción | Severidad (QA) | Prioridad (PO) | Estado |
|----|-------------|:---:|:---:|:---:|
| DEF-01 | Validaciones ausentes en `ActivityController` | Alta | Alta | Corregido |
| DEF-02 | Validaciones ausentes en `AttendanceRequest` | Alta | Alta | Corregido |
| DEF-03 | Rama no cubierta en `UserService.updateUser` | Media | Media | Corregido |
| DEF-04 | `User.password` sin tope superior | Media | Baja | Abierto |
| DEF-05 | Validación de `role` estrictamente case-sensitive | Baja | Baja | Cerrado (decisión de diseño) |
| DEF-06 | `LoginRequest` sin validaciones | Media | Media | Abierto |

### 3.2 Defectos por testing exploratorio

| ID | Descripción | Severidad | Prioridad | Estado |
|----|-------------|:---:|:---:|:---:|
| DEF-EXP-01 | `userId` inexistente devuelve 500 en vez de 404 | Menor | Media | Abierto |
| DEF-EXP-02 | Se permiten asistencias duplicadas sin restricción | Mayor | Alta | Abierto |
| DEF-EXP-03 | No se puede editar usuario sin cambiar la contraseña | Mayor | Alta | Abierto |
| DEF-EXP-04 | **Sin control de roles: CLIENT accede a todas las rutas** | Crítica | Alta | Abierto |
| DEF-EXP-05 | Mensaje incorrecto al eliminar usuario con asistencias | Menor | Baja | Abierto |
| DEF-EXP-06 | Día/horario de actividad sin validación de formato en backend | Menor | Baja | Abierto |

### 3.3 Resumen

| | Cantidad |
|---|:---:|
| Defectos totales detectados | **12** |
| Corregidos / cerrados | 5 |
| Abiertos (para próximos avances) | 7 |
| Densidad | ~17 defectos/KLOC (backend) — refleja testing exhaustivo |

> Todos los defectos están además cargados como **Issues** en el repositorio
> (https://github.com/MartinMousist/power-strike/issues) con su severidad, prioridad y trazabilidad.

## 4. Pendientes priorizados para el cierre
1. **DEF-EXP-04** (crítico): implementar RBAC real (`.hasRole()` / `@PreAuthorize`).
2. **DEF-EXP-02** (mayor): evitar asistencias duplicadas (validar en `AttendanceService`).
3. **DEF-EXP-03** (mayor): permitir editar usuario sin cambiar contraseña.
