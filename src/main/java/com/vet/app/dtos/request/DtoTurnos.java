package com.vet.app.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.vet.app.utils.EstadosEnum;

import lombok.Data;

@Data
public class DtoTurnos {
    private Long id;
    private Long mascotaId;
    private String mascotaNombre;
    private String servicioNombre;
    private String veterinarioNombre;
    private LocalDate fecha;
    private LocalTime horario;
    private EstadosEnum estado;

    public DtoTurnos(Long id, Long mascotaId, String mascotaNombre, String servicioNombre, String veterinarioNombre,
            LocalDate fecha, LocalTime horario, EstadosEnum estado) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.mascotaNombre = mascotaNombre;
        this.servicioNombre = servicioNombre;
        this.veterinarioNombre = veterinarioNombre;
        this.fecha = fecha;
        this.horario = horario;
        this.estado = estado;
    }
}
