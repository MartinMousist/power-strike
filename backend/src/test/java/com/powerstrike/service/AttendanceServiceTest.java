package com.powerstrike.service;

import com.powerstrike.dto.AttendanceRequest;
import com.powerstrike.exception.ConflictException;
import com.powerstrike.exception.NotFoundException;
import com.powerstrike.model.Attendance;
import com.powerstrike.model.User;
import com.powerstrike.repository.AttendanceRepository;
import com.powerstrike.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AttendanceService attendanceService;

    private User usuario;
    private Attendance asistencia1;
    private Attendance asistencia2;

    @BeforeEach
    void setUp() {
        usuario = new User(1L, "Juan Pérez", "juan@email.com", "12345678", "hash", "CLIENT", true);

        asistencia1 = new Attendance(1L, usuario, LocalDateTime.of(2026, 5, 20, 9, 0), "Nota 1");
        asistencia2 = new Attendance(2L, usuario, LocalDateTime.of(2026, 5, 19, 18, 0), null);
    }

    // REQ-F05 — Mostrar historial de asistencia
    @Test
    void getAllAttendances_devuelveListaOrdenadaPorFecha() {
        when(attendanceRepository.findAllByOrderByAttendanceDateDesc())
                .thenReturn(Arrays.asList(asistencia1, asistencia2));

        List<Attendance> resultado = attendanceService.getAllAttendances();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(attendanceRepository, times(1)).findAllByOrderByAttendanceDateDesc();
    }

    @Test
    void getAttendancesByUser_devuelveAsistenciasDelUsuario() {
        when(attendanceRepository.findByUserIdOrderByAttendanceDateDesc(1L))
                .thenReturn(Arrays.asList(asistencia1, asistencia2));

        List<Attendance> resultado = attendanceService.getAttendancesByUser(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(usuario, resultado.get(0).getUser());
        verify(attendanceRepository, times(1)).findByUserIdOrderByAttendanceDateDesc(1L);
    }

    @Test
    void getAttendancesByUser_cuandoNoHayAsistencias_devuelveListaVacia() {
        when(attendanceRepository.findByUserIdOrderByAttendanceDateDesc(99L))
                .thenReturn(List.of());

        List<Attendance> resultado = attendanceService.getAttendancesByUser(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(attendanceRepository, times(1)).findByUserIdOrderByAttendanceDateDesc(99L);
    }

    // REQ-F05 — Registrar asistencia
    @Test
    void registerAttendance_cuandoUsuarioExiste_guardaYDevuelveAsistencia() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(1L);
        request.setNotes("Vino puntual");

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> {
            Attendance a = inv.getArgument(0);
            a = new Attendance(10L, a.getUser(), a.getAttendanceDate(), a.getNotes());
            return a;
        });

        Attendance resultado = attendanceService.registerAttendance(request);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUser());
        assertNotNull(resultado.getAttendanceDate());
        assertEquals("Vino puntual", resultado.getNotes());
        verify(userRepository, times(1)).findById(1L);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void registerAttendance_asignaFechaYHoraActual() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(1L);
        request.setNotes(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        Attendance resultado = attendanceService.registerAttendance(request);
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);

        assertNotNull(resultado.getAttendanceDate());
        assertTrue(resultado.getAttendanceDate().isAfter(antes));
        assertTrue(resultado.getAttendanceDate().isBefore(despues));
    }

    @Test
    void registerAttendance_cuandoUsuarioNoExiste_lanzaRuntimeException() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(99L);
        request.setNotes(null);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> attendanceService.registerAttendance(request));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(attendanceRepository, never()).save(any());
    }

    // DEF-EXP-01 — usuario inexistente debe dar 404 (NotFoundException), no 500
    @Test
    void registerAttendance_usuarioNoExiste_lanzaNotFound() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> attendanceService.registerAttendance(request));
    }

    // DEF-EXP-02 — no se permite registrar dos asistencias del mismo usuario el mismo día
    @Test
    void registerAttendance_duplicadoMismoDia_lanzaConflict() {
        AttendanceRequest request = new AttendanceRequest();
        request.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        lenient().when(attendanceRepository.existsByUserIdAndAttendanceDateAfter(eq(1L), any()))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> attendanceService.registerAttendance(request));
        verify(attendanceRepository, never()).save(any());
    }
}
