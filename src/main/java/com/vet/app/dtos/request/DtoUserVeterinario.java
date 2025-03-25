package com.vet.app.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserVeterinario {
    Long id;
    String name;
    String lastname;
    String imageData;
}
