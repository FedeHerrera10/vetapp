package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vet.app.dtos.request.DtoMascotas;
import com.vet.app.entities.Mascota;

public interface MascotaRepository extends CrudRepository<Mascota, Long> {

    @Query("SELECT new com.vet.app.dtos.request.DtoMascotas(m.id, m.nombre, m.especie, m.raza) FROM Mascota m WHERE m.cliente.id = :clienteId")
    Optional<DtoMascotas[]> getMascotasByClienteId(Long clienteId);

}
