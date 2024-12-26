package com.vet.app.services.historialMedico;

import java.util.List;
import java.util.Optional;

import com.vet.app.entities.HistorialMedico;

public interface HistorialMedicoService {

    List<HistorialMedico> findAll();

    Optional<HistorialMedico> findById(Long id);

    HistorialMedico save(HistorialMedico historialMedico, Long mascotaId);

    Optional<HistorialMedico> update(HistorialMedico historialMedico, Long id);

    Optional<HistorialMedico> delete(Long id);
}
