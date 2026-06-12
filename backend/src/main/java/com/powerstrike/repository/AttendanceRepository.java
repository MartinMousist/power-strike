package com.powerstrike.repository;

import com.powerstrike.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserIdOrderByAttendanceDateDesc(Long userId);
    List<Attendance> findAllByOrderByAttendanceDateDesc();

    /** ¿El usuario ya registró asistencia desde el inicio del día? (DEF-EXP-02). */
    boolean existsByUserIdAndAttendanceDateAfter(Long userId, LocalDateTime desde);
}
