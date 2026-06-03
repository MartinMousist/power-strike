# Power Strike — Sistema de Gestión de Actividades para Gimnasio

Sistema web para centralizar la gestión de usuarios, actividades y asistencias de un gimnasio.
Desarrollado como Trabajo de Campo para la materia **Ingeniería de Software II — IUA**.
El objetivo del sistema es reemplazar la gestión manual de clientes, horarios y asistencias
con una plataforma web integrada, accesible para administradores, entrenadores y clientes.

**Grupo 2:** Brizuela Mateo · Mousist Martin · Posada Fabricio Nicolás · Ponce Ernesto

[![CI — Tests + Cobertura](https://github.com/MartinMousist/power-strike/actions/workflows/ci.yml/badge.svg)](https://github.com/MartinMousist/power-strike/actions/workflows/ci.yml)

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 + Maven |
| Frontend | Vue 3 + Vite + Pinia + Vue Router |
| Base de datos | PostgreSQL 16 |
| Autenticación | JWT (jjwt 0.11.5) + BCrypt |
| Contenedores | Docker + Docker Compose |
| Servidor web | Nginx (frontend en producción) |
| Testing | JUnit 5 + Mockito + JaCoCo |
| Linting | Checkstyle (backend) + ESLint (frontend) |

---

## Prerrequisitos

- Java 17
- Maven 3.8+
- Node.js 18+
- Docker y Docker Compose
- Git

---

## Cómo levantar el proyecto

### Opción A — Entorno completo con Docker Compose

```bash
# 1. Clonar el repositorio
git clone https://github.com/MartinMousist/power-strike.git
cd power-strike

# 2. Crear el archivo de variables de entorno
cp .env.example .env

# 3. Levantar todos los servicios (base de datos, backend y frontend)
docker compose up --build
```

| Servicio | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8090/api |
| Base de datos | localhost:5433 |

```bash
# Detener los servicios
docker compose down
```

### Opción B — Modo desarrollo

```bash
# 1. Levantar solo la base de datos
docker compose up -d db

# 2. Configurar variables de entorno
cp .env.example .env

# 3. Iniciar el backend
cd backend
mvn spring-boot:run

# 4. En otra terminal, iniciar el frontend
cd frontend
npm install
npm run dev
```

---

## Correr los tests

```bash
cd backend
mvn test
```

---

## Generar reporte de cobertura (JaCoCo)

```bash
cd backend
mvn verify
# Reporte generado en: backend/target/site/jacoco/index.html
```

---

## Correr los linters

**Backend (Checkstyle):**

```bash
cd backend
mvn checkstyle:check
```

**Frontend (ESLint):**

```bash
cd frontend
npm run lint
```

---

## Calidad y Aseguramiento (Hito 4)

El backend cuenta con una suite de pruebas automatizadas y verificación de calidad continua.

| Aspecto | Detalle |
|---|---|
| Tests automatizados | **72 tests** (JUnit 5 + Mockito), 100% en verde |
| Caja negra | 50 tests de capa web (controllers) vía MockMvc, basados en la especificación |
| Caja blanca | 22 tests de capa de servicio que recorren la lógica interna (cobertura de ramas) |
| Técnicas aplicadas | Partición de equivalencia, valores límite, tabla de decisión, máquina de estados |
| Cobertura (JaCoCo) | 100% en módulos principales (controller, service, model) |
| Complejidad ciclomática | CC ≤ 10 en todos los métodos (PMD, 0 violaciones) |
| TDD | 2 ciclos Red-Green documentados (Activity y Attendance) |
| Defectos | 6 documentados como [Issues](https://github.com/MartinMousist/power-strike/issues) (severidad + prioridad + trazabilidad) |
| Integración continua | Pipeline de GitHub Actions: tests + cobertura en cada push |

La documentación completa del Hito 4 (plan de pruebas, casos, reporte de defectos, FMEA y
máquina de estados) está en [`docs/Hito_4_PowerStrike.docx`](docs/Hito_4_PowerStrike.docx).

---

## Requerimientos funcionales implementados

| ID | Descripción |
|---|---|
| REQ-F01 | Registrar usuarios con nombre, email y DNI |
| REQ-F02 | Mostrar listado de usuarios registrados |
| REQ-F03 | Gestionar actividades (crear, editar, visualizar) con nombre, día, horario y costo |
| REQ-F04 | Mostrar actividades disponibles para consulta |
| REQ-F05 | Registrar y mostrar historial de asistencia de usuarios al gimnasio |

---

## Estructura del proyecto

```
power-strike/
├── backend/
│   ├── pom.xml
│   ├── checkstyle.xml
│   └── src/main/java/com/powerstrike/
│       ├── controller/       # Endpoints REST (Activity, Attendance, Auth, User)
│       ├── service/          # Lógica de negocio
│       ├── model/            # Entidades JPA
│       ├── repository/       # Repositorios Spring Data
│       ├── security/         # JWT filter, JwtUtil, UserDetailsService
│       ├── config/           # SecurityConfig, AppConfig, DataInitializer
│       └── dto/              # DTOs de request/response
├── frontend/
│   ├── package.json
│   └── src/
│       ├── views/            # Login, Dashboard, Users, Activities, Attendance
│       ├── components/       # Navbar
│       ├── store/            # auth.js (Pinia)
│       ├── router/           # index.js
│       └── api/              # axios.js
├── docs/                     # Plan SQA, métricas, informe Hito 4, presentación
│   └── evidencia/            # Evidencia de ejecución (tests, JaCoCo, PMD, TDD)
├── .github/workflows/        # ci.yml — pipeline de Integración Continua
├── docker-compose.yml
└── .env.example
```

---

## Variables de entorno

| Variable | Descripción | Default |
|---|---|---|
| `DB_NAME` | Nombre de la base de datos | `powerstrike_db` |
| `DB_USER` | Usuario de PostgreSQL | `powerstrike` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `powerstrike123` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | ver `.env.example` |

---

Materia: Ingeniería de Software II — IUA | Hito 4 | Entrega: 03/06/2026
