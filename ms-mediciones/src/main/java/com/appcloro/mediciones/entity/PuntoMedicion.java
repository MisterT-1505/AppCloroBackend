package com.appcloro.mediciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "puntos_medicion", indexes = {
        @Index(name = "idx_punto_granja", columnList = "granjaId")
})
public class PuntoMedicion {

    @Id
    private String id;

    @Column(nullable = false)
    private String granjaId;

    @Column(nullable = false)
    private String granjaNombre;

    @Column(nullable = false)
    private String galpon;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private double nivelCloroPpm;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime ultimaActualizacion;

    public PuntoMedicion() {
    }

    public PuntoMedicion(String id, String granjaId, String granjaNombre, String galpon,
                         String ubicacion, double nivelCloroPpm, String estado,
                         LocalDateTime ultimaActualizacion) {
        this.id = id;
        this.granjaId = granjaId;
        this.granjaNombre = granjaNombre;
        this.galpon = galpon;
        this.ubicacion = ubicacion;
        this.nivelCloroPpm = nivelCloroPpm;
        this.estado = estado;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGranjaId() {
        return granjaId;
    }

    public void setGranjaId(String granjaId) {
        this.granjaId = granjaId;
    }

    public String getGranjaNombre() {
        return granjaNombre;
    }

    public void setGranjaNombre(String granjaNombre) {
        this.granjaNombre = granjaNombre;
    }

    public String getGalpon() {
        return galpon;
    }

    public void setGalpon(String galpon) {
        this.galpon = galpon;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
