package com.vet.app.repositories;

import org.springframework.data.repository.CrudRepository;
import com.vet.app.entities.Servicio;

public interface ServicioRepository extends CrudRepository<Servicio, Long> {
}
