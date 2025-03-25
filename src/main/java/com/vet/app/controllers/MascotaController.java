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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/mascota")
@Tag(name = "Mascota", description = "Gestión de mascotas") 
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Obtener lista de mascotas", description = "Obtener lista de mascotas")
    public List<Mascota> viewAll() {
        return (List<Mascota>) mascotaService.findAll();
    }


    @GetMapping("/list/{clienteId}")
    @Operation(summary = "Obtener lista de mascotas por cliente", description = "Obtener lista de mascotas por cliente")
    public ResponseEntity<?> getMascotasByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.findByClienteId(clienteId));
    }
    

    @GetMapping("/{id}")
    @Operation(summary = "Obtener mascota por ID", description = "Obtener mascota por ID")
    public ResponseEntity<?> view(@PathVariable Long id) {
        return mascotaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{clienteId}")
    @Operation(summary = "Crear mascota", description = "Crear mascota")
    public ResponseEntity<?> createMascota(@Valid @RequestBody Mascota mascota, @PathVariable Long clienteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.save(mascota, clienteId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar mascota", description = "Actualizar mascota")
    public ResponseEntity<?> updateMascota(@Valid @RequestBody DtoMascotaUpdate dtoMascotaUpdate, @PathVariable Long id) {
        mascotaService.update(dtoMascotaUpdate, id);
        return ResponseEntity.status(HttpStatus.OK).body("Datos actualizados correctamente.");
    }

    @PutMapping("/change-status/{id}/{status}")
    @Operation(summary = "Cambiar estado de mascota", description = "Cambiar estado de mascota")
    public ResponseEntity<?> changeStatus(@PathVariable String status, @PathVariable Long id) {
        mascotaService.changeStatus(status, id);
        return ResponseEntity.status(HttpStatus.OK).body("Datos actualizados correctamente.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mascota", description = "Eliminar mascota")
    public ResponseEntity<?> deleteMascota(@PathVariable Long id) {
        return mascotaService.delete(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
