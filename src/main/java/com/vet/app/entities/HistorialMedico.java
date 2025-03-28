package com.vet.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historial_medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    private String notas;

    private String tratamientos;

    @Column(name = "recetas_medicas")
    private String recetasMedicas;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User veterinario;

    @Column(name = "fecha", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
    private LocalDate fecha;

    @Column(name = "horario", nullable = false)
    @DateTimeFormat(pattern = "HH:mm", iso = ISO.TIME)
    private LocalTime horario;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

    public void prePersist() {
        this.audit.onCreate();
        this.audit.onUpdate();
        trimAll();
    }

    public void preUpdate() {
        this.audit.onUpdate();
        trimAll();
    }

    private void trimAll() {
        this.notas = this.notas.trim();
        this.tratamientos = this.tratamientos.trim();
        this.recetasMedicas = this.recetasMedicas.trim();
    }
}
