package com.powerstrike.controller;

import com.powerstrike.config.GlobalExceptionHandler;
import com.powerstrike.model.User;
import com.powerstrike.security.JwtUtil;
import com.powerstrike.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final String VALID_NAME     = "Juan Perez";
    private static final String VALID_EMAIL    = "juan@mail.com";
    private static final String VALID_DNI      = "12345678";
    private static final String VALID_PASSWORD = "miClave123";
    private static final String VALID_ROLE     = "ADMIN";

    @BeforeEach
    void setUp() {
        User stub = new User(1L, VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE, true);
        when(userService.createUser(any())).thenReturn(stub);
    }

    private String json(String name, String email, String dni, String password, String role) {
        return "{\"name\":\"" + name + "\",\"email\":\"" + email
                + "\",\"dni\":\"" + dni + "\",\"password\":\"" + password
                + "\",\"role\":\"" + role + "\"}";
    }

    private void post200(String name, String email, String dni, String password, String role) throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(name, email, dni, password, role)))
                .andExpect(status().isOk());
    }

    private void post400(String name, String email, String dni, String password, String role) throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(name, email, dni, password, role)))
                .andExpect(status().isBadRequest());
    }

    // ═══════════════════════ DNI ═══════════════════════

    @Test
    void CP01_dni7Digitos_bordeValido_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_EMAIL, "1234567", VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP02_dni8Digitos_bordeValido_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_EMAIL, "12345678", VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP03_dni6Digitos_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, "123456", VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP04_dni9Digitos_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, "123456789", VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP05_dniNoNumerico_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, "1234ABC", VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP06_dniVacio_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, "", VALID_PASSWORD, VALID_ROLE);
    }

    // ═══════════════════════ PASSWORD ═══════════════════════

    @Test
    void CP07_password5Chars_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, VALID_DNI, "12345", VALID_ROLE);
    }

    @Test
    void CP08_password6Chars_bordeValido_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_EMAIL, VALID_DNI, "123456", VALID_ROLE);
    }

    @Test
    void CP09_passwordValida_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_EMAIL, VALID_DNI, "miClave123", VALID_ROLE);
    }

    @Test
    void CP10_passwordVacia_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, VALID_DNI, "", VALID_ROLE);
    }

    // ═══════════════════════ NAME ═══════════════════════

    @Test
    void CP11_nombre2Chars_devuelve400() throws Exception {
        post400("ab", VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP12_nombre3Chars_bordeValido_devuelve200() throws Exception {
        post200("abc", VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP13_nombre100Chars_bordeValido_devuelve200() throws Exception {
        post200("a".repeat(100), VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP14_nombre101Chars_devuelve400() throws Exception {
        post400("a".repeat(101), VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP15_nombreVacio_devuelve400() throws Exception {
        post400("", VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    // ═══════════════════════ ROLE ═══════════════════════

    @Test
    void CP16_roleAdmin_devuelve200() throws Exception {
        post200(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, "ADMIN");
    }

    @Test
    void CP17_roleInvalido_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, "USER");
    }

    @Test
    void CP18_roleLowercase_devuelve400() throws Exception {
        post400(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, "admin");
    }

    // ═══════════════════════ EMAIL ═══════════════════════

    @Test
    void CP19_emailValido_devuelve200() throws Exception {
        post200(VALID_NAME, "test@mail.com", VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    @Test
    void CP20_emailSinArroba_devuelve400() throws Exception {
        post400(VALID_NAME, "testmail.com", VALID_DNI, VALID_PASSWORD, VALID_ROLE);
    }

    // ═══════════════════════ EXCEPTION HANDLERS ═══════════════════════

    @Test
    void EX01_emailDuplicado_devuelve409() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new DataIntegrityViolationException("duplicate key: email constraint"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE)))
                .andExpect(status().isConflict());
    }

    @Test
    void EX02_dniDuplicado_devuelve409() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new DataIntegrityViolationException("duplicate key: dni constraint"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE)))
                .andExpect(status().isConflict());
    }

    @Test
    void EX03_runtimeException_devuelve500() throws Exception {
        when(userService.createUser(any()))
                .thenThrow(new RuntimeException("error interno inesperado"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(VALID_NAME, VALID_EMAIL, VALID_DNI, VALID_PASSWORD, VALID_ROLE)))
                .andExpect(status().isInternalServerError());
    }
}
