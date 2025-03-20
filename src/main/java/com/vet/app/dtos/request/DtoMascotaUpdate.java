package com.vet.app.dtos.request;

import lombok.Data;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoMascotaUpdate {
     @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El nombre es obligatorio")
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "El nombre es obligatorio")
    private Integer edad;

    private Double peso;

    private String color;
    
    private String caracteristicas;
    
    @Lob
    private String imagePet;
}
