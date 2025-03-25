package com.vet.app.services.aboutVet;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vet.app.dtos.MapperUtil;
import com.vet.app.dtos.request.DtoAbout;
import com.vet.app.entities.AboutVeterinario;
import com.vet.app.entities.User;
import com.vet.app.repositories.AboutVeterinarioRepository;
import com.vet.app.services.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AboutVeterinarioServiceImpl implements AboutVeterinarioService {

    @Autowired
    private AboutVeterinarioRepository aboutVeterinarioRepository;

    @Autowired
    private MapperUtil mapper;

    @Autowired
    private UserService userService;    

    @Override
    public DtoAbout findByUserId(Long idUser) { 
      Optional<User> user = userService.findById(idUser);
      if (user.isEmpty()) {
        throw new EntityNotFoundException("Veterinario no encontrado");
      }

      AboutVeterinario about = aboutVeterinarioRepository.findByUserId(idUser).orElse(null);
      DtoAbout dtoAbout =mapper.mapEntityToDto(about, DtoAbout.class); 
      return dtoAbout;
    }

    @Override
    public void save(Long idUser, DtoAbout about) {
        Optional<User> user = userService.findById(idUser);
        if (user.isEmpty()) {
          throw new EntityNotFoundException("Veterinario no encontrado");
        }
        AboutVeterinario aboutVeterinario = new AboutVeterinario();
        aboutVeterinario.setDescripcion(about.getDescripcion());
        aboutVeterinario.setUser(user.get());
        aboutVeterinarioRepository.save(aboutVeterinario);
    }

    @Override
    public void update(Long idUser, DtoAbout about) {
      Optional<User> user = userService.findById(idUser);
      if (user.isEmpty()) {
        throw new EntityNotFoundException("Veterinario no encontrado");
      }
      AboutVeterinario aboutVeterinario = aboutVeterinarioRepository.findByUserId(idUser).orElse(null);
      aboutVeterinario.setDescripcion(about.getDescripcion());
      aboutVeterinarioRepository.save(aboutVeterinario);
    }
    

}
