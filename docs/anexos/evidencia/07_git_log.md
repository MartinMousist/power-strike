# Evidencia de historial Git

**Fecha de consulta:** 20/05/2026  
**Comando ejecutado:** `git log --format="%h %ad %s" --date=short`

---

## Output de git log

```text
004fa87 2026-05-20 chore(backend): add pmd plugin for complexity metrics
368a5d0 2026-05-20 test(attendance): add unit tests for AttendanceService
490eeef 2026-05-20 test(user): add unit tests for UserService
b78e0d8 2026-05-20 test(activity): add unit tests for ActivityService
37ed034 2026-05-20 chore(frontend): add eslint with vue plugin
d1f94f9 2026-05-20 docs: add README with project description, setup and usage
057231b 2026-05-20 chore(backend): add jacoco and checkstyle plugins
73a8265 2026-05-20 docs: add README and create docs folder
50c28af 2026-05-20 docs: add traceability comments linking code to functional requirements
87a662c 2026-05-13 fix: corregir error 403 en endpoints protegidos
dc7c481 2026-05-13 PRIMER COMMIT INICIAL DEL PROYECTO
82b8bd5 2026-05-13 feat: initial commit — Power Strike Gym Management System
```

---

## Tabla de commits

| Hash | Fecha | Mensaje | Tipo |
|---|---|---|---|
| `004fa87` | 20/05/2026 | add pmd plugin for complexity metrics | chore |
| `368a5d0` | 20/05/2026 | add unit tests for AttendanceService | test |
| `490eeef` | 20/05/2026 | add unit tests for UserService | test |
| `b78e0d8` | 20/05/2026 | add unit tests for ActivityService | test |
| `37ed034` | 20/05/2026 | add eslint with vue plugin | chore |
| `d1f94f9` | 20/05/2026 | add README with project description, setup and usage | docs |
| `057231b` | 20/05/2026 | add jacoco and checkstyle plugins | chore |
| `73a8265` | 20/05/2026 | add README and create docs folder | docs |
| `50c28af` | 20/05/2026 | add traceability comments linking code to functional requirements | docs |
| `87a662c` | 13/05/2026 | corregir error 403 en endpoints protegidos | fix |
| `dc7c481` | 13/05/2026 | PRIMER COMMIT INICIAL DEL PROYECTO | — |
| `82b8bd5` | 13/05/2026 | initial commit — Power Strike Gym Management System | feat |

---

## Análisis

- **Total de commits:** 12
- **Commits del Hito 3 (20/05/2026):** 9 commits
- **Convención de commits:** Conventional Commits (`feat/fix/docs/test/chore`) — adoptada a partir del commit `50c28af`
- **Distribución por tipo (Hito 3):**
  - `test`: 3 commits (tests unitarios de los 3 services)
  - `chore`: 3 commits (ESLint, JaCoCo+Checkstyle, PMD)
  - `docs`: 3 commits (README, trazabilidad, carpeta docs)

---

> El historial refleja un desarrollo incremental con commits atómicos por funcionalidad, siguiendo buenas prácticas de control de versiones.
