# Defecto 3 — Rama perdida en updateUser

## Identificación

**Método:** `UserService.updateUser()` — línea 39  
**Condición:**
```java
if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
    user.setPassword(passwordEncoder.encode(updated.getPassword()));
}
```

**Rama perdida:** password es **no-null pero blank** (`""`).

La condición compuesta tiene 4 ramas lógicas:
1. `null` → condición falsa (cubierta: test `cuandoPasswordEsNulo`)
2. `not null AND not blank` → condición verdadera (cubierta: test con "newPass")
3. `not null AND blank` → condición falsa (**NO cubierta**)
4. (cortocircuito interno — idem rama 1)

JaCoCo reportaba `BRANCH 3/4` en `updateUser`.

## Decisión

La rama es **real y testeable**: representa el caso en que el frontend envía
`password: ""` en una actualización (campo vacío, no password nuevo).
Es una condición defensiva válida que merece cobertura.

**Acción tomada:** se agregó un test a `UserServiceTest.java`:

```java
@Test
void updateUser_cuandoPasswordEsBlank_noReencodea() {
    User datosActualizados = new User(null, "Juan Actualizado", "juan@email.com",
            "12345678", "", "ADMIN", true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
    when(userRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

    userService.updateUser(1L, datosActualizados);

    verify(passwordEncoder, never()).encode(any());
    verify(userRepository, times(1)).save(usuarioAdmin);
}
```

## Resultado

| Antes | Después |
|-------|---------|
| BRANCH 3/4 (75%) | BRANCH 4/4 (100%) |

`UserService.updateUser` ahora tiene cobertura de ramas completa.
Total tests: 72 (0 failures).
