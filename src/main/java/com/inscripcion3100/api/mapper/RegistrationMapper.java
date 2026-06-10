package com.inscripcion3100.api.mapper;

import com.inscripcion3100.api.dto.course.CourseResponseDTO;
import com.inscripcion3100.api.dto.inscription.InscriptionRequestDTO;
import com.inscripcion3100.api.dto.inscription.InscriptionResponseDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.Course;
import com.inscripcion3100.api.entity.RegistrationApplication;
import com.inscripcion3100.api.entity.RegistrationStatus;
import com.inscripcion3100.api.entity.Student;

import java.util.Date;

public class RegistrationMapper {
    public static InscriptionResponseDTO toInscriptionResponseDTO (RegistrationApplication registration){
        if(registration == null){
            return null;
        }

        StudentResponseDTO student = StudentMapper.toStudentResponseDTO(registration.getStudent());

        CourseResponseDTO course = CourseMapper.toCourseResponseDTO(registration.getCourse());

        return new InscriptionResponseDTO(
                registration.getRegistrationId(),
                registration.getRegistrationDate(),
                student,
                course,
                registration.getInstitutionOfOrigin(),
                registration.getPendingSubjects(),
                registration.getPendingDocumentation(),
                registration.getPhotoAuthorization(),
                //datos del tutor2
                registration.getTutor2Email(),
                registration.getTutor2Dni(),
                registration.getTutor2Cuil(),
                registration.getTutor2FirstName(),
                registration.getTutor2LastName(),
                registration.getTutor2Phone(),
                registration.getTutor2Address(),
                registration.getTutor2Birthdate(),
                registration.getTutor2Ocupation(),
                registration.getRelationship(),
                registration.getTutor2StudiesAchieved(),
                //datos de saludo
                registration.getHealthProblem(),
                registration.getHealthDetails(),
                registration.getInclusionStudent(),
                registration.getInclusionDetails(),
                registration.getInclusionCertificate(),
                registration.getContactImpediment(),
                registration.getContactImpedimentDetails(),
                //datos judiciales
                registration.getLegalProceedings(),
                registration.getLegalDetails(),
                registration.getLegalCertificate(),
                //aprobacion de administracion
                registration.getStatus()
        );
    }

    public static RegistrationApplication toRegistrationApplication (
            InscriptionRequestDTO inscriptionDTO,
            Course course,
            Student student
    ){
        if (inscriptionDTO == null){
            return null;
        }

        RegistrationApplication registrationApplication = new RegistrationApplication();

        registrationApplication.setCourse(course);
        registrationApplication.setStudent(student);
        registrationApplication.setStatus(RegistrationStatus.PENDING);
        registrationApplication.setRegistrationDate(new Date());
        registrationApplication.setInstitutionOfOrigin(inscriptionDTO.getInstitutionOfOrigin());
        registrationApplication.setPhotoAuthorization(inscriptionDTO.getPhotoAuthorization());
        //datos del tutor 2
        registrationApplication.setTutor2Email(inscriptionDTO.getTutor2Email());
        registrationApplication.setTutor2Dni(inscriptionDTO.getTutor2Dni());
        registrationApplication.setTutor2Cuil(inscriptionDTO.getTutor2Cuil());
        registrationApplication.setTutor2FirstName(inscriptionDTO.getTutor2FirstName());
        registrationApplication.setTutor2LastName(inscriptionDTO.getTutor2LastName());
        registrationApplication.setTutor2Phone(inscriptionDTO.getTutor2Phone());
        registrationApplication.setTutor2Address(inscriptionDTO.getTutor2Address());
        registrationApplication.setTutor2Birthdate(inscriptionDTO.getTutor2Birthdate());
        registrationApplication.setTutor2Ocupation(inscriptionDTO.getTutor2Ocupation());
        registrationApplication.setRelationship(inscriptionDTO.getRelationship());
        registrationApplication.setTutor2StudiesAchieved(inscriptionDTO.getTutor2StudiesAchieved());
        //datos de salud
        registrationApplication.setHealthProblem(inscriptionDTO.getHealthProblem());
        registrationApplication.setHealthDetails(inscriptionDTO.getHealthDetails());
        registrationApplication.setInclusionStudent(inscriptionDTO.getInclusionStudent());
        registrationApplication.setInclusionDetails(inscriptionDTO.getInclusionDetails());
        registrationApplication.setInclusionCertificate(inscriptionDTO.getInclusionCertificate());
        registrationApplication.setContactImpediment(inscriptionDTO.getContactImpediment());
        registrationApplication.setContactImpedimentDetails(inscriptionDTO.getContactImpedimentDetails());
        //datos judiciales
        registrationApplication.setLegalProceedings(inscriptionDTO.getLegalProceedings());
        registrationApplication.setLegalDetails(inscriptionDTO.getLegalDetails());
        registrationApplication.setLegalCertificate(inscriptionDTO.getLegalCertificate());

        return registrationApplication;
    }
}
