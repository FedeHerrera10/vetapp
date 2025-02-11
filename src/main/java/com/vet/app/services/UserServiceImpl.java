package com.vet.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.entities.ConfirmationToken;
import com.vet.app.entities.Role;
import com.vet.app.entities.User;
import com.vet.app.repositories.ConfirmationTokenRepository;
import com.vet.app.repositories.RoleRepository;
import com.vet.app.repositories.UserRepository;
import com.vet.app.utils.TimeValidation;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

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
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        String codigo = oConfirmationToken.getConfirmationToken();
        String to = user.getEmail();
        String subject = "Confirma tu cuenta en VetApp";
        String text = String.format("Hola %s  confirma tu cuenta ingresando el siguiente codigo : %s ", user.getName(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);

        return user;
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

    @Transactional
    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new EntityNotFoundException("Codigo no encontrado."));

        boolean timeTokenValidate = TimeValidation.validate(token.getCreatedDate().toString().replace(" ", "T").trim());

        if (!timeTokenValidate) {
            return ResponseEntity.badRequest().body("El codigo de verificacion expiro.");
        }

        User user = repository.findByEmailIgnoreCase(token.getUser().getEmail());
        user.setEnabled(true);
        repository.save(user);
        confirmationTokenRepository.delete(token);
        return ResponseEntity.ok("Cuenta confirmada.");
    }

    @Transactional
    @Override
    public boolean resetPassword(DtoResetPassword dtoResetPassword) {
        User user = repository.findById(dtoResetPassword.getId()).orElseThrow(
                () -> new EntityNotFoundException("Usuario no encontrado"));

        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        String codigo = oConfirmationToken.getConfirmationToken();
        String to = user.getEmail();
        String subject = "Su contraseña fue cambiada con exito!";
        String text = String.format("Hola %s  confirma tu cuenta nuevamente  ingresando el siguiente codigo : %s ",
                user.getName(), codigo);

        emailService.sendSimpleMessage(to, subject, text);

        return true;
    }

    @Override
    public boolean newCode(Long idUser) {
        User user = repository.findById(idUser).orElseThrow(
                () -> new EntityNotFoundException("Usuario no encontrado"));

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        String codigo = oConfirmationToken.getConfirmationToken();
        String to = user.getEmail();
        String subject = "Codigo de confirmacion enviado - VetApp";
        String text = String.format("Hola %s  confirma tu cuenta ingresando el siguiente codigo : %s ", user.getName(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);

        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
