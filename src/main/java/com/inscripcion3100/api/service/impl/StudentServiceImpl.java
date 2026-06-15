package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.admin.StaffResponseDTO;
import com.inscripcion3100.api.dto.student.StudentRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.Role;
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
    public StudentResponseDTO getStudentById(Long studentId, String userEmail) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        if (!student.getTutor1().getUserId().equals(user.getUserId())
                && user.getRole()!=Role.SECRETARIO
                && user.getRole()!=Role.ADMINISTRADOR){
            throw new IllegalArgumentException("Violación de seguridad: No puede acceder a los datos de este alumno");
        }

        return StudentMapper.toStudentResponseDTO(student);
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long studentId, StudentRequestDTO requestDTO, String userEmail) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        User tutor = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!student.getTutor1().getUserId().equals(tutor.getUserId())) {
            throw new IllegalArgumentException("Violación de seguridad: No puede modificar un alumno que no está a su cargo.");
        }

        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setAddress(requestDTO.getAddress());
        student.setStudentEmail(requestDTO.getStudentEmail());
        student.setStudentPhone(requestDTO.getStudentPhone());

        Student updatedStudent = studentRepository.save(student);
        return StudentMapper.toStudentResponseDTO(updatedStudent);
    }
}
