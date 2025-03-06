package com.vet.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "disponibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veterinario_id")
    private User veterinarioId;

    @Column(nullable = false, name = "fecha")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
    private LocalDate fecha;

    @Column(nullable = false, name = "hora_inicio")
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private LocalTime horaInicio;

    @Column(nullable = false, name = "hora_fin")
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private LocalTime horaFin;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

    @PrePersist
    public void prePersist() {
        this.audit.onCreate();
        this.audit.onUpdate();
    }

    @PreUpdate
    public void preUpdate() {
        this.audit.onUpdate();
    }

}
