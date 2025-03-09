package com.vet.app.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserUpdate {
    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    
    @Email
    private String email;

    @NotNull
    private boolean enabled;
}
