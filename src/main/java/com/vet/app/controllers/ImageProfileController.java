package com.vet.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.DtoImageRequest;
import com.vet.app.entities.ImageProfile;
import com.vet.app.repositories.ImageProfileRepository;
import com.vet.app.services.imageProfile.ImageProfileService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/images")
public class ImageProfileController {

    @Autowired
    private ImageProfileService imageProfileService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody DtoImageRequest imageRequest) {
        imageProfileService.saveImage(imageRequest.getUserId(), imageRequest.getImageBase64());
        return ResponseEntity.ok("Imagen guardada exitosamente");
    }
}