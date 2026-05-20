# Power Strike — Sistema de Gestión de Actividades para Gimnasio

Sistema web para centralizar la gestión de usuarios, actividades y asistencias de un gimnasio.
Desarrollado como Trabajo de Campo para **Ingeniería de Software II — IUA**.

**Grupo 2:** Brizuela Mateo · Mousist Martin · Posada Fabricio Nicolás · Ponce Ernesto

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 + Maven |
| Frontend | Vue 3 + Vite + Pinia + Vue Router |
| Base de datos | PostgreSQL 16 |
| Autenticación | JWT (jjwt 0.11.5) + BCrypt |
| Contenedores | Docker + Docker Compose |
| Servidor web | Nginx (frontend) |
| HTTP client | Axios |

---

## Requerimientos funcionales

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
│   └── src/main/java/com/powerstrike/
│       ├── controller/       # Endpoints REST (Activity, Attendance, Auth, User)
│       ├── service/          # Lógica de negocio
│       ├── model/            # Entidades JPA
│       ├── repository/       # Repositorios Spring Data
│       ├── security/         # JWT filter, JwtUtil, UserDetailsService
│       ├── config/           # SecurityConfig, AppConfig, DataInitializer
│       ├── dto/              # DTOs de request/response
│       └── PowerStrikeApplication.java
├── frontend/
│   ├── package.json
│   └── src/
│       ├── views/            # Login, Dashboard, Users, Activities, Attendance
│       ├── components/       # Navbar
│       ├── store/            # auth.js (Pinia)
│       ├── router/           # index.js
│       └── api/              # axios.js
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## Levantar el proyecto con Docker Compose

**Prerequisitos:** Docker y Docker Compose instalados.

```bash
# 1. Clonar el repositorio
git clone https://github.com/MartinMousist/power-strike.git
cd power-strike

# 2. Crear el archivo de variables de entorno
cp .env.example .env

# 3. Levantar todos los servicios
docker compose up --build
```

Una vez levantado:

| Servicio | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8090/api |
| Base de datos | localhost:5433 |

Para detener los servicios:

```bash
docker compose down
```

---

## Correr los tests

```bash
cd backend
mvn test
```

Para generar el reporte de cobertura con JaCoCo:

```bash
mvn test jacoco:report
# El reporte queda en: backend/target/site/jacoco/index.html
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

## Variables de entorno

| Variable | Descripción | Default |
|---|---|---|
| `DB_NAME` | Nombre de la base de datos | `powerstrike_db` |
| `DB_USER` | Usuario de PostgreSQL | `powerstrike` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `powerstrike123` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | ver `.env.example` |

---

## Repositorio

https://github.com/MartinMousist/power-strike
