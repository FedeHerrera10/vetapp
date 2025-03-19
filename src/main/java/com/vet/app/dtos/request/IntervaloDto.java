package com.vet.app.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Data;

@Data
public class IntervaloDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
    private LocalDate fecha;
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private String horaInicio;
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private LocalTime horaFin;

    public IntervaloDto() {
    }

    public IntervaloDto(LocalDate fecha, String horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

}
