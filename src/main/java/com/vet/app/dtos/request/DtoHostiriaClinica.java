package com.vet.app.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class DtoHostiriaClinica {
    private Long id;
    private String notas;
    private String tratamientos;
    private String recetasMedicas;
    private LocalDate fecha;
    private LocalTime horario;
    private DtoMascotas mascota;

}
