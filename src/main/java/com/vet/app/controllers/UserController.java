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

import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.entities.User;
import com.vet.app.services.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("")
    public List<User> getAllUsers() {
        return (List<User>) service.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        return service.findById(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
        user.setAdmin(false);
        return create(user);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody String entity) {
        return entity;
    }
    
}
