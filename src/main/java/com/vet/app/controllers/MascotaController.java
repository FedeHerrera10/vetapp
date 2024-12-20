package com.vet.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.entities.Mascota;
import com.vet.app.services.mascota.MascotaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping
    public List<Mascota> viewAll() {
        return (List<Mascota>) mascotaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        return mascotaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMascota(@Valid @RequestBody Mascota mascota, @PathVariable Long clienteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.save(mascota, clienteId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMascota(@Valid @RequestBody Mascota mascota, @PathVariable Long id) {
        return mascotaService.update(mascota, id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMascota(@PathVariable Long id) {
        return mascotaService.delete(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
