package com.vet.app.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.app.entities.HistorialMedico;

public interface HistorialMedicoRepository extends CrudRepository<HistorialMedico, Long> {
    @Query("SELECT hm FROM HistorialMedico hm left join fetch  hm.veterinario v left join hm.mascota m WHERE v.id = :veterinarioId")
    Optional<List<HistorialMedico>> findAllByVeterinario(Long veterinarioId);
}
