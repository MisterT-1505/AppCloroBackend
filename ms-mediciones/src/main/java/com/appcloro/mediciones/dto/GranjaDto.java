package com.appcloro.mediciones.dto;

import com.appcloro.mediciones.entity.Granja;

public record GranjaDto(
        String id,
        String nombre,
        String ubicacion,
        int totalGalpones
) {
    public static GranjaDto from(Granja granja) {
        return new GranjaDto(
                granja.getId(),
                granja.getNombre(),
                granja.getUbicacion(),
                granja.getTotalGalpones()
        );
    }
}
