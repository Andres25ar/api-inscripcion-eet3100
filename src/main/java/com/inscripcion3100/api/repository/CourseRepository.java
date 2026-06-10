package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c " +
            "WHERE c.studyYear = :sYear " +
            "AND c.division = :div")
    List<Course> findByStudyYearAndDivision (
            @Param("sYear") Integer studyYear,
            @Param("div") Integer division);

    List<Course> findByYear (Integer year);
    List<Course> findByStudyYear (Integer studyYear);
    List<Course> findBySpeciality (String speciality);
    List<Course> findByShift (String shift);
    
    //cursos de un año lectivo específico que tengan un minimo de minPlaces asientos disp
    List<Course> findByYearAndAvailablePlacesGreaterThan(Integer year, Integer minPlaces);

    //recibe true si busca los cursos de ciclo superior y false si busca los cursos de ciclo basico
    List<Course> findByIsAdvancedCycle (Boolean find);
}
