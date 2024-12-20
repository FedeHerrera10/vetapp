package com.vet.app.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vet.app.entities.User;

public interface UserService {
    
    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);

    ResponseEntity<?> confirmEmail(String confirmationToken);
}
