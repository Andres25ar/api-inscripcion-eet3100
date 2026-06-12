package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.student.StudentRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.Student;
import com.inscripcion3100.api.entity.User;
import com.inscripcion3100.api.exception.ResourceNotFoundException;
import com.inscripcion3100.api.mapper.StudentMapper;
import com.inscripcion3100.api.repository.StudentRepository;
import com.inscripcion3100.api.repository.UserRepository;
import com.inscripcion3100.api.service.IStudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements IStudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public StudentResponseDTO registerStudent(StudentRequestDTO request, String tutorEmail) {

        User tutor = userRepository.findByUserEmail(tutorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Student student = new Student();
        student = StudentMapper.toStudentEntity(request);
        student.setTutor1(tutor);

        Student studentSaved = studentRepository.save(student);

        return StudentMapper.toStudentResponseDTO(student);
    }

    @Override
    @Transactional
    public List<StudentResponseDTO> getStudentsByTutor(String tutorEmail) {
        User tutor = userRepository.findByUserEmail(tutorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Student> studentList = studentRepository.findByTutor1(tutor);

        return studentList.stream()
                .map(StudentMapper :: toStudentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDTO getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return StudentMapper.toStudentResponseDTO(student);
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setAddress(requestDTO.getAddress());
        student.setStudentEmail(requestDTO.getStudentEmail());
        student.setStudentPhone(requestDTO.getStudentPhone());

        Student updatedStudent = studentRepository.save(student);
        return StudentMapper.toStudentResponseDTO(updatedStudent);
    }
}
