# TDD Attendance — Estado VERDE

## Ciclo cerrado: ROJO → VERDE

Se agregaron `@NotNull @Positive` a `AttendanceRequest.userId` y `@Valid`
en `AttendanceController.registerAttendance()`. Los 6 tests AT-01…AT-06 pasan.

## Resultado de `mvn test`

```
Tests run: 60, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
  UserControllerTest:       23 tests ✓
  ActivityControllerTest:   10 tests ✓
  AttendanceControllerTest:  6 tests ✓
  ActivityServiceTest:       7 tests ✓
  AttendanceServiceTest:     6 tests ✓
  UserServiceTest:           8 tests ✓
```

## AttendanceRequest.java — contenido final

```java
package com.powerstrike.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AttendanceRequest {
    @NotNull(message = "El userId es obligatorio")
    @Positive(message = "El userId debe ser un número positivo")
    private Long userId;
    private String notes;
}
```

## AttendanceController.java — contenido final

```java
package com.powerstrike.controller;

import com.powerstrike.dto.AttendanceRequest;
import com.powerstrike.model.Attendance;
import com.powerstrike.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendancesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Attendance> registerAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.registerAttendance(request));
    }
}
```

## Nota sobre el ciclo TDD

- **ROJO (commit 9bbddc8):** 3 tests fallando — defecto 2 confirmado.
- **VERDE (commit siguiente):** validaciones implementadas, todos los tests pasan.
