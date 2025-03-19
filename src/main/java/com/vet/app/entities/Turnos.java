package com.vet.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vet.app.utils.EstadosEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "veterinario_id")
    private User veterinario;

    @Column(name = "fecha", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
    private LocalDate fecha;

    @Column(name = "horario", nullable = false)
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private EstadosEnum estado;

    @Column(name = "notificaciones")
    private Boolean notificaciones;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

    @PrePersist
    public void prePersist() {
        this.audit.onCreate();
        this.audit.onUpdate();
        this.notificaciones = false;
        this.estado = EstadosEnum.PENDIENTE;
    }

    @PreUpdate
    public void preUpdate() {
        this.audit.onUpdate();
    }

}