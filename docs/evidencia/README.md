# Carpeta de Evidencias — Hito 3

**Proyecto:** Power Strike — Sistema de Gestión de Gimnasio  
**Materia:** Ingeniería de Software II — IUA  
**Fecha de generación:** 20/05/2026

---

## Índice de archivos

| Archivo | Contenido | Resultado |
|---|---|---|
| [01_tests_output.md](01_tests_output.md) | Output de `mvn test` — 21 tests unitarios (JUnit 5 + Mockito) | ✅ 21/21 pasaron |
| [02_cobertura_jacoco.md](02_cobertura_jacoco.md) | Reporte JaCoCo — cobertura por instrucción/rama/método | ✅ 100% en services (código nuevo) |
| [03_checkstyle_output.md](03_checkstyle_output.md) | Output de `mvn checkstyle:check` — Google Checks | ✅ 0 errores críticos |
| [04_eslint_output.md](04_eslint_output.md) | Output de `npm run lint` — ESLint 9 + eslint-plugin-vue 10 | ✅ 0 errores (163 warnings de formato) |
| [05_pmd_output.md](05_pmd_output.md) | Output de `mvn pmd:check` — CyclomaticComplexity | ✅ 0 violaciones CC > 10 |
| [06_loc_y_archivos.md](06_loc_y_archivos.md) | Conteo de líneas de código y archivos por módulo | 37 archivos, 2127 LOC |
| [07_git_log.md](07_git_log.md) | Historial de commits del repositorio | 12 commits, 9 en Hito 3 |

---

## Cumplimiento de umbrales del Plan SQA

| Métrica | Umbral | Resultado | Estado |
|---|---|---|---|
| Complejidad ciclomática | CC ≤ 10/método | Máx CC = 3 (UserService) | ✅ CUMPLIDO |
| Cobertura de código (nuevo) | ≥ 60% instrucciones | 100% en `com.powerstrike.service` | ✅ CUMPLIDO |
| Errores críticos Checkstyle | 0 errores | 0 errores (358 warnings estilo) | ✅ CUMPLIDO |
| Errores críticos ESLint | 0 errores | 0 errores (163 warnings formato) | ✅ CUMPLIDO |
| Tests unitarios | Todos pasan | 21/21 ✓ | ✅ CUMPLIDO |

---

## Comandos de reproducción

```bash
# Tests + cobertura JaCoCo
cd backend && mvn verify

# Checkstyle
cd backend && mvn checkstyle:check

# PMD (Complejidad Ciclomática)
cd backend && mvn pmd:check

# ESLint
cd frontend && npm run lint
```

---

> Los reportes HTML completos de JaCoCo se encuentran en `backend/target/site/jacoco/index.html` tras ejecutar `mvn verify`.
