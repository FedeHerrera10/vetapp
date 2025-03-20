package com.vet.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.DtoUser;
import com.vet.app.dtos.MapperUtil;
import com.vet.app.dtos.request.DtoUserUpdate;
import com.vet.app.entities.User;
import com.vet.app.services.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private MapperUtil mapper;

    @GetMapping("/list")
    public List<DtoUser> getAllUsers() {
        return (List<DtoUser>) service.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = service.findById(userId).get();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.mapEntityToDto(user, DtoUser.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        if(service.existsByUsername(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe en el sistema.");
        if(service.existsByEmail(user.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya existe en el sistema.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        return create(user);
    }

   
    @GetMapping("/")
    public ResponseEntity<?> getUser() {
        return service.getUser();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody DtoUserUpdate userUpdate) {
        service.updateUser(id, userUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado");
    }
    
}
