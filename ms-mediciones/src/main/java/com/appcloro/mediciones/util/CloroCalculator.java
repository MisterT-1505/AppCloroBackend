package com.appcloro.mediciones.util;

public final class CloroCalculator {

    public static final double MIN_NORMAL = 3.00;
    public static final double MAX_NORMAL = 5.00;
    public static final double MIN_CRITICO = 2.00;
    public static final double MAX_CRITICO = 6.00;

    private CloroCalculator() {
    }

    public static String calcularEstado(double ppm) {
        if (ppm < MIN_CRITICO) {
            return "Crítico";
        }
        if (ppm < MIN_NORMAL) {
            return "Advertencia";
        }
        if (ppm <= MAX_NORMAL) {
            return "Normal";
        }
        if (ppm <= MAX_CRITICO) {
            return "Advertencia";
        }
        return "Crítico";
    }

    public static boolean esCritico(double ppm) {
        return ppm < MIN_CRITICO || ppm > MAX_CRITICO;
    }

    public static boolean esDesviacion(double ppm) {
        return ppm < MIN_NORMAL || ppm > MAX_NORMAL;
    }
}
