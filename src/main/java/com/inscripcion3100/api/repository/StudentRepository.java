package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.entity.Student;
import com.inscripcion3100.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentDni (Long dni);
    Optional<Student> findByCuil(String cuil);
    Optional<Student> findByStudentEmail(String email);
    List<Student> findByTutor1(User tutor);

    Boolean existsByStudentDni (Long dni);
    Boolean existsByCuil (String cuil);
    Boolean existsByStudentEmail (String email);
}
