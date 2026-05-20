# Evidencia de ejecución de Checkstyle

**Fecha de ejecución:** 20/05/2026  
**Comando ejecutado:** `cd backend && mvn checkstyle:check`  
**Configuración:** maven-checkstyle-plugin 3.3.1 + checkstyle 10.12.7 + google_checks.xml  
**failOnViolation:** false

---

## Output (resumen final)

```text
[INFO] --- maven-checkstyle-plugin:3.3.1:check (default-cli) @ powerstrike ---
[INFO] PMD version: 6.55.0
[WARN] .../controller/ActivityController.java:15:1: Falta el comentario Javadoc. [MissingJavadocType]
[WARN] .../controller/ActivityController.java:17:5: 'member def modifier' nivel 4, esperado 2. [Indentation]
... (358 warnings en total)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**Resultado:** BUILD SUCCESS — 358 warnings, **0 errores críticos**

---

## Tabla resumen de warnings por tipo

| Tipo | Ocurrencias |
|---|---|
| Indentation | 281 |
| CustomImportOrder | 29 |
| MissingJavadocType | 23 |
| MissingJavadocMethod | 8 |
| LineLength | 7 |
| AvoidStarImport | 7 |
| EmptyLineSeparator | 3 |
| **TOTAL** | **358** |

---

## Análisis

- **78% de los warnings son de indentación:** el código usa 4 espacios, Google Checks espera 2. Es una diferencia de estilo de configuración, no un defecto funcional.
- **Errores críticos:** 0 — cumple el umbral del Plan SQA.
- El build no se interrumpe gracias a `failOnViolation=false`.
- Los warnings de `MissingJavadocType` y `MissingJavadocMethod` corresponden a ausencia de documentación Javadoc, prevista para corregir en Hito 4.
