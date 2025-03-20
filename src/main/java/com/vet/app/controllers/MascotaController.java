package com.vet.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.DtoMascotaUpdate;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<Mascota> viewAll() {
        return (List<Mascota>) mascotaService.findAll();
    }


    @GetMapping("/list/{clienteId}")
    public ResponseEntity<?> getMascotasByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.findByClienteId(clienteId));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        return mascotaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping("/{clienteId}")
    public ResponseEntity<?> createMascota(@Valid @RequestBody Mascota mascota, @PathVariable Long clienteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.save(mascota, clienteId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMascota(@Valid @RequestBody DtoMascotaUpdate dtoMascotaUpdate, @PathVariable Long id) {
        mascotaService.update(dtoMascotaUpdate, id);
        return ResponseEntity.status(HttpStatus.OK).body("Datos actualizados correctamente.");
    }

    @PutMapping("/change-status/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable String status, @PathVariable Long id) {
        mascotaService.changeStatus(status, id);
        return ResponseEntity.status(HttpStatus.OK).body("Datos actualizados correctamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMascota(@PathVariable Long id) {
        return mascotaService.delete(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
