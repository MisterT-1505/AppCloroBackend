package com.appcloro.mediciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "granjas")
public class Granja {

    @Id
    private String id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private int totalGalpones;

    public Granja() {
    }

    public Granja(String id, String nombre, String ubicacion, int totalGalpones) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.totalGalpones = totalGalpones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getTotalGalpones() {
        return totalGalpones;
    }

    public void setTotalGalpones(int totalGalpones) {
        this.totalGalpones = totalGalpones;
    }
}
