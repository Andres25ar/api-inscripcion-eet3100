package com.inscripcion3100.api.controller;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.course.CourseRequestDTO;
import com.inscripcion3100.api.dto.course.CourseResponseDTO;
import com.inscripcion3100.api.service.ICourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<CourseResponseDTO> createCourse (
            @Valid @RequestBody CourseRequestDTO requestDTO){
        CourseResponseDTO course = courseService.createCourse(requestDTO);
        return new ResponseEntity<>(course,HttpStatus.CREATED);
    }

    @PostMapping("/clone/{year}")
    @PreAuthorize("hasAnyAuthority('SECRETARIO', 'ADMINISTRADOR')")
    public ResponseEntity<MessageResponse> cloneCourses (
            @PathVariable("year") Integer year){
        Date newYear = new Date();
        MessageResponse response = courseService.cloneCoursesForNewYear(year, newYear.getYear());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseResponseDTO>> getAvailableCourse(
            @RequestParam("year") Integer year){
        List<CourseResponseDTO> courses = courseService.getAvailableCourses(year);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourseById (
            @PathVariable("courseId") Long courseId){
        CourseResponseDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }
}
