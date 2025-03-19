package com.vet.app.services.disponibilidad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import com.vet.app.utils.EstadosEnum;

@Service
public class DisponibilidadServiceImpl implements DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private TurnosRepository turnoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<IntervaloDto> obtenerIntervaloDisponibles(Long veterinarioId, IntervaloDto fecha) {

        Optional<List<Disponibilidad>> disponibilidades = disponibilidadRepository.findByVetAndFecha(
                veterinarioId,
                fecha.getFecha());
        List<IntervaloDto> intervalosDisponibles = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

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
                boolean reservado = turnoRepository.existsByVeterinarioIdAndFechaAndHorarioAndEstado(veterinarioId,
                        fecha.getFecha(),
                        inicio, EstadosEnum.PENDIENTE);

                if (!reservado) {
                    intervalosDisponibles
                            .add(new IntervaloDto(fecha.getFecha(), inicio.format(formatter), horaFinIntervalo));
                }

                inicio = inicio.plusHours(1);
            }
        }
        return intervalosDisponibles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> obtenerRangoFechasISO(Long veterinarioId) {
        List<String> fechasISO = new ArrayList<>();

        Optional<List<Disponibilidad>> disp = disponibilidadRepository.findByVet(veterinarioId);

        disp.ifPresent(d -> {
            for (Disponibilidad fec : d) {
                // Asegúrate d e que fechaInicio no sea después de fechaFin
                if (fec.getFechaInicio() != null && fec.getFechaFin() != null
                        && !fec.getFechaInicio().isAfter(fec.getFechaFin())) {
                    long diasEntreFechas = ChronoUnit.DAYS.between(fec.getFechaInicio(), fec.getFechaFin());
                    for (long i = 0; i <= diasEntreFechas; i++) {
                        LocalDate fecha = fec.getFechaInicio().plusDays(i);
                        fechasISO.add(fecha.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                }
            }

        });

        return fechasISO;
    }

    @Override
    public List<Disponibilidad> findAll() {
        return null;
    }

    @Override
    public Optional<Disponibilidad> delete(Long id) {
        return Optional.empty();
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
