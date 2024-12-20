package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.app.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User findByEmailIgnoreCase(String email);

    Boolean existsByEmail(String email);
}
