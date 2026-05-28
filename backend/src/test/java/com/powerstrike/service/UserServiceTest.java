package com.powerstrike.service;

import com.powerstrike.model.User;
import com.powerstrike.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    private User usuarioAdmin;
    private User usuarioCliente;

    @BeforeEach
    void setUp() {
        usuarioAdmin = new User(1L, "Juan Pérez", "juan@email.com", "12345678", "hashedPass", "ADMIN", true);
        usuarioCliente = new User(2L, "Ana López", "ana@email.com", "87654321", "hashedPass2", "CLIENT", true);
    }

    // REQ-F02 — Listado de usuarios registrados
    @Test
    void getAllUsers_devuelveListaDeUsuarios() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(usuarioAdmin, usuarioCliente));

        List<User> resultado = userService.getAllUsers();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_cuandoIdExiste_devuelveUsuario() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));

        User resultado = userService.getUserById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_cuandoIdNoExiste_lanzaRuntimeException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.getUserById(99L));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(userRepository, times(1)).findById(99L);
    }

    // REQ-F01 — Registro de usuarios con nombre, email y DNI
    @Test
    void createUser_encodesPasswordYGuardaUsuario() {
        User nuevoUsuario = new User(null, "Carlos Ruiz", "carlos@email.com", "11223344", "password123", "CLIENT", true);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword123");
        when(userRepository.save(nuevoUsuario)).thenReturn(nuevoUsuario);

        User resultado = userService.createUser(nuevoUsuario);

        assertNotNull(resultado);
        assertEquals("hashedPassword123", resultado.getPassword());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    void updateUser_cuandoIdExisteYHayPasswordNuevo_actualizaTodosLosCampos() {
        User datosActualizados = new User(null, "Juan Actualizado", "juannuevo@email.com", "12345678", "newPass", "TRAINER", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPass");
        when(userRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

        User resultado = userService.updateUser(1L, datosActualizados);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getName());
        assertEquals("juannuevo@email.com", resultado.getEmail());
        assertEquals("TRAINER", resultado.getRole());
        assertEquals("hashedNewPass", resultado.getPassword());
        verify(passwordEncoder, times(1)).encode("newPass");
        verify(userRepository, times(1)).save(usuarioAdmin);
    }

    @Test
    void updateUser_cuandoPasswordEsNulo_noReencodea() {
        User datosActualizados = new User(null, "Juan Actualizado", "juan@email.com", "12345678", null, "ADMIN", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(userRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

        userService.updateUser(1L, datosActualizados);

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, times(1)).save(usuarioAdmin);
    }

    @Test
    void updateUser_cuandoPasswordEsBlank_noReencodea() {
        User datosActualizados = new User(null, "Juan Actualizado", "juan@email.com", "12345678", "", "ADMIN", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(userRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

        userService.updateUser(1L, datosActualizados);

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, times(1)).save(usuarioAdmin);
    }

    @Test
    void updateUser_cuandoIdNoExiste_lanzaRuntimeException() {
        User datosActualizados = new User(null, "X", "x@email.com", "00000000", null, "CLIENT", true);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateUser(99L, datosActualizados));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_llamaADeleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
