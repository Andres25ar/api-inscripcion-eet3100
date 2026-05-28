package com.inscripcion3100.api.dto.course;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {
    //año, ciclo, división, turno, especialidad, cupo máximo
    @NotNull(message = "Debe ingresar año electivo")
    private Integer year;

    @NotNull(message = "Ingresar el año del curso")
    private Integer studyYear;

    @NotNull(message = "Debe ingresar division del curso")
    private Integer division;

    //true si es ciclo superior o false si es ciclo basico
    @NotNull(message = "Especifique si es ciclo basico o superior")
    private Boolean advancedCycle;

    @NotNull(message = "Es necesario conocer la capadidad maxima de alumnos por curso")
    private Integer maxCapacity;

    @NotNull(message = "Ingrese un turno del curso: MAÑANA, TARDE, VESPERTINO")
    private String shift;

    private String speciality;
}
