package com.vet.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Size(max = 100)
    private String descripcion;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

    public Servicio(Long id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        this.audit.onCreate();
        this.audit.onUpdate();
        trimAll();
    }

    @PreUpdate
    public void preUpdate() {
        this.audit.onUpdate();
        trimAll();
    }

    public void trimAll() {
        if (this.nombre != null) {
            this.nombre = this.nombre.trim();
        }
        if (this.descripcion != null) {
            this.descripcion = this.descripcion.trim();
        }
    }

}
