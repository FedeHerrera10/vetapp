package com.vet.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.entities.User;

public interface UserService {

    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);

    ResponseEntity<?> confirmEmail(String confirmationToken);
    
    Optional<User> findById(Long id);

    boolean resetPassword(DtoResetPassword dtoResetPassword);

    boolean newCode(Long idUser);

    boolean existsByEmail(String email);

}
