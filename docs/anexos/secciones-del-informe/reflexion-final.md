# Reflexión final — Power Strike (Hito 5)

## Qué aprendimos

A lo largo del trabajo de campo migramos un sistema de gestión de gimnasio a una arquitectura moderna
(Java + Spring Boot en el backend, Vue 3 en el frontend) y, sobre todo, incorporamos una cultura de
calidad que al principio no teníamos:

- Testing como parte del desarrollo, no como un agregado. Pasamos de 21 a 78 tests automatizados y
  aplicamos TDD (Red-Green-Refactor) sobre funcionalidades nuevas: escribir el test primero nos
  obligó a pensar el caso de uso antes que el código.
- Técnicas de diseño de casos. Aprendimos a justificar cada test con una técnica (partición de
  equivalencia, valores límite, tabla de decisión, máquina de estados), en vez de probar "a ojo".
- Métricas que importan. Entendimos que la cobertura es un indicador, no un objetivo: vale más el
  100 % en los módulos principales que un número global inflado. También medimos complejidad
  ciclomática (CC ≤ 10) y mantenibilidad.
- Integración continua. Montar el pipeline de GitHub Actions cambió la dinámica: cada push corre los
  tests solo y avisa si algo se rompe.
- Trazabilidad. La RTM y la gestión de defectos como Issues nos dieron una visión clara de qué
  requerimiento cubre qué código y qué test.
- Trabajo en equipo con Git. Convención de commits, ramas y revisión nos ordenaron el avance.

## Qué haríamos distinto

- Empezar por los tests desde el día 1. Arrancamos a testear en serio en el Hito 4; si lo hubiéramos
  hecho desde el principio, varios defectos (los del testing exploratorio) no habrían llegado tan lejos.
- Implementar el control de roles (RBAC) antes. El defecto crítico DEF-EXP-04 (un CLIENT accede a
  todo) muestra que dejamos la autorización para el final; lo corregimos en el Hito 5 con TDD.
- Reforzar la validación en el frontend, no solo en el backend, para dar feedback inmediato al usuario.
- Cubrir también la capa de seguridad y el frontend con tests, que quedaron con baja cobertura.
- Pulir la UX según el análisis heurístico: confirmación al borrar, búsqueda/paginación, mensajes de
  error claros (404/409 en vez de 500).
- Cerrar el ciclo de los defectos abiertos antes de la entrega final.

## Cierre

El proyecto nos dejó algo más valioso que la app en sí: un método de trabajo (planificar, testear,
medir, documentar y automatizar) que podemos llevar a cualquier proyecto futuro.
