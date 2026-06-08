package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.entity.Role;
import com.inscripcion3100.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail (String email);
    Optional<User> findByDni (Long dni);
    Optional<User> findByCuil (String cuil);
    List<User> findByRole (Role role);
    Boolean existsByUserEmail (String email);
    Boolean existsByDni (Long dni);
    Boolean existsByCuil (String cuil);
}
