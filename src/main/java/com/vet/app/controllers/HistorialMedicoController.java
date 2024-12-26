package com.vet.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.entities.HistorialMedico;
import com.vet.app.services.historialMedico.HistorialMedicoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/historial-medico")
public class HistorialMedicoController {

    @Autowired
    private HistorialMedicoService historialMedicoService;

    @GetMapping
    public List<HistorialMedico> viewAll() {
        return (List<HistorialMedico>) historialMedicoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        return historialMedicoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{mascotaId}")
    public ResponseEntity<?> createHistorialMedico(@Valid @RequestBody HistorialMedico historialMedico,
            @PathVariable Long mascotaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialMedicoService.save(historialMedico, mascotaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHistorialMedico(@Valid @RequestBody HistorialMedico historialMedico,
            @PathVariable Long id) {
        return historialMedicoService.update(historialMedico, id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistorialMedico(@PathVariable Long id) {
        return historialMedicoService.delete(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
