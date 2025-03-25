package com.vet.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticación")
public class AuthController {

    @Autowired
    private UserService service;

    @Operation(summary = "Confirmar cuenta", description = "Confirmar cuenta")
    @PutMapping(value="/confirm-account/{token}")
    public ResponseEntity<?> confirmUserAccount(@PathVariable String token) {
        return service.confirmEmail(token);
    }

    @Operation(summary = "Restablecer contraseña", description = "Restablecer contraseña")
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody DtoResetPassword dtoResetPassword) {
        boolean update = service.resetPassword(dtoResetPassword);
        if(!update) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar la contraseña.");
        return ResponseEntity.status(200).body("Contraseña actualizada");
    }

    @PostMapping("/new-code")
    @Operation(summary = "Generar nuevo código", description = "Generar nuevo código")
    public ResponseEntity<?> generateNewCode(@RequestBody Map<String, String> requestBody) {
        service.newCode(requestBody.get("email"));
        return ResponseEntity.status(200).body("Codigo enviado , revise su mail.");
    }
    
}
