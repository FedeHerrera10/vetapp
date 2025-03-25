package com.vet.app.services;

import jakarta.annotation.PostConstruct;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartupService {
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init() {
        userService.createUserAdmin();
    }
}