package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.vet.app.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
