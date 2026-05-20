# Evidencia de ejecución de ESLint

**Fecha de ejecución:** 20/05/2026  
**Comando ejecutado:** `cd frontend && npm run lint`  
**Configuración:** ESLint 9 + eslint-plugin-vue 10, flat config (eslint.config.mjs)  
**Extiende:** `flat/recommended` (Vue 3)

---

## Output (resumen final)

```text
> powerstrike-frontend@0.0.1 lint
> eslint .

/src/views/UsersView.vue
  61:44   warning  'type' should be on a new line                         vue/max-attributes-per-line
  61:60   warning  ':required' should be on a new line                    vue/max-attributes-per-line
  ...
/src/views/ActivitiesView.vue
  ...
/src/views/AttendanceView.vue
  ...

✖ 163 problems (0 errors, 163 warnings)
  0 errors and 138 warnings potentially fixable with the --fix option.
```

**Resultado:** 163 warnings, **0 errores** — cumple el umbral del Plan SQA.

---

## Tabla resumen de warnings por regla

| Regla | Ocurrencias |
|---|---|
| vue/singleline-html-element-content-newline | 82 |
| vue/max-attributes-per-line | 71 |
| vue/html-self-closing | 10 |
| **TOTAL** | **163** |

---

## Análisis

- **Todos son warnings de formato HTML en templates Vue**, no errores de lógica.
- `vue/singleline-html-element-content-newline`: elementos inline que el estándar recomienda en múltiples líneas.
- `vue/max-attributes-per-line`: atributos HTML que se podrían separar en líneas individuales.
- `vue/html-self-closing`: elementos void (`<input>`) que deberían cerrarse como `<input />`.
- **Errores:** 0 — cumple el umbral del Plan SQA.
- 138 de los 163 warnings son auto-corregibles con `npm run lint -- --fix`.
