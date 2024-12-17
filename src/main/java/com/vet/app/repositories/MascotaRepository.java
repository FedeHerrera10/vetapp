package com.vet.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vet.app.entities.Mascota;

public interface MascotaRepository extends CrudRepository<Mascota, Long> {

}
