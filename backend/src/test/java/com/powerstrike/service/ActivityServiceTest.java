package com.powerstrike.service;

import com.powerstrike.model.Activity;
import com.powerstrike.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    ActivityRepository activityRepository;

    @InjectMocks
    ActivityService activityService;

    private Activity actividadBoxeo;
    private Activity actividadSpinning;

    @BeforeEach
    void setUp() {
        actividadBoxeo = new Activity(1L, "Boxeo", "Lunes", "09:00 - 10:00", 5000.0, true);
        actividadSpinning = new Activity(2L, "Spinning", "Miércoles", "18:00 - 19:00", 4500.0, true);
    }

    // REQ-F04 — Mostrar actividades disponibles para consulta
    @Test
    void getAllActivities_devuelveListaDeActividades() {
        when(activityRepository.findAll()).thenReturn(Arrays.asList(actividadBoxeo, actividadSpinning));

        List<Activity> resultado = activityService.getAllActivities();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Boxeo", resultado.get(0).getName());
        verify(activityRepository, times(1)).findAll();
    }

    // REQ-F03 — Gestión de actividades: visualizar por id
    @Test
    void getActivityById_cuandoIdExiste_devuelveActividad() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(actividadBoxeo));

        Activity resultado = activityService.getActivityById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Boxeo", resultado.getName());
        verify(activityRepository, times(1)).findById(1L);
    }

    @Test
    void getActivityById_cuandoIdNoExiste_lanzaRuntimeException() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> activityService.getActivityById(99L));

        assertEquals("Actividad no encontrada", ex.getMessage());
        verify(activityRepository, times(1)).findById(99L);
    }

    // REQ-F03 — Gestión de actividades: crear
    @Test
    void createActivity_guardaYDevuelveActividad() {
        when(activityRepository.save(actividadBoxeo)).thenReturn(actividadBoxeo);

        Activity resultado = activityService.createActivity(actividadBoxeo);

        assertNotNull(resultado);
        assertEquals("Boxeo", resultado.getName());
        verify(activityRepository, times(1)).save(actividadBoxeo);
    }

    // REQ-F03 — Gestión de actividades: editar
    @Test
    void updateActivity_cuandoIdExiste_actualizaYDevuelveActividad() {
        Activity datosActualizados = new Activity(null, "Boxeo Avanzado", "Martes", "10:00 - 11:00", 5500.0, true);
        when(activityRepository.findById(1L)).thenReturn(Optional.of(actividadBoxeo));
        when(activityRepository.save(actividadBoxeo)).thenReturn(actividadBoxeo);

        Activity resultado = activityService.updateActivity(1L, datosActualizados);

        assertNotNull(resultado);
        assertEquals("Boxeo Avanzado", resultado.getName());
        assertEquals("Martes", resultado.getDay());
        assertEquals(5500.0, resultado.getMonthlyCost());
        verify(activityRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).save(actividadBoxeo);
    }

    @Test
    void updateActivity_cuandoIdNoExiste_lanzaRuntimeException() {
        Activity datosActualizados = new Activity(null, "Yoga", "Viernes", "08:00 - 09:00", 3000.0, true);
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> activityService.updateActivity(99L, datosActualizados));

        assertEquals("Actividad no encontrada", ex.getMessage());
        verify(activityRepository, times(1)).findById(99L);
        verify(activityRepository, never()).save(any());
    }

    // REQ-F03 — Gestión de actividades: eliminar
    @Test
    void deleteActivity_llamaADeleteById() {
        doNothing().when(activityRepository).deleteById(1L);

        activityService.deleteActivity(1L);

        verify(activityRepository, times(1)).deleteById(1L);
    }
}
