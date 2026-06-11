# Análisis heurístico (10 heurísticas de Nielsen) — Power Strike (Hito 5)

Evaluación de usabilidad de la app (pantallas: Login, Dashboard, Usuarios, Actividades, Asistencias)
según las 10 heurísticas de Jakob Nielsen. Cada una incluye **hallazgos positivos**, **problemas
detectados** con su **severidad** (0 = no es problema · 1 = cosmético · 2 = menor · 3 = mayor · 4 = catastrófico)
y una **recomendación**.

---

### 1. Visibilidad del estado del sistema
*El sistema debe informar al usuario qué está pasando mediante feedback oportuno.*
- ✅ Botones con estado "Guardando…" durante operaciones; navegación con la vista activa marcada.
- ⚠️ **(Sev. 2)** Falta un indicador de carga global al traer listados (usuario/actividades). Si la API tarda, la pantalla parece vacía.
- 💡 Agregar spinners/skeletons en listados y un toast de confirmación tras crear/editar.

### 2. Correspondencia entre el sistema y el mundo real
*Usar el lenguaje del usuario, no términos técnicos.*
- ✅ Vocabulario del dominio gimnasio: "Actividades", "Asistencias", "Usuarios", "Costo mensual".
- ⚠️ **(Sev. 1)** Mensajes de error de validación a veces muestran texto técnico (ej. patrón de DNI).
- 💡 Traducir los mensajes a lenguaje natural ("El DNI debe tener 7 u 8 dígitos").

### 3. Control y libertad del usuario
*"Salidas de emergencia" claras: deshacer/cancelar.*
- ✅ Botón Cancelar en los formularios; navegación libre por el menú.
- ⚠️ **(Sev. 3)** No hay confirmación al eliminar (usuario/actividad) ni "deshacer". Una eliminación accidental es irreversible.
- 💡 Modal de confirmación antes de borrar y, si se puede, baja lógica en vez de borrado físico.

### 4. Consistencia y estándares
*Misma acción = misma palabra/ubicación en toda la app.*
- ✅ Layout consistente (Navbar fija + vistas), botones y formularios con el mismo estilo.
- ⚠️ **(Sev. 1)** Verificar que todos los botones de acción primaria tengan el mismo color/posición.
- 💡 Definir un sistema de design tokens (colores, espaciados) reutilizado en todas las vistas.

### 5. Prevención de errores
*Mejor prevenir que mostrar un buen mensaje de error.*
- ✅ Validaciones en backend (`@Valid`, `@Pattern`, `@Size`, `@Positive`) que bloquean datos inválidos.
- ⚠️ **(Sev. 2)** La validación en el frontend es más débil que en el backend → el usuario llega a enviar y recién ahí ve el error.
- 💡 Replicar las validaciones clave en el formulario (deshabilitar "Guardar" si el form es inválido).

### 6. Reconocer mejor que recordar
*Minimizar la carga de memoria: opciones y acciones visibles.*
- ✅ Día y rol como **selects** (no texto libre) → el usuario reconoce las opciones.
- ⚠️ **(Sev. 2)** Al registrar asistencia hay que conocer/recordar el `userId`. No hay buscador de paciente por nombre.
- 💡 Selector/autocompletar de usuario por nombre en el registro de asistencia.

### 7. Flexibilidad y eficiencia de uso
*Atajos para usuarios expertos sin estorbar a los novatos.*
- ⚠️ **(Sev. 2)** No hay filtros, búsqueda ni paginación en los listados; con muchos registros se vuelve lento de usar.
- 💡 Agregar búsqueda/filtros y paginación en Usuarios y Actividades.

### 8. Diseño estético y minimalista
*Sin información irrelevante que compita con la importante.*
- ✅ Interfaz limpia, pocas pantallas, foco en la tarea.
- ⚠️ **(Sev. 1)** El Dashboard podría mostrar métricas útiles (cantidad de usuarios/actividades/asistencias del día) en vez de quedar vacío.
- 💡 Tarjetas de resumen en el Dashboard.

### 9. Ayudar a reconocer, diagnosticar y recuperarse de errores
*Errores en lenguaje claro, que indiquen el problema y la solución.*
- ✅ `GlobalExceptionHandler` devuelve códigos HTTP coherentes (400/409/500) con cuerpo de error.
- ⚠️ **(Sev. 3)** Detectado en testing exploratorio: userId inexistente devuelve **500** en vez de 404 (DEF-EXP-01); mensaje incorrecto al borrar usuario con asistencias (DEF-EXP-05).
- 💡 Mapear esos casos a 404/409 con mensajes claros en la UI.

### 10. Ayuda y documentación
*Aunque lo ideal es no necesitarla, debe estar disponible.*
- ✅ README completo con instalación y uso (orientado a desarrolladores).
- ⚠️ **(Sev. 2)** No hay ayuda contextual ni tooltips para el usuario final (recepcionista/entrenador).
- 💡 Tooltips en campos no obvios y una breve guía de uso por pantalla.

---

## Resumen de hallazgos por severidad

| Severidad | Cantidad | Ejemplos |
|---|:---:|---|
| 3 — Mayor | 3 | Sin confirmación al borrar · userId inexistente → 500 · falta búsqueda de usuario en asistencia |
| 2 — Menor | 5 | Sin loading en listados · validación front débil · sin filtros/paginación · sin ayuda contextual |
| 1 — Cosmético | 4 | Mensajes técnicos · consistencia de botones · Dashboard vacío |

## Prioridad de mejoras (top 3)
1. **Confirmación antes de eliminar** (H3) — evita pérdida de datos irreversible.
2. **Corregir códigos de error** 404/409 (H9) — alinea con DEF-EXP-01/05.
3. **Búsqueda/selector de usuario en asistencias** (H6) — reduce errores y carga de memoria.
