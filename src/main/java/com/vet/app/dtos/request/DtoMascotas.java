package com.vet.app.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoMascotas {

    private Long id;

    private String nombre;

    private String especie;

    private String raza;

}
