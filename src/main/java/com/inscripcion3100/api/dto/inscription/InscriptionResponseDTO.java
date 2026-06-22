package com.inscripcion3100.api.dto.inscription;

import com.inscripcion3100.api.dto.course.CourseResponseDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.RegistrationStatus;
import com.inscripcion3100.api.entity.StudiesAchieved;
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
public class InscriptionResponseDTO {
    private Long registrationId;
    private Date registrationDate;
    private StudentResponseDTO student;
    private CourseResponseDTO course;
    //quizas solo basta el id del estudiante y el del curso
    private String institutionOfOrigin;
    private String pendingSubjects;
    private String pendingDocumentation;
    private Boolean photoAuthorization;

    /*
     *DATOS DEL SEGUNDO TUTOR
     */
    private String tutorEmail;
    private Long dni;
    private String cuil;
    private String firstName;
    private String lastName;
    private String tutorPhone;
    private String tutorAddress;
    private LocalDate tutor2Birthdate;
    private String ocupation;
    private String relationship;
    private StudiesAchieved studiesAchieved;

    /*
     * DATOS DE SALUD
     */
    private Boolean healthProblem;
    private String healthDetails;
    private Boolean inclusionStudent;
    private String inclusionDetails;
    private String inclusionCertificate;
    private Boolean contactImpediment;
    private String contactImpedimentDetails;

    /*
     * DATOS JUDICIALES
     */
    private Boolean legalProceedings;
    private String legalDetails;
    private String legalCertificate;

    /*
     *  APROBACION DE LA ADMINISTRACION
     */
    private RegistrationStatus status;
}
















