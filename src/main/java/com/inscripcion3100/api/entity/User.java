package com.inscripcion3100.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_app")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_email", unique = true, nullable = false)
    private String userEmail;

    //dni de usuario
    @Column(name = "dni", nullable = false, length = 8)
    private Long dni;

    //cuil del usuario
    @Column(name = "cuil", nullable = false, unique = true, length = 11)
    private String cuil;

    //nombre del usuario
    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    //aperllido del usuario
    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    //contraseña (encriptar)
    @Column(name = "password", nullable = false)
    private String password;

    //numero de telefono
    @Column(name = "user_phone")
    private String userPhone;

    //direccion
    @Column(name = "user_address")
    private String userAddress;

    //fecha de nacimiento
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    //ocupacion
    @Column(name = "ocupation", nullable = false)
    private String ocupation;

    //relacion con el alumno (quizas deberia ser un enum [PADRE, MADRE, HERMAN@, ABUEL@, OTRO]
    @Column(name = "relationship")
    private String relationship;

    /*
     *  campos enumerados
     * ROLES
     * ESTUDIOS ALCANZADOS (nullable)
     */

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "studies_achieved")
    private StudiesAchieved studiesAchieved;

    //AUDITORIAS
    /*
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;
    */

    /*
     *  RELACIONES
     */

    @OneToMany(mappedBy = "tutor1")
    private List<Student> studentsInCharge = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<NotificationForUser> userNotification = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Notification> notificationsSent = new ArrayList<>();
}
