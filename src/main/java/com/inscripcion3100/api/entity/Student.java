package com.inscripcion3100.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_dni", nullable = false)
    private Long studentDni;

    //cuil del alumno
    @Column(name = "cuil", nullable = false, unique = true, length = 11)
    private String cuil;

    //nombre del alumno
    @Column(name = "student_first_name", nullable = false, length = 120)
    private String firstName;

    //apellido del alumno
    @Column(name = "student_last_name", nullable = false, length = 120)
    private String lastName;

    //numero de telefono
    @Column(name = "student_phone")
    private String studentPhone;

    @Column(name = "student_email")
    private String studentEmail;

    //fecha de nacimiento del alumno
    @Column(name = "student_birthdate", nullable = false)
    private Date studentBirthdate;

    //lugar de nacimiento del alumno
    @Column(name = "student_birthplace", nullable = false)
    private String birthplace;

    //direccion del alumno
    @Column(name = "student_address", nullable = false)
    private String address;

    /*
     *  RELACIONES
     */
}
