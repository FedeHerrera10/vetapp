package com.vet.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.entities.Disponibilidad;
import com.vet.app.services.disponibilidad.DisponibilidadService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @GetMapping
    public List<Disponibilidad> viewAll() {
        return disponibilidadService.findAll();
    }

    @GetMapping("/{veterinarioId}")
    public ResponseEntity<?> intervalosDisponibles(
            @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(disponibilidadService.obtenerIntervaloDisponibles(veterinarioId, fecha));
    }

}
