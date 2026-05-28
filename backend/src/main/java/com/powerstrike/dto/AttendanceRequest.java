package com.powerstrike.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AttendanceRequest {
    @NotNull(message = "El userId es obligatorio")
    @Positive(message = "El userId debe ser un número positivo")
    private Long userId;
    private String notes;
}
