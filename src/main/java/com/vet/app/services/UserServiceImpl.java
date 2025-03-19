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
import com.vet.app.dtos.request.DtoUserVeterinario;
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

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_CLIENTE = "ROLE_CLIENTE";
    private static final String ROLE_VETERINARIO = "ROLE_VETERINARIO";
    private static final String CONFIRMATION_EMAIL_SUBJECT = "Confirma tu cuenta en VetApp";
    private static final String RESET_PASSWORD_EMAIL_SUBJECT = "Su contraseña fue cambiada con exito!";
    private static final String NEW_CODE_EMAIL_SUBJECT = "Codigo de confirmacion enviado - VetApp";
    private static final String USER_NOT_FOUND = "Usuario no encontrado";

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    public List<DtoUserVeterinario> findAllVeterinarios() {
        return repository.findAllVeterinarios();
    }

    @Override
    @Transactional
    public User save(User user) {
        List<Role> roles = assignRoles(user);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendConfirmationEmail(user, oConfirmationToken);

        return user;
    }

    private List<Role> assignRoles(User user) {
        List<Role> roles = new ArrayList<>();
        roleRepository.findByName(ROLE_USER).ifPresent(roles::add);

        if (user.isAdmin()) {
            roleRepository.findByName(ROLE_ADMIN).ifPresent(roles::add);
        }
        if (user.isCliente()) {
            roleRepository.findByName(ROLE_CLIENTE).ifPresent(roles::add);
        }
        if (user.isVeterinario()) {
            roleRepository.findByName(ROLE_VETERINARIO).ifPresent(roles::add);
        }
        return roles;
    }

    private void sendConfirmationEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = CONFIRMATION_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s", user.getName(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);
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
        User user = repository.findByUsername(dtoResetPassword.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND));
        Long idUser = user.getId();
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(dtoResetPassword.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendResetPasswordEmail(user, oConfirmationToken);

        return true;
    }

    private void sendResetPasswordEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = RESET_PASSWORD_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta nuevamente ingresando el siguiente codigo: %s",
                user.getName(), codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

    @Override
    public boolean newCode(Long idUser) {
        User user = repository.findById(idUser).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND));

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendNewCodeEmail(user, oConfirmationToken);

        return true;
    }

    private void sendNewCodeEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = NEW_CODE_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s", user.getName(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
