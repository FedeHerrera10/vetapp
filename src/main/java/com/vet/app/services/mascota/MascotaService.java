package com.vet.app.services.mascota;

import java.util.List;
import java.util.Optional;

import com.vet.app.dtos.request.DtoMascotas;
import com.vet.app.entities.Mascota;

public interface MascotaService {

    List<DtoMascotas> findAll();

    Optional<Mascota> findById(Long id);

    Mascota save(Mascota mascota, Long clienteId);

    Optional<Mascota> update(Mascota mascota, Long id);

    Optional<Mascota> delete(Long id);

    Optional<DtoMascotas[]> getMascotasByClienteId(Long clienteId);

}
