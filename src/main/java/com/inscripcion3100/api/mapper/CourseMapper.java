package com.inscripcion3100.api.mapper;

import com.inscripcion3100.api.dto.course.CourseRequestDTO;
import com.inscripcion3100.api.dto.course.CourseResponseDTO;
import com.inscripcion3100.api.entity.Course;

public class CourseMapper {
    public static CourseResponseDTO toCourseResponseDTO (Course course){
        if(course == null){
            return null;
        }
        return new CourseResponseDTO(
                course.getIdCourse(),
                course.getYear(),
                course.getStudyYear(),
                course.getDivision(),
                course.getIsAdvancedCycle(),
                course.getShift(),
                course.getSpeciality(),
                course.getAvailablePlaces()
        );
    }

    public static Course toCourseEntity (CourseRequestDTO courseRequestDTO) {
        if(courseRequestDTO == null){
            return null;
        }

        Course course = new Course();

        //course.setAvailablePlaces(courseRequestDTO.get);
        course.setYear(courseRequestDTO.getYear());
        course.setStudyYear(courseRequestDTO.getStudyYear());
        course.setDivision(courseRequestDTO.getDivision());
        course.setIsAdvancedCycle(courseRequestDTO.getAdvancedCycle());
        course.setShift(courseRequestDTO.getShift());
        course.setSpeciality(courseRequestDTO.getSpeciality());
        course.setMaxCapacity(courseRequestDTO.getMaxCapacity());
        course.setAvailablePlaces(course.getMaxCapacity());

        return course;
    }
}