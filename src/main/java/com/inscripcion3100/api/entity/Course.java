package com.inscripcion3100.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_course")
    private Long idCourse;

    //anio de cursada - 2025, 2026, 2027, etc
    @Column(name = "year", nullable = false)
    private Integer year;

    //true si es ciclo superior, falso si es ciclo basico
    @Column(name = "advanced_cycle")
    private Boolean isAdvancedCycle;

    //anio de estudio - primero, segundo, tercero, cuarto, etc.
    //puede ser un enum
    @Column(name = "study_year", nullable = false)
    private Integer studyYear;

    //division del curso, (puede ser un enum) - 1⁰3⁰, 2⁰2⁰, 4⁰5⁰, etc
    @Column(name = "division", nullable = false)
    private Integer division;

    //turno del curso - MAÑANA, TARDE, VESPERTINO  (puede ser un enum)
    @Column(name = "shift", nullable = false)
    private String shift;

    //especialidad - ELECTRONICA - INFORMATICA
    @Column(name = "speciality", nullable = false)
    private String speciality;

    //lugares disponibles (si se puede calcular, vuela)
    @Column(name = "available_places", nullable = false)
    private Integer availablePlaces;

    //capacidad maxima permitida por curso
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    /*
     *  AUDITORIAS
     */

    /*
     *  RELACIONES
     */
}
