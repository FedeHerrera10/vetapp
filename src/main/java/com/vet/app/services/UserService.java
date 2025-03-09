package com.vet.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.vet.app.dtos.DtoUser;
import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.dtos.request.DtoUserUpdate;
import com.vet.app.entities.User;

public interface UserService {

    List<DtoUser> findAll();

    DtoUser save(User user);

    boolean existsByUsername(String username);

    ResponseEntity<?> confirmEmail(String confirmationToken);
    
    Optional<User> findById(Long id);

    boolean resetPassword(DtoResetPassword dtoResetPassword);

    boolean newCode(String email);

    boolean existsByEmail(String email);

    ResponseEntity<?> getUser();

    boolean updateUser(Long id, DtoUserUpdate userUpdate);
}
