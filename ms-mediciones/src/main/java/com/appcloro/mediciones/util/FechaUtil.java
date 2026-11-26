package com.appcloro.mediciones.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class FechaUtil {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter APP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter APP_CON_SEGUNDOS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FechaUtil() {
    }

    public static LocalDateTime parseFlexible(String valor) {
        if (valor == null || valor.isBlank()) {
            return LocalDateTime.now().withNano(0);
        }

        String limpio = valor.trim();
        try {
            return LocalDateTime.parse(limpio, ISO).withNano(0);
        } catch (DateTimeParseException ignored) {
            // Se intenta con el formato que usa la app.
        }
        try {
            return LocalDateTime.parse(limpio, APP);
        } catch (DateTimeParseException ignored) {
            // Se intenta con segundos.
        }
        try {
            return LocalDateTime.parse(limpio, APP_CON_SEGUNDOS);
        } catch (DateTimeParseException ignored) {
            // Si no se puede interpretar, se usa la hora actual.
        }
        return LocalDateTime.now().withNano(0);
    }

    public static String formatear(LocalDateTime valor) {
        return valor == null ? null : valor.format(ISO);
    }

    public static String formatearApp(LocalDateTime valor) {
        return valor == null ? null : valor.format(APP);
    }
}
