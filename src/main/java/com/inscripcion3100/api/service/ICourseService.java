package com.inscripcion3100.api.service;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.course.CourseRequestDTO;
import com.inscripcion3100.api.dto.course.CourseResponseDTO;

import java.util.List;

public interface ICourseService {
    CourseResponseDTO createCourse(CourseRequestDTO request);

    List<CourseResponseDTO> getAvailableCourses(Integer year);

    CourseResponseDTO getCourseById(Long courseId);

    MessageResponse cloneCoursesForNewYear(Integer oldYear, Integer newYear);
}
