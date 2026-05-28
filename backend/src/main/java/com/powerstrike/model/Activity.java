package com.powerstrike.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El día no puede estar vacío")
    @Column(nullable = false)
    private String day;

    @NotBlank(message = "El horario no puede estar vacío")
    @Column(nullable = false)
    private String schedule;

    @NotNull(message = "El costo mensual es obligatorio")
    @Positive(message = "El costo mensual debe ser mayor a cero")
    @Column(nullable = false)
    private Double monthlyCost;

    @Column(nullable = false)
    private boolean active = true;
}
