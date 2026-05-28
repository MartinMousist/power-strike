# Tests de Controllers — Estado Acumulado

## Resumen (se actualiza con cada bloque)

### UserControllerTest (27 tests)
| Grupo          | Tests | Qué cubren |
|----------------|-------|------------|
| CP-01…CP-20    | 20    | Validaciones de campos (DNI, password, name, role, email) |
| EX-01…EX-03    |  3    | GlobalExceptionHandler (409 email dup, 409 dni dup, 500 runtime) |
| Happy-path GET/PUT/DELETE | 4 | getAllUsers, getUserById, updateUser, deleteUser |

### ActivityControllerTest (10 tests)
| Grupo     | Tests | Qué cubren |
|-----------|-------|------------|
| AC-01…AC-10 | 10 | Validaciones (monthlyCost, name, day, schedule) — TDD red→green |

### AttendanceControllerTest (6 tests)
| Grupo     | Tests | Qué cubren |
|-----------|-------|------------|
| AT-01…AT-06 | 6 | Validaciones userId (null, negativo, cero, positivo) + notes opcional |

## Total acumulado hasta Bloque C

```
UserControllerTest:       27 tests ✓
ActivityControllerTest:   10 tests ✓
AttendanceControllerTest:  6 tests ✓
ActivityServiceTest:       7 tests ✓
AttendanceServiceTest:     6 tests ✓
UserServiceTest:           8 tests ✓
─────────────────────────────────────
TOTAL:                    64 tests, 0 failures — BUILD SUCCESS
```

_(Este archivo se actualizará al completar el Bloque D con happy-path de
ActivityController, AttendanceController y AuthController)_
