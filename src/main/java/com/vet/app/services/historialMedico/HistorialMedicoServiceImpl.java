package com.vet.app.services.historialMedico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.entities.HistorialMedico;
import com.vet.app.entities.Mascota;
import com.vet.app.dtos.DtoUser;
import com.vet.app.dtos.MapperUtil;
import com.vet.app.dtos.request.DtoHostiriaClinica;
import com.vet.app.dtos.request.DtoMascotas;
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

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public List<DtoHostiriaClinica> findAllByVeterinario(Long veterinarioId) {

        Optional<List<HistorialMedico>> historialMedicoDto = historialMedicoRepository
                .findAllByVeterinario(veterinarioId);
        List<DtoHostiriaClinica> result = new ArrayList<>();

        if (!historialMedicoDto.isPresent()) {
            return result;
        }

        historialMedicoDto.get().forEach(hm -> {
            DtoMascotas dtoMascota = new DtoMascotas();
            dtoMascota.setId(hm.getMascota().getId());
            dtoMascota.setNombre(hm.getMascota().getNombre());
            dtoMascota.setEspecie(hm.getMascota().getEspecie());
            dtoMascota.setRaza(hm.getMascota().getRaza());

            DtoHostiriaClinica dtoHostiriaClinica = new DtoHostiriaClinica();
            dtoHostiriaClinica.setId(hm.getId());
            dtoHostiriaClinica.setNotas(hm.getNotas());
            dtoHostiriaClinica.setTratamientos(hm.getTratamientos());
            dtoHostiriaClinica.setRecetasMedicas(hm.getRecetasMedicas());
            dtoHostiriaClinica.setFecha(hm.getFecha());
            dtoHostiriaClinica.setHorario(hm.getHorario());
            dtoHostiriaClinica.setMascota(dtoMascota);

            result.add(dtoHostiriaClinica);
        });

        return result;
    }

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
    public HistorialMedico save(HistorialMedico historialMedico) {
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
