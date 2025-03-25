package com.vet.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.app.entities.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
}
