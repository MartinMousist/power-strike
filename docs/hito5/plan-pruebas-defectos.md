# Plan de pruebas y reporte de defectos — Power Strike (Hito 5)

Actualización final del plan de pruebas y el reporte de defectos del Hito 4. El detalle completo
(casos, técnicas, FMEA, máquina de estados) vive en `docs/Hito_4_PowerStrike.docx`.

## 1. Estrategia de pruebas

| Nivel / tipo | Qué prueba | Herramientas |
|---|---|---|
| Unitario de servicio (caja blanca) | Lógica de `UserService`, `ActivityService`, `AttendanceService` con dobles de prueba | JUnit 5 + Mockito |
| Capa web / slice (caja negra) | Validaciones y manejo de errores de los controllers vía HTTP | `@WebMvcTest` + MockMvc |
| Exploratorio | Sesiones manuales sobre el sistema desplegado | Navegador + Postman |

Técnicas de diseño aplicadas: partición de equivalencia, valores límite, tabla de decisión,
máquina de estados (sobre la entidad Asistencia).

## 2. Resultados de ejecución (cierre)

| Métrica | Valor |
|---|---|
| Tests automatizados | 78 (56 caja negra + 22 caja blanca) |
| Resultado | 78/78 (0 fallos, 0 errores) |
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
| DEF-EXP-01 | `userId` inexistente devuelve 500 en vez de 404 | Menor | Media | Corregido (Hito 5, TDD) |
| DEF-EXP-02 | Se permiten asistencias duplicadas sin restricción | Mayor | Alta | Corregido (Hito 5, TDD) |
| DEF-EXP-03 | No se puede editar usuario sin cambiar la contraseña | Mayor | Alta | Corregido (Hito 5, TDD) |
| DEF-EXP-04 | Sin control de roles: CLIENT accede a todas las rutas | Crítica | Alta | Corregido (Hito 5, TDD) |
| DEF-EXP-05 | Mensaje incorrecto al eliminar usuario con asistencias | Menor | Baja | Abierto |
| DEF-EXP-06 | Día de actividad sin validación de formato en backend | Menor | Baja | Corregido (Hito 5, TDD) |

### 3.3 Resumen

| | Cantidad |
|---|:---:|
| Defectos totales detectados | 12 |
| Corregidos / cerrados | 9 (incl. 5 cerrados en Hito 5 con TDD: DEF-EXP-01/02/03/04/06, incluido el crítico) |
| Abiertos (para próximos avances) | 3 |
| Densidad | ~17 defectos/KLOC (backend) — refleja testing exhaustivo |

Todos los defectos están además cargados como Issues en el repositorio
(https://github.com/MartinMousist/power-strike/issues) con su severidad, prioridad y trazabilidad.

## 4. Corregidos en el Hito 5 (con TDD, ciclo Red→Green visible en el historial)
- DEF-EXP-04 (crítico): RBAC con `@PreAuthorize("hasRole('ADMIN')")` — solo ADMIN gestiona usuarios; un CLIENT recibe 403. (`MethodSecurityConfig` + `UserControllerSecurityTest`).
- DEF-EXP-01: `NotFoundException` → 404 para usuario inexistente.
- DEF-EXP-02: rechazo de asistencia duplicada el mismo día → 409.
- DEF-EXP-03 (mayor): la contraseña es obligatoria solo al crear (grupo de validación `OnCreate`); editar un usuario sin enviar contraseña ahora devuelve 200 y conserva la actual.
- DEF-EXP-06: validación `@Pattern` del día de la actividad → 400 si es inválido.

## 5. Pendientes priorizados para el cierre
1. DEF-EXP-05 (menor): mensaje correcto al borrar usuario con asistencias (FK).
2. DEF-06 (medio): validaciones en `LoginRequest`.
3. DEF-04 (medio): tope superior en la longitud de la contraseña.
