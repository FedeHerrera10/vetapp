package com.vet.app.services.mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vet.app.entities.Mascota;
import com.vet.app.entities.User;
import com.vet.app.repositories.MascotaRepository;
import com.vet.app.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<Mascota> findAll() {
        return (List<Mascota>) mascotaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mascota> findById(Long id) {
        return mascotaRepository.findById(id);
    }

    @Override
    @Transactional
    public Mascota save(Mascota mascota, Long clienteId) {

        User cliente = userService.findById(clienteId).orElseThrow(
                () -> new EntityNotFoundException("Hubo un error al dar de alta la mascota"));

        mascota.setCliente(cliente);
        return mascotaRepository.save(mascota);
    }

    @Override
    @Transactional
    public Optional<Mascota> update(Mascota mascota, Long id) {
        Optional<Mascota> mascotaDb = mascotaRepository.findById(id);

        mascotaDb.ifPresent(m -> {
            if (mascota.getEspecie() != null) {
                m.setEspecie(mascota.getEspecie());
            }
            if (mascota.getRaza() != null) {
                m.setRaza(mascota.getRaza());
            }
            if (mascota.getEdad() != null) {
                m.setEdad(mascota.getEdad());
            }
            if (mascota.getVacunas() != null) {
                m.setVacunas(mascota.getVacunas());
            }
            mascotaRepository.save(m);
        });

        return mascotaDb;
    }

    @Override
    @Transactional
    public Optional<Mascota> delete(Long id) {
        Optional<Mascota> mascota = mascotaRepository.findById(id);
        mascota.ifPresent(mascotaRepository::delete);
        return mascota;
    }

}
