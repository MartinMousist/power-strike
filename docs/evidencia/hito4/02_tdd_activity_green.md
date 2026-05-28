# TDD Activity — Estado VERDE

## Ciclo cerrado: ROJO → VERDE

Se agregaron las anotaciones de validación a `Activity.java` y `@Valid` en
`ActivityController`. Los 10 tests AC-01…AC-10 pasan en verde.

## Resultado de `mvn test`

```
Tests run: 54, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
  UserControllerTest:   23 tests ✓
  ActivityControllerTest: 10 tests ✓
  ActivityServiceTest:    7 tests ✓
  AttendanceServiceTest:  6 tests ✓
  UserServiceTest:        8 tests ✓
```

## Activity.java — contenido final

```java
package com.powerstrike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El día no puede estar vacío")
    @Column(nullable = false)
    private String day;

    @NotBlank(message = "El horario no puede estar vacío")
    @Column(nullable = false)
    private String schedule;

    @NotNull(message = "El costo mensual es obligatorio")
    @Positive(message = "El costo mensual debe ser mayor a cero")
    @Column(nullable = false)
    private Double monthlyCost;

    @Column(nullable = false)
    private boolean active = true;
}
```

## ActivityController.java — contenido final

```java
package com.powerstrike.controller;

import com.powerstrike.model.Activity;
import com.powerstrike.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.createActivity(activity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @Valid @RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.updateActivity(id, activity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Nota sobre el ciclo TDD

- **ROJO (commit 4d21072):** 7 tests fallando — defecto 1 confirmado.
- **VERDE (commit siguiente):** validaciones implementadas, todos los tests pasan.
- El `GlobalExceptionHandler` intercepta los `MethodArgumentNotValidException`
  y devuelve 400 con mapa de errores por campo.
