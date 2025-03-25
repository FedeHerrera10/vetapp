package com.vet.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.DtoImageRequest;
import com.vet.app.services.imageProfile.ImageProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/images")
@Tag(name = "ImageProfile", description = "Gestión de imágenes de perfil")
public class ImageProfileController {

    @Autowired
    private ImageProfileService imageProfileService;


    @PostMapping("/upload")
    @Operation(summary = "Subir imagen de perfil", description = "Subir imagen de perfil")
    public ResponseEntity<?> uploadImage(@RequestBody DtoImageRequest imageRequest) {
        imageProfileService.saveImage(imageRequest.getUserId(), imageRequest.getImageBase64());
        return ResponseEntity.ok("Imagen guardada exitosamente");
    }
}