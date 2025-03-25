package com.vet.app.services.turno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.dtos.request.DtoMascotas;
import com.vet.app.dtos.request.DtoTurnos;
import com.vet.app.entities.Mascota;
import com.vet.app.entities.Servicio;
import com.vet.app.entities.Turnos;
import com.vet.app.entities.User;
import com.vet.app.repositories.MascotaRepository;
import com.vet.app.repositories.TurnosRepository;
import com.vet.app.utils.UtilService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired
    private TurnosRepository turnosRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UtilService utilService;

    @Override
    @Transactional(readOnly = true)
    public List<Turnos> findAll() {
        return (List<Turnos>) turnosRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turnos> findById(Long id) {
        return turnosRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoTurnos> findTurnosByUser(Long userId) {

        Optional<DtoMascotas[]> mascotas = mascotaRepository.getMascotasByClienteId(userId);
        List<DtoTurnos> turnos = new ArrayList<>();

        if (!mascotas.isPresent()) {
            return turnos;
        }

        Arrays.stream(mascotas.get()).forEach(mascota -> {

            turnos.addAll(turnosRepository.findTurnosByMascotaId(mascota.getId()));

        });

        return turnos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoTurnos> findTurnosByVeterinarioId(Long veterinarioId) {
        return turnosRepository.findTurnosByVeterinarioId(veterinarioId);
    }

    @Override
    @Transactional
    public Turnos save(Turnos turno) {

        // System.out.println("Turno: " + turno);
        // List<Object[]> results =
        // turnosRepository.findMascotaVeterinarioServicio(turno.getMascota().getId(),
        // turno.getVeterinario().getId(), turno.getServicio().getId());

        // if (results.isEmpty()) {
        // throw new EntityNotFoundException("No se encontró la mascota, veterinario o
        // servicio");
        // }

        Mascota mascota = turno.getMascota();
        User veterinario = turno.getVeterinario();
        Servicio servicio = turno.getServicio();

        turno.setMascota(mascota);
        turno.setVeterinario(veterinario);
        turno.setServicio(servicio);
        return turnosRepository.save(turno);

    }

    @Override
    @Transactional
    public Optional<Turnos> update(Turnos turno, Long id) {
        return turnosRepository.findById(id).map(m -> {
            utilService.copyNonNullProperties(turno, m);
            return turnosRepository.save(m);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turnos> delete(Long id) {
        return turnosRepository.findById(id).map(turno -> {
            turnosRepository.delete(turno);
            return turno;
        });
    }

}
