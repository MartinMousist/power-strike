# TDD Attendance — Estado ROJO

## Descripción

Estado intencional del ciclo TDD: los tests de validación de `AttendanceController`
se escriben ANTES de implementar las validaciones. `AttendanceRequest` no tiene
anotaciones de `jakarta.validation` y el controller no usa `@Valid`.

## Resultado de `mvn test` (resumen)

```
AttendanceControllerTest: Tests run: 6, Failures: 3, Errors: 0 <<< FAILURE
```

Los 54 tests anteriores siguen en verde.

## Casos AT-01…AT-06 — estado ROJO

| ID    | Campo  | Valor     | Esperado | Obtenido | Estado |
|-------|--------|-----------|----------|----------|--------|
| AT-01 | userId | null      | 400      | 200      | ROJO ✗ |
| AT-02 | userId | -1        | 400      | 200      | ROJO ✗ |
| AT-03 | userId | 0         | 400      | 200      | ROJO ✗ |
| AT-04 | userId | 1         | 2xx      | 200      | VERDE ✓ |
| AT-05 | notes  | null      | 2xx      | 200      | VERDE ✓ |
| AT-06 | notes  | "" (vacío)| 2xx      | 200      | VERDE ✓ |

**Resumen: 3 ROJO (defecto confirmado), 3 VERDE**

## Causa raíz

`AttendanceRequest.java` no tiene `@NotNull` ni `@Positive` en `userId`.
`AttendanceController.registerAttendance()` no usa `@Valid`.
Sin `@Valid`, cualquier `userId` (incluso null o negativo) llega al service.

## Próximo paso

Bloque B.2: agregar `@NotNull @Positive` a `AttendanceRequest.userId` y
`@Valid` al controller → todos los tests deben ponerse VERDE.
