package com.vet.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.entities.ConfirmationToken;
import com.vet.app.entities.Role;
import com.vet.app.entities.User;
import com.vet.app.repositories.ConfirmationTokenRepository;
import com.vet.app.repositories.RoleRepository;
import com.vet.app.repositories.UserRepository;
import com.vet.app.utils.TimeValidation;

@Service
public class UserServiceImpl implements UserService{

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

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        ConfirmationToken oConfirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(oConfirmationToken);
        
        String codigo = oConfirmationToken.getConfirmationToken();
        String to =user.getEmail();
        String subject = "Confirma tu cuenta en VetApp";
        String text =String.format("Hola %s  confirma tu cuenta ingresando el siguiente codigo : %s ", user.getName() , codigo);

        emailService.sendSimpleMessage(to, subject, text);
        
        return user;
    }
    
    @Transactional(readOnly = true)
    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        
        if(token != null)
        {
            boolean timeTokenValidate=TimeValidation.validate(token.getCreatedDate().toString().replace(" ", "T").trim());

            if(timeTokenValidate){
                User user = repository.findByEmailIgnoreCase(token.getUser().getEmail());
                user.setEnabled(true);
                repository.save(user);
                return ResponseEntity.ok("Cuenta confirmada.");
            }
            return ResponseEntity.badRequest().body("El codigo de verificacion expiro.");
        }
        
        return ResponseEntity.badRequest().body("Error, la cuenta no pudo ser confirmada.");
    }
}
