package com.appcloro.mediciones.dto;

import com.appcloro.mediciones.entity.Medicion;
import com.appcloro.mediciones.util.FechaUtil;

public record MedicionDto(
        Long id,
        String puntoId,
        String granja,
        String galpon,
        double nivelCloroPpm,
        String estado,
        String fechaHora
) {
    public static MedicionDto from(Medicion medicion) {
        return new MedicionDto(
                medicion.getId(),
                medicion.getPuntoId(),
                medicion.getGranja(),
                medicion.getGalpon(),
                medicion.getNivelCloroPpm(),
                medicion.getEstado(),
                FechaUtil.formatear(medicion.getFechaHora())
        );
    }
}
