package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.student.StudentRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;

import java.util.List;

public interface IStudentService {
    StudentResponseDTO registerStudent(StudentRequestDTO request, String tutorEmail);

    List<StudentResponseDTO> getStudentsByTutor(String tutorEmail);

    StudentResponseDTO getStudentById(Long studentId, String userEmail);

    StudentResponseDTO updateStudent (Long studentId, StudentRequestDTO requestDTO, String userEmail);
}
