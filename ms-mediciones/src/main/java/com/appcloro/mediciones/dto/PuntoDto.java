package com.appcloro.mediciones.dto;

import com.appcloro.mediciones.entity.PuntoMedicion;
import com.appcloro.mediciones.util.FechaUtil;

public record PuntoDto(
        String id,
        String granja,
        String galpon,
        String ubicacion,
        double nivelCloroPpm,
        String estado,
        String ultimaActualizacion
) {
    public static PuntoDto from(PuntoMedicion punto) {
        return new PuntoDto(
                punto.getId(),
                punto.getGranjaNombre(),
                punto.getGalpon(),
                punto.getUbicacion(),
                punto.getNivelCloroPpm(),
                punto.getEstado(),
                FechaUtil.formatear(punto.getUltimaActualizacion())
        );
    }
}
