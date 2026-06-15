package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.student.StudentRequestDTO;
import com.inscripcion3100.api.dto.student.StudentResponseDTO;
import com.inscripcion3100.api.service.IStudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<StudentResponseDTO>> getMyStudents (Authentication auth){
        String username = auth.getName();

        List<StudentResponseDTO> students = studentService.getStudentsByTutor(username);

        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> upStudent (
            @Valid @RequestBody StudentRequestDTO requestDTO,
            Authentication auth){
        String username = auth.getName();
        StudentResponseDTO student = studentService.registerStudent(requestDTO, username);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent (
            @Valid @RequestBody StudentRequestDTO requestDTO,
            @PathVariable("studentId") Long studentId,
            Authentication auth){
        String username = auth.getName();
        StudentResponseDTO response = studentService.updateStudent(studentId, requestDTO, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById (
            @PathVariable("studentId") Long studentId,
            Authentication auth){
        String username = auth.getName();
        StudentResponseDTO student = studentService.getStudentById(studentId, username);
        return ResponseEntity.ok(student);
    }
}
