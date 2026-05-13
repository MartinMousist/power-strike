package com.powerstrike.controller;

import com.powerstrike.dto.AttendanceRequest;
import com.powerstrike.model.Attendance;
import com.powerstrike.service.AttendanceService;
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
    public ResponseEntity<Attendance> registerAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.registerAttendance(request));
    }
}
