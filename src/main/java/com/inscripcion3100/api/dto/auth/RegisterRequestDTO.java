package com.inscripcion3100.api.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotNull(message = "El dni es obligatorio")
    @Positive(message = "No existen dni negativo")
    private Long dni;

    @NotBlank(message = "Su cuil es obligatorio")
    @Size(min = 11, max = 11, message = "El cuil debe tener 11 digitos")
    private String cuil;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3, max = 120)
    private String lastName;

    @Email(message = "Debe ingresar un email valido")
    @NotBlank(message = "Email obligatorio")
    private String email;   //con @Setter crea un metodo getEmail

    @NotBlank(message = "Contaseña obligatoria")
    @Size(min = 8, max = 16, message = "Ingrese una contraseña que contenga entre 8 y 16 digitos")
    private String password;

    private String userPhone;

    private String userAddress;

    @NotNull(message = "Fecha de nacimiento obligatoria")
    @Past(message = "No puede ingresar una fecha futura")
    private LocalDate dateOfBirth;

    private Set<String> roles;

    private String ocupation;

    private String relationship;
}
