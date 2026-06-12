# Matriz de Trazabilidad de Requerimientos (RTM) — Power Strike (Hito 5)

Vincula cada requerimiento con su diseño, su implementación (backend y frontend) y los tests que lo verifican. Es la base de la trazabilidad exigida por el Plan SQA.

## Requerimientos funcionales

| ID | Requerimiento | Endpoint / Diseño | Backend (código) | Frontend | Tests que lo verifican | Estado |
|----|---------------|-------------------|------------------|----------|------------------------|:------:|
| REQ-F01 | Registrar usuarios con nombre, email y DNI (con validaciones) | POST /api/users | User (validaciones @NotBlank/@Size/@Email/@Pattern), UserController.createUser, UserService.createUser, UserRepository | UsersView.vue | UserControllerTest (CP-01..CP-20, EX-01/02), UserServiceTest | OK |
| REQ-F02 | Mostrar listado de usuarios registrados | GET /api/users | UserController.getAllUsers, UserService.getAllUsers | UsersView.vue | UserControllerTest, UserServiceTest.getAllUsers | OK |
| REQ-F03 | Gestionar actividades (crear, editar, visualizar) con nombre, día, horario y costo | POST/PUT/GET /api/activities | Activity (validaciones), ActivityController (create/update/getById), ActivityService | ActivitiesView.vue | ActivityControllerTest (AC-01..AC-11), ActivityServiceTest (create/update/getById) | OK |
| REQ-F04 | Mostrar actividades disponibles para consulta | GET /api/activities | ActivityController.getAllActivities, ActivityService.getAllActivities | ActivitiesView.vue | ActivityControllerTest.getAllActivities, ActivityServiceTest.getAllActivities | OK |
| REQ-F05 | Registrar y mostrar historial de asistencia de usuarios | POST /api/attendances | Attendance, AttendanceRequest (validaciones userId), AttendanceController.registerAttendance, AttendanceService | AttendanceView.vue | AttendanceControllerTest (AT-01..AT-06), AttendanceServiceTest | OK |

## Requerimientos transversales (no funcionales)

| ID | Requerimiento | Diseño | Implementación | Test | Estado |
|----|---------------|--------|----------------|------|:------:|
| REQ-NF01 | Autenticación con JWT | POST /api/auth/login | AuthController, JwtUtil, JwtAuthenticationFilter, SecurityConfig | AuthControllerTest (login happy-path) | OK |
| REQ-NF02 | Autorización por rol (ADMIN/TRAINER/CLIENT) | RBAC con @PreAuthorize | User.role (@Pattern), MethodSecurityConfig, UserController (@PreAuthorize) | UserControllerSecurityTest (CLIENT 403 / ADMIN 200) | OK (DEF-EXP-04 corregido) |
| REQ-NF03 | Manejo coherente de errores HTTP | GlobalExceptionHandler | traduce validación a 400, conflicto a 409, no encontrado a 404, acceso denegado a 403 | UserControllerTest (EX-01/02/03), UserControllerSecurityTest | OK |

## Cobertura de la trazabilidad

- 5 de 5 requerimientos funcionales implementados y con tests automatizados.
- Cada test referencia su caso de diseño (CP-, AC-, AT-, EX-); ver el plan de pruebas.
- Trazabilidad inversa: cada defecto del reporte (DEF-XX) está vinculado al requerimiento y al test que lo expuso (ver plan-pruebas-defectos.md).

## Mapa de pantallas (frontend) y requerimientos

| Pantalla (frontend/src/views) | Requerimientos |
|---|---|
| LoginView.vue | REQ-NF01 |
| DashboardView.vue | resumen / navegación |
| UsersView.vue | REQ-F01, REQ-F02 |
| ActivitiesView.vue | REQ-F03, REQ-F04 |
| AttendanceView.vue | REQ-F05 |
