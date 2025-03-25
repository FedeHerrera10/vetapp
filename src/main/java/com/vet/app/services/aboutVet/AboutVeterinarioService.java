package com.vet.app.services.aboutVet;

import com.vet.app.dtos.request.DtoAbout;

public interface  AboutVeterinarioService {
    
    DtoAbout findByUserId(Long idUser);
    void save(Long idUser, DtoAbout about);
    void update(Long idUser, DtoAbout about);
    
}
