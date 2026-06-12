package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.dto.inscription.InscriptionResponseDTO;
import com.inscripcion3100.api.entity.Course;
import com.inscripcion3100.api.entity.RegistrationApplication;
import com.inscripcion3100.api.entity.RegistrationStatus;
import com.inscripcion3100.api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationApplicationRepository extends JpaRepository<RegistrationApplication, Long> {
    List<RegistrationApplication> findByCourse (Course course);
    List<RegistrationApplication> findByStudent (Student student);
    //List<RegistrationApplication> findByIsApproved (Boolean find);
    //List<RegistrationApplication> findByCourseAndIsApproved(Course course, Boolean isApproved);

    Optional<RegistrationApplication> findFirstByStudentOrderByRegistrationDateDesc(Student student);
    List<RegistrationApplication> findByStatus(RegistrationStatus status);
    List<RegistrationApplication> findByCourseAndStatus(Course course, RegistrationStatus status);
    Long countByCourseAndStatus(Course course, RegistrationStatus status);
    List<RegistrationApplication> findByStudent_StudentDniAndCourse_Year(Long studentDni, Integer year);
    // Cambiar el nombre para traer solo el primero (el más reciente)
    Optional<RegistrationApplication> findFirstByStudent_StudentDniAndCourse_YearOrderByRegistrationDateDesc(Long studentDni, Integer year);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RegistrationApplication r WHERE r.student = :student AND r.course.year = :year")
    Boolean existsByStudentAndCourseYear(@Param("student") Student student, @Param("year") Integer year);

}
