package com.vet.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vet.app.entities.User;
import com.vet.app.repositories.UserRepository;

@Service
public class ServiceJwt {

    @Autowired
    private UserRepository userRepository;

    private Optional<User> getUserNameFromToken() {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = null;
        
        if(authentication ==null){
            return optionalUser;
        }
        // Extraer el username del usuario autenticado
       optionalUser=Optional.ofNullable(userRepository.findByUsername(authentication.getName()).get());
        return optionalUser;
    }

    public ResponseEntity<?> getUserLogged(){
        Optional<User> userLogged = getUserNameFromToken();

        if(!userLogged.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userLogged.get());
    }
}
