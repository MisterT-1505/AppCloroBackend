package com.appcloro.mediciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicionRequest(
        @NotBlank(message = "El puntoId es obligatorio")
        String puntoId,

        String granja,

        String galpon,

        @NotNull(message = "El nivel de cloro es obligatorio")
        @DecimalMin(value = "0.0", message = "El nivel de cloro no puede ser negativo")
        Double nivelCloroPpm,

        String fechaHora
) {
}
