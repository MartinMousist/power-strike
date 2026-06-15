package com.powerstrike.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.powerstrike.validation.OnCreate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "^\\d{7,8}$", message = "El DNI debe contener 7-8 dígitos")
    @Column(nullable = false, unique = true)
    private String dni;

    // La contraseña solo es obligatoria al CREAR (grupo OnCreate). En la edición es opcional:
    // si llega vacía o nula se conserva la actual (DEF-EXP-03). Si llega, debe tener 6+ caracteres.
    @NotBlank(message = "La contraseña no puede estar vacía", groups = OnCreate.class)
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    @Pattern(regexp = "^(ADMIN|TRAINER|CLIENT)$", message = "El rol debe ser ADMIN, TRAINER o CLIENT")
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean active = true;
}
