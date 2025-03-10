package com.vet.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vet.app.entities.Turnos;

public interface TurnosRepository extends CrudRepository<Turnos, Long> {

    @Query("SELECT t.mascota, t.veterinario, t.servicio FROM Turnos t WHERE t.mascota.id = ?1 AND t.veterinario.id = ?2 AND t.servicio.id = ?3")
    List<Object[]> findMascotaVeterinarioServicio(Long mascotaId, Long veterinarioId, Long servicioId);

}
