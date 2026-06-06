package com.inscripcion3100.api.mapper;

import com.inscripcion3100.api.dto.student.StudentRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.entity.Student;

import java.util.Date;

public class StudentMapper {
    public static StudentResponseDTO toStudentResponseDTO(Student student){
        if (student == null){
            return null;
        }
        return new StudentResponseDTO(
                student.getStudentId(),
                student.getStudentDni(),
                student.getCuil(),
                student.getLastName(),
                student.getFirstName(),
                student.getStudentEmail(),
                student.getStudentPhone(),
                student.getStudentBirthdate(),
                student.getBirthplace(),
                student.getAddress()
        );
    }

    public static Student toStudentEntity (StudentRequestDTO studentRequestDTO){
        if (studentRequestDTO == null){
            return null;
        }

        Student student = new Student();

        student.setStudentDni(studentRequestDTO.getStudentDni());
        student.setCuil(studentRequestDTO.getStudentCuil());
        student.setLastName(studentRequestDTO.getLastName());
        student.setFirstName(studentRequestDTO.getFirstName());
        student.setStudentEmail(studentRequestDTO.getStudentEmail());
        student.setStudentPhone(studentRequestDTO.getStudentPhone());
        student.setStudentBirthdate(studentRequestDTO.getBirthdate());
        student.setBirthplace(studentRequestDTO.getBirthplace());
        student.setAddress(studentRequestDTO.getAddress());

        return student;
    }
}
