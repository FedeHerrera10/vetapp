package com.vet.app.services.disponibilidad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.app.dtos.request.IntervaloDto;
import com.vet.app.entities.Disponibilidad;
import com.vet.app.repositories.DisponibilidadRepository;
import com.vet.app.repositories.TurnosRepository;

@Service
public class DisponibilidadServiceImpl implements DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private TurnosRepository turnoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<IntervaloDto> obtenerIntervaloDisponibles(Long veterinarioId, LocalDate fecha) {

        Optional<List<Disponibilidad>> disponibilidades = disponibilidadRepository.findByVetAndFecha(
                veterinarioId,
                fecha);
        List<IntervaloDto> intervalosDisponibles = new ArrayList<>();

        if (!disponibilidades.isPresent()) {
            return intervalosDisponibles;
        }

        // Para cada registro de disponibilidad (por ejemplo, 8:00 a 12:00 y 14:00 a
        // 20:00)
        for (Disponibilidad disponibilidad : disponibilidades.get()) {
            LocalTime inicio = disponibilidad.getHoraInicio();
            LocalTime fin = disponibilidad.getHoraFin();

            // Se asume que cada turno dura 1 hora
            while (inicio.plusHours(1).compareTo(fin) <= 0) {

                LocalTime horaFinIntervalo = inicio.plusHours(1);

                // Se verifica que el turno en este intervalo no haya sido ya reservado
                boolean reservado = turnoRepository.existsByVeterinarioIdAndFechaAndHorario(veterinarioId, fecha,
                        inicio);

                if (!reservado) {
                    intervalosDisponibles.add(new IntervaloDto(fecha, inicio, horaFinIntervalo));
                }

                inicio = inicio.plusHours(1);
            }
        }
        return intervalosDisponibles;
    }

    @Override
    public Optional<Disponibilidad> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Disponibilidad> findAll() {
        return null;
    }

    @Override
    public Optional<Disponibilidad> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Disponibilidad save(Disponibilidad turno) {
        return null;
    }

    @Override
    public Optional<Disponibilidad> update(Disponibilidad turno, Long id) {
        return Optional.empty();
    }

}
