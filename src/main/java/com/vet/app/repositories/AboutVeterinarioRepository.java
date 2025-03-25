package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.app.entities.AboutVeterinario;

public interface AboutVeterinarioRepository extends JpaRepository<AboutVeterinario, Long> {
    Optional<AboutVeterinario> findByUserId(Long idUser);
}
