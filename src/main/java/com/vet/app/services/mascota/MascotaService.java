package com.vet.app.services.mascota;

import java.util.List;
import java.util.Optional;

import com.vet.app.entities.Mascota;
import com.vet.app.dtos.request.DtoMascotaUpdate;

public interface MascotaService {

    List<Mascota> findAll();

    Optional<Mascota> findById(Long id);

    Mascota save(Mascota mascota, Long clienteId);

    boolean update(DtoMascotaUpdate dtoMascotaUpdate, Long id);

    Optional<Mascota> delete(Long id);

    List<Mascota> findByClienteId(Long clienteId);

    boolean changeStatus(String dtoStatusMascota, Long id);

}
