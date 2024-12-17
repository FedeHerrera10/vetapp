package com.vet.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.entities.Role;
import com.vet.app.entities.User;
import com.vet.app.repositories.RoleRepository;
import com.vet.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {

        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        if (user.isCliente()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_CLIENTE");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        if (user.isVeterinario()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_VETERINARIO");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

}
