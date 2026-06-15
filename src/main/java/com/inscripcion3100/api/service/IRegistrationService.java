package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.inscription.InscriptionRequestDTO;
import com.inscripcion3100.api.dto.inscription.InscriptionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRegistrationService {
    InscriptionResponseDTO createRegistration(InscriptionRequestDTO request, String tutorEmail);

    void approveRegistration(Long registrationId);

    void proposeReassignment(Long registrationId, Long newCourseId);

    void rejectRegistration(Long registrationId);

    void replyToReassignment(Long registrationId, boolean accepted, String userEmail);

    List<InscriptionResponseDTO> getPendingRegistrations();

    List<InscriptionResponseDTO> getRegistrationsByCourse(Long courseId);

    List<InscriptionResponseDTO> getMyRegistrations(String tutorEmail);

    InscriptionResponseDTO getRegistrationByStudentDniAndYear(Long studentDni, Integer year);

    InscriptionResponseDTO getLatestRegistrationByStudent(Long studentId, String userEmail);
}
