package com.vet.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @GetMapping(value="/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return service.confirmEmail(confirmationToken);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody DtoResetPassword dtoResetPassword) {
        boolean update = service.resetPassword(dtoResetPassword);
        if(!update) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar la contraseña.");
        return ResponseEntity.status(200).body("Contraseña actualizada");
    }

    @PostMapping("/new-code")
    public ResponseEntity<?> generateNewCode(@RequestBody Long id) {
        service.newCode(id);
        return ResponseEntity.status(200).body("Codigo enviado , revise su mail.");
    }
    
}
