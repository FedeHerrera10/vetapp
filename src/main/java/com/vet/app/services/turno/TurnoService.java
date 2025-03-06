package com.vet.app.services.turno;

import java.util.List;
import java.util.Optional;

import com.vet.app.entities.Turnos;

public interface TurnoService {
    List<Turnos> findAll();

    Optional<Turnos> findById(Long id);

    Turnos save(Turnos turno);

    Optional<Turnos> update(Turnos turno, Long id);

    Optional<Turnos> delete(Long id);

    List<Object[]> findTurnosByVeterinarioId(Long veterinarioId);
}
