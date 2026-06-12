package com.inscripcion3100.api.service.impl;

import com.inscripcion3100.api.dto.MessageResponse;
import com.inscripcion3100.api.dto.course.CourseRequestDTO;
import com.inscripcion3100.api.dto.course.CourseResponseDTO;
import com.inscripcion3100.api.entity.Course;
import com.inscripcion3100.api.entity.RegistrationStatus;
import com.inscripcion3100.api.exception.ResourceNotFoundException;
import com.inscripcion3100.api.mapper.CourseMapper;
import com.inscripcion3100.api.repository.CourseRepository;
import com.inscripcion3100.api.repository.RegistrationApplicationRepository;
import com.inscripcion3100.api.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final RegistrationApplicationRepository registrationRepository;

    public CourseServiceImpl(CourseRepository courseRepository, RegistrationApplicationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
    }

    @Override
    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        /*
         * FALTA UN METODO PARA VERIFICAR QUE NO SE CARGUE UN CURSO REPETIDO
         * EN LOS REPOSITORIES
         */

        Course course = new Course();

        course.setYear(request.getYear());
        course.setStudyYear(request.getStudyYear());
        course.setDivision(request.getDivision());
        course.setIsAdvancedCycle(request.getAdvancedCycle());
        course.setShift(request.getShift());
        course.setSpeciality(request.getSpeciality());
        course.setMaxCapacity(request.getMaxCapacity());
        course.setAvailablePlaces(course.getMaxCapacity());

        Course savedCourse = courseRepository.save(course);

        return CourseMapper.toCourseResponseDTO(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAvailableCourses(Integer year) {
        // Traes todos los cursos (incluso si tienen 0 cupos, para que el padre vea que existe pero decida si anotarse a la lista de espera)
        List<Course> courses = courseRepository.findByYear(year);

        return courses.stream().map(course -> {
            CourseResponseDTO dto = CourseMapper.toCourseResponseDTO(course);
            // Calculamos cuántos están en lista de espera
            Long pending = registrationRepository.countByCourseAndStatus(course, RegistrationStatus.PENDING);
            dto.setPendingRequests(pending);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));

        return CourseMapper.toCourseResponseDTO(course);
    }

    @Override
    @Transactional
    public MessageResponse cloneCoursesForNewYear(Integer oldYear, Integer newYear) {
        List<Course> oldCourses = courseRepository.findByYear(oldYear);

        if (oldCourses.isEmpty()) {
            throw new RuntimeException("No hay cursos en el año de origen.");
        }

        List<Course> newCourses = new ArrayList<>();
        for (Course old : oldCourses) {
            Course newCourse = new Course();
            newCourse.setYear(newYear);
            newCourse.setStudyYear(old.getStudyYear());
            newCourse.setDivision(old.getDivision());
            newCourse.setIsAdvancedCycle(old.getIsAdvancedCycle());
            newCourse.setShift(old.getShift());
            newCourse.setSpeciality(old.getSpeciality());
            newCourse.setMaxCapacity(old.getMaxCapacity());
            newCourse.setAvailablePlaces(old.getMaxCapacity());

            newCourses.add(newCourse);
        }

        courseRepository.saveAll(newCourses);
        return new MessageResponse("Se clonaron " + newCourses.size() + " cursos para el año " + newYear);
    }
}
