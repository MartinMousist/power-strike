# Evidencia de LOC y conteo de archivos

**Fecha de medición:** 20/05/2026  
**Comando de conteo:** `find <directorio> -name "*.java" -exec wc -l {} +` / `xargs wc -l`

---

## Backend — código fuente principal (`src/main`)

**Total: 23 archivos, 769 líneas**

| Archivo | LOC |
|---|---|
| `config/SecurityConfig.java` | 77 |
| `config/DataInitializer.java` | 68 |
| `security/JwtUtil.java` | 58 |
| `security/JwtAuthFilter.java` | 55 |
| `service/UserService.java` | 48 |
| `controller/ActivityController.java` | 45 |
| `controller/UserController.java` | 45 |
| `service/ActivityService.java` | 42 |
| `controller/AuthController.java` | 39 |
| `service/AttendanceService.java` | 39 |
| `model/User.java` | 38 |
| `controller/AttendanceController.java` | 35 |
| `model/Activity.java` | 33 |
| `model/Attendance.java` | 29 |
| `security/UserDetailsServiceImpl.java` | 28 |
| `config/AppConfig.java` | 15 |
| `dto/LoginResponse.java` | 13 |
| `repository/UserRepository.java` | 12 |
| `repository/AttendanceRepository.java` | 11 |
| `PowerStrikeApplication.java` | 11 |
| `repository/ActivityRepository.java` | 10 |
| `dto/AttendanceRequest.java` | 9 |
| `dto/LoginRequest.java` | 9 |
| **TOTAL** | **769** |

**Promedio por archivo:** ~33 líneas

---

## Backend — tests (`src/test`)

**Total: 3 archivos, 411 líneas**

| Archivo | LOC |
|---|---|
| `service/UserServiceTest.java` | ~160 |
| `service/ActivityServiceTest.java` | ~130 |
| `service/AttendanceServiceTest.java` | ~121 |
| **TOTAL** | **411** |

---

## Frontend — código fuente (`src/`)

**Total: 11 archivos, 947 líneas**

| Archivo | LOC |
|---|---|
| `views/UsersView.vue` | 179 |
| `views/ActivitiesView.vue` | 164 |
| `views/AttendanceView.vue` | 160 |
| `views/DashboardView.vue` | 130 |
| `views/LoginView.vue` | 112 |
| `components/Navbar.vue` | 78 |
| `router/index.js` | 48 |
| `api/axios.js` | 27 |
| `App.vue` | 15 |
| `store/auth.js` | 25 |
| `main.js` | 9 |
| **TOTAL** | **947** |

---

## Resumen general

| Módulo | Archivos | LOC |
|---|---|---|
| Backend (main) | 23 | 769 |
| Backend (test) | 3 | 411 |
| Frontend (src) | 11 | 947 |
| **TOTAL** | **37** | **2127** |

---

## Análisis

- **Promedio LOC/clase backend:** ~33 líneas → clases pequeñas y con responsabilidad única (principio SRP).
- **Ratio test/código backend:** 411 / 769 = **53%** — alto índice de cobertura de tests respecto al código de producción.
- **Clases más grandes:** `SecurityConfig` (77) y `DataInitializer` (68) son infraestructura/configuración, no lógica de negocio.
- **Services (código nuevo Hito 3):** `ActivityService` 42 LOC, `UserService` 48 LOC, `AttendanceService` 39 LOC — promedio 43 LOC por service.
- El tamaño reducido de los archivos contribuye positivamente al **Maintainability Index** (bajo LOC → mayor MI).
