package com.vet.app.services.servicio;

import java.util.List;
import java.util.Optional;

import com.vet.app.entities.Servicio;

public interface ServicioService {

    List<Servicio> findAll();

    Optional<Servicio> findById(Long id);

    Servicio save(Servicio servicio);

    Optional<Servicio> update(Servicio servicio, Long id);

    Optional<Servicio> delete(Long id);
}
