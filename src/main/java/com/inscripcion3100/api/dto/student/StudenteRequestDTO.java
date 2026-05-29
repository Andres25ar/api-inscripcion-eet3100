package com.inscripcion3100.api.dto.student;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudenteRequestDTO {
    @NotNull(message = "Es necesario ingresar un DNI")
    @Positive(message = "No existe DNI negativo")
    @Min(value = 9999999, message = "Ingrese un DNI valido")
    @Max(value = 100000000, message = "Ingrese un DNI valido")
    private Long studentDni;

    @NotNull(message = "Debe ingresar un numero de CUIL")
    private String studentCuil;

    @NotNull(message = "Ingrese un apellido")
    @Size(min = 3, max = 120)
    private String lastName;

    @NotNull(message = "Ingrese un nombre")
    @Size(min = 3, max = 120)
    private String firstName;

    @Email(message = "Ingrese un email valido")
    private String studentEmail;

    @Size(min = 6, max = 13)
    private  String studentPhone;

    @NotNull(message = "Ingrese fecha de nacimiento del alumno")
    @Past
    private Date birthdate;

    @NotNull(message = "Ingrese lugar de nacimiento del alumno")
    private String birthplace;

    @NotNull(message = "Ingrese un domicilio")
    private String address;
}
