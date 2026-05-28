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

    // REQ-F05 — Registrar y mostrar historial de asistencia de usuarios al gimnasio
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendancesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUser(userId));
    }

    // REQ-F05 — Registro de asistencia de un usuario
    @PostMapping
    public ResponseEntity<Attendance> registerAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.registerAttendance(request));
    }
}
