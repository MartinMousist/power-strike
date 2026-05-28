package com.powerstrike.controller;

import com.powerstrike.config.GlobalExceptionHandler;
import com.powerstrike.model.Attendance;
import com.powerstrike.security.JwtUtil;
import com.powerstrike.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        Attendance stub = new Attendance();
        stub.setAttendanceDate(LocalDateTime.now());
        stub.setNotes("ok");
        when(attendanceService.registerAttendance(any())).thenReturn(stub);
    }

    private String json(Long userId, String notes) {
        String userIdVal = (userId == null) ? "null" : String.valueOf(userId);
        String notesVal  = (notes  == null) ? "null" : "\"" + notes + "\"";
        return "{\"userId\":" + userIdVal + ",\"notes\":" + notesVal + "}";
    }

    private void post200(Long userId, String notes) throws Exception {
        mockMvc.perform(post("/api/attendances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(userId, notes)))
                .andExpect(status().isOk());
    }

    private void post400(Long userId, String notes) throws Exception {
        mockMvc.perform(post("/api/attendances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(userId, notes)))
                .andExpect(status().isBadRequest());
    }

    // ═══════════════════════ userId ═══════════════════════

    @Test
    void AT01_userIdNull_devuelve400() throws Exception {
        post400(null, "ok");
    }

    @Test
    void AT02_userIdNegativo_devuelve400() throws Exception {
        post400(-1L, "ok");
    }

    @Test
    void AT03_userIdCero_devuelve400() throws Exception {
        post400(0L, "ok");
    }

    @Test
    void AT04_userIdValido_devuelve200() throws Exception {
        post200(1L, "ok");
    }

    // ═══════════════════════ notes (opcional) ═══════════════════════

    @Test
    void AT05_notesNull_devuelve200() throws Exception {
        post200(1L, null);
    }

    @Test
    void AT06_notesVacio_devuelve200() throws Exception {
        post200(1L, "");
    }

    // ═══════════════════════ HAPPY-PATH (Bloque D) ═══════════════════════

    @Test
    void getAllAttendances_devuelve200ConLista() throws Exception {
        Attendance stub = new Attendance();
        stub.setAttendanceDate(LocalDateTime.now());
        when(attendanceService.getAllAttendances()).thenReturn(List.of(stub));

        mockMvc.perform(get("/api/attendances"))
                .andExpect(status().isOk());
    }

    @Test
    void getAttendancesByUser_devuelve200ConLista() throws Exception {
        when(attendanceService.getAttendancesByUser(eq(1L))).thenReturn(List.of());

        mockMvc.perform(get("/api/attendances/user/1"))
                .andExpect(status().isOk());
    }
}
