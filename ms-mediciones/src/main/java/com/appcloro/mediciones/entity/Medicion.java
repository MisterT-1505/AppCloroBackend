package com.appcloro.mediciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "mediciones", indexes = {
        @Index(name = "idx_medicion_granja", columnList = "granja"),
        @Index(name = "idx_medicion_punto", columnList = "puntoId"),
        @Index(name = "idx_medicion_fecha", columnList = "fechaHora")
})
public class Medicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String puntoId;

    @Column(nullable = false)
    private String granja;

    @Column(nullable = false)
    private String galpon;

    @Column(nullable = false)
    private double nivelCloroPpm;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    public Medicion() {
    }

    public Medicion(String puntoId, String granja, String galpon, double nivelCloroPpm,
                    String estado, LocalDateTime fechaHora) {
        this.puntoId = puntoId;
        this.granja = granja;
        this.galpon = galpon;
        this.nivelCloroPpm = nivelCloroPpm;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPuntoId() {
        return puntoId;
    }

    public void setPuntoId(String puntoId) {
        this.puntoId = puntoId;
    }

    public String getGranja() {
        return granja;
    }

    public void setGranja(String granja) {
        this.granja = granja;
    }

    public String getGalpon() {
        return galpon;
    }

    public void setGalpon(String galpon) {
        this.galpon = galpon;
    }

    public double getNivelCloroPpm() {
        return nivelCloroPpm;
    }

    public void setNivelCloroPpm(double nivelCloroPpm) {
        this.nivelCloroPpm = nivelCloroPpm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
