package com.vet.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.dtos.DtoUser;
import com.vet.app.dtos.MapperUtil;
import com.vet.app.dtos.request.DtoResetPassword;
import com.vet.app.dtos.request.DtoUserUpdate;
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

    @Autowired
    private ServiceJwt serviceJwt;

    @Autowired
    private MapperUtil mapperUtil;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_CLIENTE = "ROLE_CLIENTE";
    private static final String ROLE_VETERINARIO = "ROLE_VETERINARIO";
    private static final String CONFIRMATION_EMAIL_SUBJECT = "Confirma tu cuenta en VetApp";
    private static final String RESET_PASSWORD_EMAIL_SUBJECT = "Su contraseña fue cambiada con exito!";
    private static final String NEW_CODE_EMAIL_SUBJECT = "Codigo de confirmacion enviado - VetApp";
    private static final String USER_NOT_FOUND = "Usuario no encontrado";
    private static final String CODE_NOT_FOUND = "Codigo no encontrado";
    private static final String CODE_EXPIRED = "El codigo ha expirado";
    private static final String ACCOUNT_CONFIRM_OK = "La cuenta se ha confirmado con exito";

    @Override
    @Transactional(readOnly = true)
    public List<DtoUser> findAll() {
        List<User> users = new ArrayList<>();
        users = (List<User>) repository.findAll();

        return mapperUtil.mapList(users, DtoUser.class);
    }

    @Override
    @Transactional
    public DtoUser save(User user) {
        String passwordRaw = "";

        List<Role> roles = assignRoles(user);
        user.setRoles(roles);

        if (isRoleClient(user) == false) {
            user.setPassword_expired(false);
            passwordRaw = user.getPassword();
            user.setEnabled(true);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendConfirmationEmail(user, oConfirmationToken, passwordRaw);

        return mapperUtil.mapEntityToDto(user, DtoUser.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND)
        ));
    }

    @Override
    public boolean existsByUsername(String username) {  
        return repository.existsByUsername(username);
    }

    @Transactional
    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).orElseThrow(
                () -> new EntityNotFoundException(CODE_NOT_FOUND));

        boolean timeTokenValidate = TimeValidation.validate(token.getCreatedDate().toString().replace(" ", "T").trim());

        if (!timeTokenValidate) {
            return ResponseEntity.badRequest().body(CODE_EXPIRED);
        }

        User user = repository.findByEmailIgnoreCase(token.getUser().getEmail());
        user.setEnabled(true);
        user.setPassword_expired(false);
        repository.save(user);
        confirmationTokenRepository.delete(token);
        return ResponseEntity.ok(ACCOUNT_CONFIRM_OK);
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

    @Override
    public boolean newCode(String email) {
        User user = repository.findByEmailIgnoreCase(email);

        if (user == null) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        Long idUser = user.getId();
        confirmationTokenRepository.deleteAllByUser_Id(idUser);
        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);

        sendNewCodeEmail(user, oConfirmationToken);

        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getUser() {
        User userLogged = (User) serviceJwt.getUserLogged().getBody();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapperUtil.mapEntityToDto(userLogged, DtoUser.class));
    }


    @Override
    @Transactional
    public boolean updateUser(Long id, DtoUserUpdate userUpdate) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        user.setName(userUpdate.getName());
        user.setLastname(userUpdate.getLastname());
        user.setEmail(userUpdate.getEmail());
        user.setEnabled(userUpdate.isEnabled());
        
        repository.save(user);

        return true;
    }


    /* FUNCIONES PRIVADAS */

    private List<Role> assignRoles(User user) {
        List<Role> roles = new ArrayList<>();

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

    private boolean isRoleClient(User user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_CLIENTE));
    }

    private void sendConfirmationEmail(User user, ConfirmationToken token, String passwordRaw) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = CONFIRMATION_EMAIL_SUBJECT;

        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s", user.getName(),
                codigo);

        if (isRoleClient(user) == false) {
            text = "";
            text = String.format(
                    "Hola %s, tu contraseña es %s recuerda cambiarla.",
                    user.getName(), passwordRaw);
        }

        emailService.sendSimpleMessage(to, subject, text);
    }

    private void sendResetPasswordEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = RESET_PASSWORD_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta nuevamente ingresando el siguiente codigo: %s",
                user.getName(), codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

    private void sendNewCodeEmail(User user, ConfirmationToken token) {
        String codigo = token.getConfirmationToken();
        String to = user.getEmail();
        String subject = NEW_CODE_EMAIL_SUBJECT;
        String text = String.format("Hola %s, confirma tu cuenta ingresando el siguiente codigo: %s", user.getName(),
                codigo);

        emailService.sendSimpleMessage(to, subject, text);
    }

    

}
