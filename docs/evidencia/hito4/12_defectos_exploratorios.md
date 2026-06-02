# Defectos — Testing Exploratorio

**Proyecto:** Power Strike  
**Materia:** Ingeniería de Software II — IUA  
**Hito:** 4  
**Tipo de prueba:** Exploratoria (caja negra sobre sistema desplegado)  
**Responsable:** Ernesto Ponce / Martín Mousist  
**Fecha:** 01/06/2026

---

## 1. Metodología

El testing exploratorio se realizó con el sistema levantado via Docker Compose (PostgreSQL + backend Spring Boot en puerto 8090 + frontend Vite en puerto 5173). Se probaron todos los flujos descritos en el guion de pruebas exploratorias del equipo, haciendo peticiones directas a la API REST para documentar comportamientos no cubiertos por los tests automatizados.

Herramientas utilizadas: navegador, peticiones HTTP directas via PowerShell/curl.

Credenciales usadas: `admin@powerstrike.com` / `admin123` (rol ADMIN), y un usuario CLIENT de prueba.

---

## 2. Resumen cuantitativo

| Severidad | Cantidad | Estado |
|---|---|---|
| Crítica | 1 | Abierto |
| Mayor | 2 | Abierto |
| Menor | 3 | Abierto |
| **Total** | **6** | — |

---

## 3. Tabla consolidada

| ID | Descripción corta | Severidad | Estado |
|---|---|---|---|
| DEF-EXP-01 | userId inexistente devuelve HTTP 500 en lugar de 404 | Menor | Abierto |
| DEF-EXP-02 | Se permiten asistencias duplicadas sin restricción | Mayor | Abierto |
| DEF-EXP-03 | No se puede editar usuario sin cambiar la contraseña | Mayor | Abierto |
| DEF-EXP-04 | Sin control de roles — CLIENT accede a todas las rutas | Crítica | Abierto |
| DEF-EXP-05 | Mensaje de error incorrecto al eliminar usuario con asistencias | Menor | Abierto |
| DEF-EXP-06 | Campos día y horario de actividad sin validación de formato en backend | Menor | Abierto |

---

## 4. Detalle de cada defecto

---

### DEF-EXP-01 — userId inexistente devuelve HTTP 500 en lugar de 404

| Campo | Valor |
|---|---|
| **Descripción** | Al registrar una asistencia con un `userId` positivo pero inexistente en la base de datos, el sistema devuelve HTTP 500 en lugar de HTTP 404. |
| **Pasos para reproducir** | `POST /api/attendances` con body `{"userId": 99999}` usando un token válido. |
| **Resultado real** | HTTP 500 `{"message":"Usuario no encontrado"}` |
| **Resultado esperado** | HTTP 404 Not Found |
| **Causa raíz** | `AttendanceService.registerAttendance()` lanza `RuntimeException("Usuario no encontrado")`, que el `GlobalExceptionHandler` captura como error genérico y devuelve 500. |
| **Plan de corrección** | Crear una excepción específica `NotFoundException` y agregarla al handler con respuesta HTTP 404. |
| **Relacionado con** | `10_fmea.md` modo 4 (RPN 147); `11_maquina_estados.md` ME-05 |

---

### DEF-EXP-02 — Se permiten asistencias duplicadas sin restricción

| Campo | Valor |
|---|---|
| **Descripción** | El sistema acepta registrar múltiples asistencias para el mismo usuario en el mismo día sin ninguna validación. Ambos registros se persisten con HTTP 200. |
| **Pasos para reproducir** | 1. `POST /api/attendances` con `{"userId": 2}`. 2. Repetir inmediatamente con los mismos datos. 3. Ambas devuelven HTTP 200. |
| **Resultado real** | HTTP 200 en ambas peticiones; dos filas en la tabla `attendances` para el mismo usuario y fecha. |
| **Resultado esperado** | Segunda petición devuelve HTTP 409 Conflict indicando que ya existe asistencia para ese usuario en la fecha actual. |
| **Causa raíz** | `AttendanceRepository` no tiene constraint de unicidad por `(user_id, fecha)`. El servicio no valida la existencia previa. |
| **Plan de corrección** | Agregar validación en `AttendanceService`: consultar si existe una asistencia del mismo userId en la misma fecha antes de persistir. Si existe, lanzar una excepción con HTTP 409. |
| **Relacionado con** | `10_fmea.md` modo 2 (RPN 210 — mayor prioridad); `11_maquina_estados.md` ME-07 |

---

### DEF-EXP-03 — No se puede editar usuario sin cambiar la contraseña

| Campo | Valor |
|---|---|
| **Descripción** | Al editar un usuario desde el frontend y dejar el campo contraseña vacío (como indica el texto de ayuda "dejar vacío para no cambiar"), el backend devuelve error de validación. La funcionalidad de edición está rota para el caso de uso principal. |
| **Pasos para reproducir** | 1. Login como ADMIN. 2. Ir a Gestión de Usuarios → Editar cualquier usuario. 3. Dejar contraseña en blanco. 4. Guardar. |
| **Resultado real** | HTTP 400 `{"errors":{"password":"La contraseña no puede estar vacía"}}`. Frontend muestra: *"Error al guardar. Verificá los datos."* sin indicar qué campo falló. |
| **Resultado esperado** | El usuario se actualiza con los datos nuevos; la contraseña no cambia si se deja vacía. |
| **Causa raíz** | El modelo `User` tiene `@NotBlank` en el campo `password`, y el endpoint `PUT /api/users/{id}` aplica `@Valid`. El service sí tiene lógica para no re-encodear si el campo es blank, pero la validación Bean Validation falla antes de llegar al service. |
| **Plan de corrección** | Crear un DTO separado `UpdateUserRequest` sin `@NotBlank` en password, o usar grupos de validación (`@Validated`) para diferenciar creación de actualización. |

---

### DEF-EXP-04 — Sin control de roles: CLIENT accede a todas las rutas

| Campo | Valor |
|---|---|
| **Descripción** | Un usuario con rol CLIENT puede ejecutar todas las operaciones de la API (crear/editar/eliminar usuarios, actividades, registrar asistencias de otros) sin restricción. No existe control de acceso basado en roles (RBAC). |
| **Pasos para reproducir** | 1. Login con usuario CLIENT. 2. Con el token obtenido, hacer `GET /api/users`, `POST /api/users`, `DELETE /api/users/1`. 3. Todas devuelven HTTP 200/204. |
| **Resultado real** | Un cliente del gimnasio puede crear cuentas de administrador, eliminar usuarios, modificar actividades y registrar asistencias de cualquier persona. |
| **Resultado esperado** | HTTP 403 Forbidden para operaciones que requieren rol ADMIN o TRAINER. |
| **Causa raíz** | `SecurityConfig` usa `.anyRequest().authenticated()` sin restricciones por rol. No hay anotaciones `@PreAuthorize` o `.hasRole()` en ningún endpoint. |
| **Plan de corrección** | Agregar autorización por rol en `SecurityConfig` o mediante `@PreAuthorize("hasRole('ADMIN')")` en los métodos del controller. |
| **Nota** | Los tests automatizados no detectaron este defecto porque `@AutoConfigureMockMvc(addFilters = false)` deshabilita la seguridad en los tests de capa web. |

---

### DEF-EXP-05 — Mensaje incorrecto al eliminar usuario con asistencias

| Campo | Valor |
|---|---|
| **Descripción** | Al intentar eliminar un usuario que tiene asistencias asociadas, el sistema devuelve HTTP 409 con el mensaje *"Error al guardar. Verifique que el email y DNI sean únicos."*, que no corresponde al error real. Además, el frontend no muestra ningún mensaje al usuario (no hay try-catch en `confirmDelete()`). |
| **Pasos para reproducir** | 1. Registrar al menos una asistencia para un usuario. 2. Ir a Gestión de Usuarios → Eliminar ese usuario → Confirmar. |
| **Resultado real** | HTTP 409 con mensaje incorrecto sobre email/DNI. En el frontend: el modal se cierra sin mostrar error; el usuario permanece en la lista. |
| **Resultado esperado** | HTTP 409 con mensaje claro: "No se puede eliminar el usuario porque tiene asistencias registradas." Frontend muestra el mensaje. |
| **Causa raíz** | El `GlobalExceptionHandler` captura toda `DataIntegrityViolationException` y solo verifica si el mensaje contiene "email" o "dni". La violación de FK por asistencias no cae en ninguna rama y devuelve el mensaje genérico. |
| **Plan de corrección** | Agregar detección de FK violation en el handler. En el frontend, envolver `api.delete()` en try-catch y mostrar el error al usuario. |

---

### DEF-EXP-06 — Campos día y horario de actividad sin validación de formato en backend

| Campo | Valor |
|---|---|
| **Descripción** | Los campos `day` y `schedule` de una actividad aceptan cualquier string en el backend, incluyendo texto libre, emojis y formatos inválidos. El frontend restringe `day` a un select (Lunes-Domingo), pero accediendo directo a la API se pueden insertar datos inconsistentes. |
| **Pasos para reproducir** | `POST /api/activities` con body `{"name":"Test","day":"CUALQUIER DIA","schedule":"mañana temprano 🏋️","monthlyCost":1000}`. |
| **Resultado real** | HTTP 200. La actividad se persiste con `day="CUALQUIER DIA"` y `schedule="mañana temprano 🏋️"`. |
| **Resultado esperado** | HTTP 400. El campo `day` debería validarse contra los 7 días de la semana; `schedule` debería seguir un formato horario (ej: `HH:MM - HH:MM`). |
| **Causa raíz** | El modelo `Activity` tiene `@NotBlank` en `day` y `schedule` pero sin `@Pattern` que restrinja los valores permitidos. |
| **Plan de corrección** | Agregar `@Pattern(regexp = "^(Lunes|Martes|Miércoles|Jueves|Viernes|Sábado|Domingo)$")` en `day` y un pattern de formato horario en `schedule`. |

---

## 5. Relación con defectos del testing formal (Sección 6.1–6.3)

Los defectos DEF-EXP-01 a DEF-EXP-06 son **complementarios** a los DEF-01 a DEF-06 documentados por el testing dirigido por técnicas formales. Los formales se detectaron por diseño de casos y análisis de cobertura; los exploratorios se detectaron usando el sistema como un usuario real.

| Defecto exploratorio | Técnica que lo habría detectado | Por qué no se detectó antes |
|---|---|---|
| DEF-EXP-01 | Valores límite sobre userId | No se probó userId positivo inexistente en los tests formales |
| DEF-EXP-02 | Tabla de decisión / máquina de estados | No hay constraint de unicidad; no se diseñó caso de doble registro |
| DEF-EXP-03 | Prueba de edición sin contraseña | El caso de uso editar-sin-cambiar-pass no estaba en los casos formales |
| DEF-EXP-04 | Prueba de seguridad por rol | Los tests deshabilitan filtros de seguridad (`addFilters=false`) |
| DEF-EXP-05 | Prueba de eliminación con dependencias | No se diseñó caso de delete con FK activa |
| DEF-EXP-06 | Partición de equivalencia sobre day/schedule | No se probó la API directamente sin pasar por el frontend |

> Esto ilustra el **Principio 5 de ISTQB (Paradoja del pesticida)**: los tests formales encontraron los bugs que estaban diseñados para encontrar. El testing exploratorio encontró los que quedaron fuera del diseño.
