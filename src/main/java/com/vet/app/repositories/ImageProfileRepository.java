package com.vet.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.app.entities.ImageProfile;

public interface ImageProfileRepository extends JpaRepository<ImageProfile, Long>{
    Optional<ImageProfile> findByUserId(Long userId);
}
