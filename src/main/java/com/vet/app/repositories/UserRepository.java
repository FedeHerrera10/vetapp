package com.vet.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.app.dtos.request.DtoUserVeterinario;
import com.vet.app.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.vet.app.dtos.request.DtoUserVeterinario(u.id, u.name, u.lastname) FROM User u JOIN u.roles r WHERE r.name = 'ROLE_VETERINARIO' AND u.enabled = true ORDER BY u.username")
    List<DtoUserVeterinario> findAllVeterinarios();

}
