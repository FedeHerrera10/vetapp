package com.vet.app.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vet.app.entities.Disponibilidad;
import com.vet.app.entities.User;

public interface DisponibilidadRepository extends CrudRepository<Disponibilidad, Long> {

    @Query("SELECT d FROM Disponibilidad d WHERE d.veterinarioId.id = ?1 AND d.fecha = ?2")
    Optional<List<Disponibilidad>> findByVetAndFecha(Long veterinarioId, LocalDate fecha);

}
