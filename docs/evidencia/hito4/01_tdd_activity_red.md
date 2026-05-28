# TDD Activity — Estado ROJO

## Descripción

Estado intencional del ciclo TDD: los tests de validación de `ActivityController`
se escriben ANTES de implementar las validaciones. El modelo `Activity` no tiene
ninguna anotación de `jakarta.validation` y el controller no usa `@Valid`.
Todo request POST llega al service sin validar → retorna 200 para cualquier entrada.

## Resultado de `mvn test` (resumen)

```
Tests run: 10, Failures: 7, Errors: 0 <<< FAILURE
```

Los 23 tests anteriores (UserControllerTest + service tests) siguen verdes.

## Casos AC-01…AC-10 — estado ROJO

| ID    | Campo         | Valor         | Esperado | Obtenido | Estado |
|-------|---------------|---------------|----------|----------|--------|
| AC-01 | monthlyCost   | 0.0           | 400      | 200      | ROJO ✗ |
| AC-02 | monthlyCost   | 0.01          | 2xx      | 200      | VERDE ✓ |
| AC-03 | monthlyCost   | -10.0         | 400      | 200      | ROJO ✗ |
| AC-04 | monthlyCost   | null          | 400      | 200      | ROJO ✗ |
| AC-05 | name          | "ab" (2 chars)| 400      | 200      | ROJO ✗ |
| AC-06 | name          | "abc" (3 chars)| 2xx     | 200      | VERDE ✓ |
| AC-07 | name          | "" (vacío)    | 400      | 200      | ROJO ✗ |
| AC-08 | day           | "" (vacío)    | 400      | 200      | ROJO ✗ |
| AC-09 | schedule      | "" (vacío)    | 400      | 200      | ROJO ✗ |
| AC-10 | (todos válidos)| —            | 2xx      | 200      | VERDE ✓ |

**Resumen: 7 ROJO (defecto confirmado), 3 VERDE**

## Causa raíz

`Activity.java` no tiene `@NotBlank`, `@NotNull`, `@Positive`.
`ActivityController.createActivity()` no usa `@Valid`.
Sin `@Valid`, Spring no activa Bean Validation y el `GlobalExceptionHandler`
nunca intercepta nada → todos los requests devuelven 200.

## Próximo paso

Bloque A.2: agregar las anotaciones de validación a `Activity.java` y `@Valid`
al controller → todos los tests deben ponerse VERDE.
