package com.vet.app.repositories;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import com.vet.app.entities.Mascota;

public interface MascotaRepository extends CrudRepository<Mascota, Long> {
    List<Mascota> findByClienteId(Long clienteId);
}
