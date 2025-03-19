package com.vet.app.dtos;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {
    Long id;
    String username;
    String name;
    String lastname;
    String email;
    List<DtoRole> roles;
    Boolean enabled;
    Boolean password_expired;
    String imageProfile;
}
