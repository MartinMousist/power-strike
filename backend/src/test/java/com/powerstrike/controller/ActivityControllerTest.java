package com.powerstrike.controller;

import com.powerstrike.config.GlobalExceptionHandler;
import com.powerstrike.model.Activity;
import com.powerstrike.security.JwtUtil;
import com.powerstrike.service.ActivityService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final String VALID_NAME     = "Spinning";
    private static final String VALID_DAY      = "Lunes";
    private static final String VALID_SCHEDULE = "18:00-19:00";
    private static final Double VALID_COST     = 5000.0;

    @BeforeEach
    void setUp() {
        Activity stub = new Activity(1L, VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST, true);
        when(activityService.createActivity(any())).thenReturn(stub);
    }

    private String json(String name, String day, String schedule, Double monthlyCost, boolean active) {
        String costPart = (monthlyCost == null) ? "null" : String.valueOf(monthlyCost);
        return "{\"name\":\"" + name + "\",\"day\":\"" + day + "\",\"schedule\":\"" + schedule
                + "\",\"monthlyCost\":" + costPart + ",\"active\":" + active + "}";
    }

    private void post200(String name, String day, String schedule, Double cost) throws Exception {
        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(name, day, schedule, cost, true)))
                .andExpect(status().isOk());
    }

    private void post400(String name, String day, String schedule, Double cost) throws Exception {
        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(name, day, schedule, cost, true)))
                .andExpect(status().isBadRequest());
    }

    // ═══════════════════════ monthlyCost ═══════════════════════

    @Test
    void AC01_monthlyCost0_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_DAY, VALID_SCHEDULE, 0.0);
    }

    @Test
    void AC02_monthlyCost001_bordeValido_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_DAY, VALID_SCHEDULE, 0.01);
    }

    @Test
    void AC03_monthlyCostNegativo_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_DAY, VALID_SCHEDULE, -10.0);
    }

    @Test
    void AC04_monthlyCostNull_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_DAY, VALID_SCHEDULE, null);
    }

    // ═══════════════════════ name ═══════════════════════

    @Test
    void AC05_nombre2Chars_devuelve400() throws Exception {
        post400("ab", VALID_DAY, VALID_SCHEDULE, VALID_COST);
    }

    @Test
    void AC06_nombre3Chars_bordeValido_devuelve200() throws Exception {
        post200("abc", VALID_DAY, VALID_SCHEDULE, VALID_COST);
    }

    @Test
    void AC07_nombreVacio_devuelve400() throws Exception {
        post400("", VALID_DAY, VALID_SCHEDULE, VALID_COST);
    }

    // ═══════════════════════ day / schedule ═══════════════════════

    @Test
    void AC08_dayVacio_devuelve400() throws Exception {
        post400(VALID_NAME, "", VALID_SCHEDULE, VALID_COST);
    }

    @Test
    void AC09_scheduleVacio_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_DAY, "", VALID_COST);
    }

    @Test
    void AC10_todosValidos_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST);
    }

    // ═══════════════════════ HAPPY-PATH (Bloque D) ═══════════════════════

    @Test
    void getAllActivities_devuelve200ConLista() throws Exception {
        Activity stub = new Activity(1L, VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST, true);
        when(activityService.getAllActivities()).thenReturn(List.of(stub));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(VALID_NAME));
    }

    @Test
    void getActivityById_devuelve200() throws Exception {
        Activity stub = new Activity(1L, VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST, true);
        when(activityService.getActivityById(1L)).thenReturn(stub);

        mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(VALID_NAME));
    }

    @Test
    void updateActivity_bodyValido_devuelve200() throws Exception {
        Activity stub = new Activity(1L, VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST, true);
        when(activityService.updateActivity(eq(1L), any())).thenReturn(stub);

        mockMvc.perform(put("/api/activities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(VALID_NAME, VALID_DAY, VALID_SCHEDULE, VALID_COST, true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(VALID_NAME));
    }

    @Test
    void deleteActivity_devuelve204() throws Exception {
        doNothing().when(activityService).deleteActivity(1L);

        mockMvc.perform(delete("/api/activities/1"))
                .andExpect(status().isNoContent());
    }
}
