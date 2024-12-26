package com.vet.app.services.historialMedico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.entities.HistorialMedico;
import com.vet.app.entities.Mascota;
import com.vet.app.repositories.HistorialMedicoRepository;
import com.vet.app.repositories.MascotaRepository;
import com.vet.app.utils.UtilService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> findAll() {
        return (List<HistorialMedico>) historialMedicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialMedico> findById(Long id) {
        return historialMedicoRepository.findById(id);
    }

    @Override
    @Transactional
    public HistorialMedico save(HistorialMedico historialMedico, Long mascotaId) {

        Mascota mascotaOptional = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la mascota"));

        historialMedico.setMascota(mascotaOptional);
        return historialMedicoRepository.save(historialMedico);
    }

    @Override
    @Transactional
    public Optional<HistorialMedico> update(HistorialMedico historialMedico, Long id) {
        return historialMedicoRepository.findById(id).map(hm -> {
            utilService.copyNonNullProperties(historialMedico, hm);
            return historialMedicoRepository.save(hm);
        });
    }

    @Override
    @Transactional
    public Optional<HistorialMedico> delete(Long id) {
        Optional<HistorialMedico> historialMedico = historialMedicoRepository.findById(id);
        historialMedico.ifPresent(historialMedicoRepository::delete);
        return historialMedico;
    }

}
