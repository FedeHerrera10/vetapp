package com.vet.app.services.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.entities.Servicio;
import com.vet.app.repositories.ServicioRepository;
import com.vet.app.utils.UtilService;

import jakarta.persistence.EntityExistsException;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UtilService utilService;

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findAll() {
        return (List<Servicio>) servicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

    @Override
    @Transactional
    public Servicio save(Servicio servicio) {
        Optional<Servicio> servicioOptional = servicioRepository.findByNombre(servicio.getNombre());

        if (servicioOptional.isPresent()) {
            throw new EntityExistsException("Ya existe un servicio con ese nombre");
        }

        return servicioRepository.save(servicio);
    }

    @Override
    @Transactional
    public Optional<Servicio> update(Servicio servicio, Long id) {
        return servicioRepository.findById(id).map(s -> {
            utilService.copyNonNullProperties(servicio, s);
            return servicioRepository.save(s);
        });
    }

    @Override
    @Transactional
    public Optional<Servicio> delete(Long id) {
        return servicioRepository.findById(id).map(s -> {
            servicioRepository.delete(s);
            return s;
        });
    }

}
