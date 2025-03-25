package com.vet.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "about_veterinarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AboutVeterinario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 500)
    private String descripcion;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


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

    private void trimAll() {
        this.descripcion = this.descripcion.trim();
    }

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

}
