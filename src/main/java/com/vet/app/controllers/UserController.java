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
import com.vet.app.dtos.request.DtoAbout;
import com.vet.app.dtos.request.DtoUserUpdate;
import com.vet.app.entities.User;
import com.vet.app.services.UserService;
import com.vet.app.services.aboutVet.AboutVeterinarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Gestión de usuarios")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private MapperUtil mapper;

    @Autowired
    private AboutVeterinarioService aboutService;

    @GetMapping("/list")
    @Operation(summary = "Obtener lista de usuarios", description = "Obtener lista de usuarios")
    public List<DtoUser> getAllUsers() {
        return (List<DtoUser>) service.findAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtener usuario por ID")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = service.findById(userId).get();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.mapEntityToDto(user, DtoUser.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear usuario ", description = "Crear usuario solo rol admin")
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        if(service.existsByUsername(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe en el sistema.");
        if(service.existsByEmail(user.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya existe en el sistema.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registrar usuario")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        return create(user);
    }

   
    @GetMapping("/")
    @Operation(summary = "Obtener usuario loggeado", description = "Obtener usuario loggeado")
    public ResponseEntity<?> getUser() {
        return service.getUser();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualizar usuario")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody DtoUserUpdate userUpdate) {
        service.updateUser(id, userUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario actualizado");
    }

    @PostMapping("/about/{id}")
    @Operation(summary = "Agregar perfil", description = "Agregar perfil")
    public ResponseEntity<?> addAbout(@PathVariable Long id, @Valid @RequestBody DtoAbout about) {
        aboutService.save(id, about);
        return ResponseEntity.status(HttpStatus.OK).body("Perfil actualizado");
    }

    @GetMapping("/about/{id}")
    @Operation(summary = "Obtener perfil", description = "Obtener perfil")
    public ResponseEntity<?> getAbout(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(aboutService.findByUserId(id));
    }

    @PutMapping("/about/{id}")
    @Operation(summary = "Actualizar perfil", description = "Actualizar perfil")
    public ResponseEntity<?> updateAbout(@PathVariable Long id, @Valid @RequestBody DtoAbout about) {
        aboutService.update(id, about);
        return ResponseEntity.status(HttpStatus.OK).body("Perfil actualizado");
    }
    
    
}
