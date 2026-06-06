package com.inscripcion3100.api.repository;

import com.inscripcion3100.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);
    Optional<User> findByDni (Long dni);
    Optional<User> findByCuil (String cuil);

    Boolean existsByEmail (String email);
    Boolean existsByDni (Long dni);
    Boolean existsByCuil (String cuil);
}
