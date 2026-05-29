package com.inscripcion3100.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "registration_application")
public class RegistrationApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @Column(name = "date", nullable = false)
    private Date registrationDate;

    @Column(name = "institution_origin")
    private String institutionOfOrigin;

    //materia adeudadas
    @Column(name = "pending_subjects")
    private String pendingSubjects;

    //documentacion adeudada
    @Column(name = "pending_documentation")
    private String pendingDocumentation;

    //autorizacion para usar la imagen del estudiante en publicaciones de la institucion
    @Column(name = "photo_authorization", nullable = false)
    private Boolean photoAuthorization;

    /*
     *DATOS DEL SEGUNDO TUTOR
     */
    @Column(name = "tutor2_email")
    private String userEmail;

    //dni de tutor 2
    @Column(name = "tutor2_dni", length = 8)
    private Long dni;

    //cuil del tutor 2
    @Column(name = "tutor2_cuil", length = 11)
    private String cuil;

    //nombre del tutor 2
    @Column(name = "tutor2_first_name", length = 120)
    private String firstName;

    //aperllido del tutor 2
    @Column(name = "tutor2_last_name", length = 120)
    private String lastName;

    //numero de telefono
    @Column(name = "tutor2_phone")
    private String userPhone;

    //direccion
    @Column(name = "tutor2_address")
    private String userAddress;

    //fecha de nacimiento
    @Column(name = "tutor2_birthdate")
    private LocalDate dateOfBirth;

    //ocupacion
    @Column(name = "tutor2_ocupation")
    private String ocupation;

    @Column(name = "tutor2_relationship")
    private String relationship;

    @Enumerated(EnumType.STRING)
    @Column(name = "tutor2_studies_achieved")
    private StudiesAchieved studiesAchieved;

    /*
     * DATOS DE SALUD
     */
    @Column(name = "health_problem", nullable = false)
    private Boolean healthProblem;

    @Column(name = "health_details")
    private String healthDetails;

    @Column(name = "inclusion_student", nullable = false)
    private Boolean inclusionStudent;

    @Column(name = "inclusion_details")
    private String inclusionDetails;

    //ruta al directorio donde esta la imagen del certificado
    @Column(name = "inclusion_certificate")
    private String inclusionCertificate;

    @Column(name = "contact_impediment", nullable = false)
    private Boolean contactImpediment;

    @Column(name = "contact_impediment_details")
    private String contactImpedimentDetails;

    /*
     * DATOS JUDICIALES
     */
    @Column(name = "legal_proceedings", nullable = false)
    private Boolean legalProceedings;

    @Column(name = "legal_details")
    private String legalDetails;

    //ruta al directorio donde esta la imagen del certificado
    @Column(name = "legal_certificate")
    private String legalCertificate;

    /*
     *  APROBACION DE LA ADMINISTRACION
     */
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    /*
     *  RELACIONES
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_student")
    private Student student;
}
