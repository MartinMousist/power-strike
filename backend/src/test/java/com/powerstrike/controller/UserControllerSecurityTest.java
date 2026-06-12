package com.powerstrike.controller;

import com.powerstrike.config.GlobalExceptionHandler;
import com.powerstrike.config.MethodSecurityConfig;
import com.powerstrike.model.User;
import com.powerstrike.security.JwtUtil;
import com.powerstrike.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RBAC (DEF-EXP-04): solo ADMIN puede crear usuarios; un CLIENT debe recibir 403.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, MethodSecurityConfig.class})
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private PasswordEncoder passwordEncoder;

    private static final String VALID_USER =
        "{\"name\":\"Juan Perez\",\"email\":\"juan@mail.com\",\"dni\":\"12345678\","
        + "\"password\":\"secret1\",\"role\":\"CLIENT\"}";

    @Test
    @WithMockUser(roles = "CLIENT")
    void crearUsuario_comoClient_devuelve403() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_USER))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearUsuario_comoAdmin_permitido() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(new User(1L, "Juan Perez", "juan@mail.com", "12345678", "hash", "CLIENT", true));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_USER))
                .andExpect(status().isOk());
    }
}
