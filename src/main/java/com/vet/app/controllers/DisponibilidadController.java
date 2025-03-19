package com.vet.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vet.app.dtos.request.IntervaloDto;
import com.vet.app.entities.Disponibilidad;
import com.vet.app.services.disponibilidad.DisponibilidadService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/{veterinarioId}")
    public ResponseEntity<?> intervalosDisponibles(
            @RequestBody IntervaloDto fecha,
            @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(disponibilidadService.obtenerIntervaloDisponibles(veterinarioId, fecha));
    }

    @GetMapping("/rango-fechas/{veterinarioId}")
    public ResponseEntity<?> rangoFechas(
            @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(disponibilidadService.obtenerRangoFechasISO(veterinarioId));
    }

}
