# Resumen Ejecutivo — Hito 4

## Tabla de casos de prueba ejecutados

### UserController (23 CP + 3 EX + 4 happy-path = 27 tests)

| ID    | Técnica          | Entrada                  | Esperado | Resultado |
|-------|------------------|--------------------------|----------|-----------|
| CP-01 | Valor borde      | dni="1234567" (7 dígitos)| 200      | 200 ✓     |
| CP-02 | Valor borde      | dni="12345678" (8 dígitos)| 200     | 200 ✓     |
| CP-03 | Valor inválido   | dni="123456" (6 dígitos) | 400      | 400 ✓     |
| CP-04 | Valor inválido   | dni="123456789" (9)      | 400      | 400 ✓     |
| CP-05 | Valor inválido   | dni="1234ABC"            | 400      | 400 ✓     |
| CP-06 | Valor inválido   | dni=""                   | 400      | 400 ✓     |
| CP-07 | Valor borde inf  | password="12345" (5)     | 400      | 400 ✓     |
| CP-08 | Valor borde sup  | password="123456" (6)    | 200      | 200 ✓     |
| CP-09 | Valor típico     | password="miClave123"    | 200      | 200 ✓     |
| CP-10 | Valor inválido   | password=""              | 400      | 400 ✓     |
| CP-11 | Valor borde inf  | name="ab" (2 chars)      | 400      | 400 ✓     |
| CP-12 | Valor borde sup  | name="abc" (3 chars)     | 200      | 200 ✓     |
| CP-13 | Valor borde sup  | name=100 chars           | 200      | 200 ✓     |
| CP-14 | Valor inválido   | name=101 chars           | 400      | 400 ✓     |
| CP-15 | Valor inválido   | name=""                  | 400      | 400 ✓     |
| CP-16 | Equivalencia     | role="ADMIN"             | 200      | 200 ✓     |
| CP-17 | Equivalencia     | role="USER"              | 400      | 400 ✓     |
| CP-18 | Equivalencia     | role="admin"             | 400      | 400 ✓     |
| CP-19 | Valor típico     | email="test@mail.com"    | 200      | 200 ✓     |
| CP-20 | Valor inválido   | email="testmail.com"     | 400      | 400 ✓     |
| EX-01 | Excepción        | DataIntegrity email dup  | 409      | 409 ✓     |
| EX-02 | Excepción        | DataIntegrity dni dup    | 409      | 409 ✓     |
| EX-03 | Excepción        | RuntimeException         | 500      | 500 ✓     |
| HP-01 | Happy-path       | GET /api/users           | 200      | 200 ✓     |
| HP-02 | Happy-path       | GET /api/users/1         | 200      | 200 ✓     |
| HP-03 | Happy-path       | PUT /api/users/1         | 200      | 200 ✓     |
| HP-04 | Happy-path       | DELETE /api/users/1      | 204      | 204 ✓     |

### ActivityController (10 AC + 4 happy-path = 14 tests)

| ID    | Técnica          | Entrada                       | Esperado | Resultado |
|-------|------------------|-------------------------------|----------|-----------|
| AC-01 | Valor inválido   | monthlyCost=0.0               | 400      | 400 ✓     |
| AC-02 | Valor borde      | monthlyCost=0.01              | 200      | 200 ✓     |
| AC-03 | Valor inválido   | monthlyCost=-10.0             | 400      | 400 ✓     |
| AC-04 | Valor inválido   | monthlyCost=null              | 400      | 400 ✓     |
| AC-05 | Valor borde inf  | name="ab" (2 chars)           | 400      | 400 ✓     |
| AC-06 | Valor borde sup  | name="abc" (3 chars)          | 200      | 200 ✓     |
| AC-07 | Valor inválido   | name=""                       | 400      | 400 ✓     |
| AC-08 | Valor inválido   | day=""                        | 400      | 400 ✓     |
| AC-09 | Valor inválido   | schedule=""                   | 400      | 400 ✓     |
| AC-10 | Valor típico     | todos válidos                 | 200      | 200 ✓     |
| HP-05 | Happy-path       | GET /api/activities           | 200      | 200 ✓     |
| HP-06 | Happy-path       | GET /api/activities/1         | 200      | 200 ✓     |
| HP-07 | Happy-path       | PUT /api/activities/1         | 200      | 200 ✓     |
| HP-08 | Happy-path       | DELETE /api/activities/1      | 204      | 204 ✓     |

### AttendanceController (6 AT + 2 happy-path = 8 tests)

| ID    | Técnica          | Entrada                | Esperado | Resultado |
|-------|------------------|------------------------|----------|-----------|
| AT-01 | Valor inválido   | userId=null            | 400      | 400 ✓     |
| AT-02 | Valor inválido   | userId=-1              | 400      | 400 ✓     |
| AT-03 | Valor inválido   | userId=0               | 400      | 400 ✓     |
| AT-04 | Valor válido     | userId=1               | 200      | 200 ✓     |
| AT-05 | Opcional null    | notes=null             | 200      | 200 ✓     |
| AT-06 | Opcional vacío   | notes=""               | 200      | 200 ✓     |
| HP-09 | Happy-path       | GET /api/attendances   | 200      | 200 ✓     |
| HP-10 | Happy-path       | GET /api/attendances/user/1 | 200 | 200 ✓    |

### AuthController (1 test)

| ID    | Técnica    | Entrada                        | Esperado | Resultado |
|-------|------------|--------------------------------|----------|-----------|
| HP-11 | Happy-path | POST /api/auth/login con JWT   | 200 + token | 200 ✓  |

---

## Defectos detectados

### Defecto 1 — ActivityController sin @Valid

- **Detectado:** AC-01, AC-03, AC-04, AC-05, AC-07, AC-08, AC-09 → todos daban
  200 en lugar de 400 (ciclo TDD rojo, commit `4d21072`).
- **Causa:** `Activity.java` sin anotaciones de validación; `ActivityController`
  sin `@Valid` en `createActivity` y `updateActivity`.
- **Corrección:** commit `a9c721e` — se agregaron `@NotBlank`, `@Size`, `@NotNull`,
  `@Positive` al modelo y `@Valid` al controller.
- **Evidencia:** `01_tdd_activity_red.md`, `02_tdd_activity_green.md`.

### Defecto 2 — AttendanceRequest sin validaciones

- **Detectado:** AT-01, AT-02, AT-03 → daban 200 en lugar de 400 (ciclo TDD rojo,
  commit `9bbddc8`).
- **Causa:** `AttendanceRequest.userId` sin `@NotNull` ni `@Positive`; controller
  sin `@Valid`.
- **Corrección:** commit `21b0416` — se agregaron `@NotNull @Positive` al DTO y
  `@Valid` al controller.
- **Evidencia:** `03_tdd_attendance_red.md`, `04_tdd_attendance_green.md`.

### Defecto 3 — Rama perdida en UserService.updateUser

- **Detectado:** JaCoCo reportaba BRANCH 3/4 en `updateUser` (post-Bloque D).
- **Causa:** el path `password != null && password.isBlank()` no estaba cubierto.
- **Corrección:** se agregó `updateUser_cuandoPasswordEsBlank_noReencodea` a
  `UserServiceTest`. Ahora BRANCH 4/4.
- **Evidencia:** `06_defecto3_updateuser.md`.

---

## Setup técnico de testing

- **Capa service:** `@ExtendWith(MockitoExtension.class)` + `@Mock` + `@InjectMocks`.
- **Capa controller:** `@WebMvcTest(Controller.class)` + `@AutoConfigureMockMvc(addFilters=false)`
  + `@Import(GlobalExceptionHandler.class)`.
- **MockBeans estándar:** `UserService/ActivityService/AttendanceService`,
  `JwtUtil`, `UserDetailsService` (**interfaz**, NO la impl — para evitar
  `NoUniqueBeanDefinitionException` con el `InMemoryUserDetailsManager` de
  Spring Security), `PasswordEncoder`.
- **AuthController:** requiere además `@MockBean AuthenticationManager` y
  `@MockBean UserRepository`.

---

## Métricas finales

| Métrica                | Valor      |
|------------------------|------------|
| Total tests            | 72         |
| Tests fallidos         | 0          |
| Cobertura líneas global| 53.0%      |
| Cobertura controller/  | 100% líneas|
| Cobertura service/     | 100% líneas, 100% ramas |
| Commits Hito 4         | 11         |
| Defectos detectados    | 3          |
| Defectos corregidos    | 3          |
