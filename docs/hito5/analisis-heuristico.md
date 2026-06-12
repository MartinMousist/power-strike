# Análisis heurístico (10 heurísticas de Nielsen) — Power Strike (Hito 5)

Evaluación de usabilidad de la aplicación (pantallas: Login, Dashboard, Usuarios, Actividades y Asistencias) según las 10 heurísticas de Jakob Nielsen. Para cada una se indican fortalezas, problemas detectados con su severidad (1 cosmético, 2 menor, 3 mayor, 4 catastrófico) y una recomendación.

### 1. Visibilidad del estado del sistema
El sistema debe informar al usuario qué está pasando mediante feedback oportuno.
- Fortaleza: los botones muestran el estado "Guardando…" durante las operaciones; la navegación marca la vista activa.
- Problema (sev. 2): falta un indicador de carga al traer los listados. Si la API tarda, la pantalla parece vacía.
- Recomendación: agregar spinners o skeletons en los listados y un aviso de confirmación tras crear o editar.

### 2. Correspondencia entre el sistema y el mundo real
Usar el lenguaje del usuario, no términos técnicos.
- Fortaleza: vocabulario del dominio gimnasio (Actividades, Asistencias, Usuarios, Costo mensual).
- Problema (sev. 1): algunos mensajes de validación muestran texto técnico (por ejemplo, el patrón del DNI).
- Recomendación: traducir los mensajes a lenguaje natural ("El DNI debe tener 7 u 8 dígitos").

### 3. Control y libertad del usuario
Salidas de emergencia claras: deshacer o cancelar.
- Fortaleza: botón Cancelar en los formularios y navegación libre por el menú.
- Problema (sev. 3): no hay confirmación al eliminar un usuario o actividad ni opción de deshacer; una eliminación accidental es irreversible.
- Recomendación: mostrar un diálogo de confirmación antes de borrar y, de ser posible, usar baja lógica en lugar de borrado físico.

### 4. Consistencia y estándares
La misma acción debe usar la misma palabra y ubicación en toda la aplicación.
- Fortaleza: layout consistente (barra de navegación fija y vistas), botones y formularios con el mismo estilo.
- Problema (sev. 1): conviene verificar que todos los botones de acción primaria tengan el mismo color y posición.
- Recomendación: definir un conjunto de tokens de diseño (colores, espaciados) reutilizado en todas las vistas.

### 5. Prevención de errores
Es mejor prevenir el error que mostrar un buen mensaje.
- Fortaleza: validaciones en el backend (@Valid, @Pattern, @Size, @Positive) que bloquean datos inválidos.
- Problema (sev. 2): la validación en el frontend es más débil que en el backend; el usuario llega a enviar el formulario y recién ahí ve el error.
- Recomendación: replicar las validaciones clave en el formulario y deshabilitar el botón Guardar si el formulario es inválido.

### 6. Reconocer mejor que recordar
Minimizar la carga de memoria: opciones y acciones visibles.
- Fortaleza: el día y el rol se eligen con un select (no texto libre), por lo que el usuario reconoce las opciones.
- Problema (sev. 2): al registrar una asistencia hay que conocer el userId; no hay un buscador de usuario por nombre.
- Recomendación: agregar un selector con autocompletado de usuario por nombre en el registro de asistencia.

### 7. Flexibilidad y eficiencia de uso
Atajos para usuarios expertos sin estorbar a los novatos.
- Problema (sev. 2): no hay filtros, búsqueda ni paginación en los listados; con muchos registros se vuelve lento de usar.
- Recomendación: agregar búsqueda, filtros y paginación en Usuarios y Actividades.

### 8. Diseño estético y minimalista
Sin información irrelevante que compita con la importante.
- Fortaleza: interfaz limpia, pocas pantallas y foco en la tarea.
- Problema (sev. 1): el Dashboard podría mostrar métricas útiles (cantidad de usuarios, actividades y asistencias del día) en lugar de quedar vacío.
- Recomendación: incorporar tarjetas de resumen en el Dashboard.

### 9. Ayudar a reconocer, diagnosticar y recuperarse de errores
Errores en lenguaje claro, que indiquen el problema y la solución.
- Fortaleza: el GlobalExceptionHandler devuelve códigos HTTP coherentes (400, 409, 404, 403) con un cuerpo de error.
- Problema (sev. 3): detectado en testing exploratorio, el mensaje al eliminar un usuario con asistencias no corresponde al error real (DEF-EXP-05). El caso de userId inexistente, que antes devolvía 500, ya se corrigió a 404 (DEF-EXP-01).
- Recomendación: corregir el mensaje de la eliminación con clave foránea y mostrarlo de forma clara en la interfaz.

### 10. Ayuda y documentación
Aunque lo ideal es no necesitarla, debe estar disponible.
- Fortaleza: README completo con instalación y uso, orientado a desarrolladores.
- Problema (sev. 2): no hay ayuda contextual ni tooltips para el usuario final (recepcionista o entrenador).
- Recomendación: agregar tooltips en los campos no obvios y una breve guía de uso por pantalla.

## Resumen de hallazgos por severidad

| Severidad | Cantidad | Ejemplos |
|---|:---:|---|
| Mayor (3) | 2 | sin confirmación al borrar; mensaje incorrecto al borrar usuario con asistencias |
| Menor (2) | 5 | sin indicador de carga; validación de frontend débil; sin filtros ni paginación; sin buscador de usuario; sin ayuda contextual |
| Cosmético (1) | 3 | mensajes técnicos; consistencia de botones; Dashboard vacío |

## Prioridad de mejoras (top 3)
1. Confirmación antes de eliminar (heurística 3): evita la pérdida de datos irreversible.
2. Corregir el mensaje de error al borrar con clave foránea (heurística 9, DEF-EXP-05).
3. Buscador o selector de usuario en el registro de asistencias (heurística 6).

Nota: el control de roles, que era un problema crítico de seguridad y usabilidad (DEF-EXP-04), se corrigió en el Hito 5 con autorización por rol; un usuario CLIENT ya no puede acceder a la gestión de usuarios.
