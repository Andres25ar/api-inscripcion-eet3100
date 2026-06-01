package com.inscripcion3100.api.dto.inscription;

import com.inscripcion3100.api.entity.StudiesAchieved;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionRequestDTO {
    @NotNull(message = "Debe especificar el curso")
    private Long idCourse;

    @NotNull(message = "Ingrese informacion del alumno a inscribir")
    private Long studentId;

    /*@NotNull(message = "Fecha es necesario")
    private Date registrationDate;*/

    private String institutionOfOrigin;

    //private String pendingSubjects;

    //private String pendingDocumentation;

    @NotNull(message = "Especifique si el establecimiento puede usar la imagen del alumno")
    private Boolean photoAuthorization;

    /*
     *DATOS DEL SEGUNDO TUTOR
     */

    @Email(message = "Ingrese un email valido")
    private String userEmail;

    @Positive(message = "No existe DNI negativo")
    @Min(value = 9999999, message = "Ingrese un DNI valido")
    @Max(value = 100000000, message = "Ingrese un DNI valido")
    private Long dni;

    //cuil del tutor 2
    @Size(max = 11)
    private String cuil;

    @Size(min = 3, max = 120)
    private String firstName;

    @Size(min = 3, max = 120)
    private String lastName;

    private String userPhone;

    private String userAddress;

    @Past(message = "Ingrese una fecha valida")
    private LocalDate dateOfBirth;

    private String ocupation;

    private String relationship;

    private StudiesAchieved studiesAchieved;

    /*
     * DATOS DE SALUD
     */
    @NotNull(message = "Debe especificar se el alumno tiene problemas de salud")
    private Boolean healthProblem;

    private String healthDetails;

    @NotNull(message = "Debe especificar se el alumno es persona de inclusion")
    private Boolean inclusionStudent;

    private String inclusionDetails;

    private String inclusionCertificate;

    @NotNull(message = "Debe especificar se el alumno tiene impedimento de contacto")
    private Boolean contactImpediment;

    private String contactImpedimentDetails;

    /*
     * DATOS JUDICIALES
     */
    @NotNull(message = "Debe especificar se el alumno tiene algun proceso legal en proceso")
    private Boolean legalProceedings;

    private String legalDetails;

    private String legalCertificate;
}
