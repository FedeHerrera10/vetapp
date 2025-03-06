package com.vet.app.services.disponibilidad;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.vet.app.dtos.request.IntervaloDto;
import com.vet.app.entities.Disponibilidad;

public interface DisponibilidadService {

    List<Disponibilidad> findAll();

    Optional<Disponibilidad> findById(Long id);

    Disponibilidad save(Disponibilidad turno);

    Optional<Disponibilidad> update(Disponibilidad turno, Long id);

    Optional<Disponibilidad> delete(Long id);

    List<IntervaloDto> obtenerIntervaloDisponibles(Long veterinarioId, LocalDate fecha);

}
