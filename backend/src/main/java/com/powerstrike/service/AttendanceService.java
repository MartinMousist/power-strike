package com.powerstrike.service;

import com.powerstrike.dto.AttendanceRequest;
import com.powerstrike.model.Attendance;
import com.powerstrike.model.User;
import com.powerstrike.repository.AttendanceRepository;
import com.powerstrike.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAllByOrderByAttendanceDateDesc();
    }

    public List<Attendance> getAttendancesByUser(Long userId) {
        return attendanceRepository.findByUserIdOrderByAttendanceDateDesc(userId);
    }

    public Attendance registerAttendance(AttendanceRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setAttendanceDate(LocalDateTime.now());
        attendance.setNotes(request.getNotes());
        return attendanceRepository.save(attendance);
    }
}
