package com.vet.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
