package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.vet.app.entities.Servicio;

public interface ServicioRepository extends CrudRepository<Servicio, Long> {

    Optional<Servicio> findByNombre(String nombre);
}
