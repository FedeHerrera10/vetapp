package com.vet.app.services.mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vet.app.dtos.request.DtoMascotaUpdate;
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
    public boolean update(DtoMascotaUpdate dtoMascotaUpdate, Long id) {

        Mascota m = mascotaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Hubo un error al actualizar la mascota"));

        m.setNombre(dtoMascotaUpdate.getNombre());
        m.setEspecie(dtoMascotaUpdate.getEspecie());
        m.setRaza(dtoMascotaUpdate.getRaza());
        m.setEdad(dtoMascotaUpdate.getEdad());
        m.setPeso(dtoMascotaUpdate.getPeso());
        m.setColor(dtoMascotaUpdate.getColor());
        m.setCaracteristicas(dtoMascotaUpdate.getCaracteristicas());
        m.setImagePet(dtoMascotaUpdate.getImagePet());

        mascotaRepository.save(m);

        return true;
    }

    @Override
    @Transactional
    public Optional<Mascota> delete(Long id) {
        Optional<Mascota> mascota = mascotaRepository.findById(id);
        mascota.ifPresent(mascotaRepository::delete);
        return mascota;
    }

    @Override
    public List<Mascota> findByClienteId(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId);
    }

    @Override
    public boolean changeStatus(String dtoStatusMascota, Long id) {
        Mascota m = mascotaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Hubo un error al actualizar la mascota"));
        m.setEnabled(false);
            if(dtoStatusMascota.equals("active")){
            m.setEnabled(true);
        }
        

        mascotaRepository.save(m);

        return true;
    }

}
