# Evidencia de ejecución de PMD (Complejidad Ciclomática)

**Fecha de ejecución:** 20/05/2026  
**Comandos ejecutados:**
- `cd backend && mvn pmd:pmd` — genera reporte XML en `target/pmd.xml`
- `cd backend && mvn pmd:check` — verifica umbrales

**Configuración:** maven-pmd-plugin 3.21.2 (PMD 6.55.0)  
**Ruleset:** `backend/pmd-ruleset.xml`  
**Reglas activas:** CyclomaticComplexity (methodReportLevel=10), CognitiveComplexity  
**includeTests:** false

---

## Output de mvn pmd:check

```text
[INFO] --- maven-pmd-plugin:3.21.2:check (default-cli) @ powerstrike ---
[INFO] PMD version: 6.55.0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**Resultado: 0 violaciones de CC — todos los métodos cumplen CC ≤ 10.**

---

## Interpretación

PMD reporta violaciones **solo cuando se supera el umbral configurado**. La ausencia de violaciones indica que ningún método del proyecto supera CC = 10.

El paquete `com.powerstrike.service` (código nuevo del Hito 3) tiene métodos de baja complejidad:

| Clase | Método más complejo | CC estimada |
|---|---|---|
| ActivityService | `updateActivity` (setea 5 campos + llama a getById) | 2 |
| UserService | `updateUser` (setea campos + condicional de password) | 3 |
| AttendanceService | `registerAttendance` (busca user + construye objeto) | 2 |

> La CC se estima como: número de caminos de ejecución independientes = número de `if/else/for/while/&&/||` + 1.

---

## Nota sobre Maintainability Index (MI)

PMD 6.x no calcula el Maintainability Index directamente. El MI es una métrica compuesta (Halstead Volume + CC + LOC) que requiere herramientas adicionales como:
- **SonarQube** (para un análisis integrado en CI/CD)
- **Radon** (Python, análisis offline)

Se considera como actividad prevista para el Hito 4. Los indicadores proxy disponibles en este Hito son:
- CC ≤ 10 en todos los métodos ✓
- LOC por clase: promedio ~38 líneas (ver `06_loc_y_archivos.md`) ✓
- 0 errores críticos en Checkstyle y ESLint ✓

**Umbral del Plan SQA:** CC ≤ 10 por método — **CUMPLIDO**.
