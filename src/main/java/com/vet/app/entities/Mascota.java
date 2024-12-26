package com.vet.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    private String raza;

    @NotNull(message = "El nombre es obligatorio")
    private Integer edad;

    @NotBlank(message = "Las vacunas son obligatorias")
    private String vacunas;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private User cliente;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turnos> turnos;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialMedico> historialMedico;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

    @PrePersist
    public void prePersist() {
        this.audit.onCreate();
        this.audit.onUpdate();
        trimAll();
    }

    @PreUpdate
    public void postPersist() {
        this.audit.onUpdate();
        trimAll();
    }

    private void trimAll() {
        if (this.especie != null) {
            this.especie = this.especie.trim();
        }
        if (this.raza != null) {
            this.raza = this.raza.trim();
        }
        if (this.vacunas != null) {
            this.vacunas = this.vacunas.trim();
        }
    }

}