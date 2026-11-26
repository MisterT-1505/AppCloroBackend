package com.appcloro.mediciones.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloroCalculatorTest {

    @Test
    void clasificaNivelesNormales() {
        assertEquals("Normal", CloroCalculator.calcularEstado(3.00));
        assertEquals("Normal", CloroCalculator.calcularEstado(4.25));
        assertEquals("Normal", CloroCalculator.calcularEstado(5.00));
    }

    @Test
    void clasificaAdvertencias() {
        assertEquals("Advertencia", CloroCalculator.calcularEstado(2.00));
        assertEquals("Advertencia", CloroCalculator.calcularEstado(2.99));
        assertEquals("Advertencia", CloroCalculator.calcularEstado(5.01));
        assertEquals("Advertencia", CloroCalculator.calcularEstado(6.00));
    }

    @Test
    void clasificaNivelesCriticos() {
        assertEquals("Crítico", CloroCalculator.calcularEstado(1.99));
        assertEquals("Crítico", CloroCalculator.calcularEstado(6.01));
        assertEquals("Crítico", CloroCalculator.calcularEstado(0.00));
    }

    @Test
    void identificaDesviacionesYCriticos() {
        assertTrue(CloroCalculator.esCritico(1.50));
        assertTrue(CloroCalculator.esCritico(6.50));
        assertFalse(CloroCalculator.esCritico(4.00));

        assertTrue(CloroCalculator.esDesviacion(2.50));
        assertTrue(CloroCalculator.esDesviacion(5.50));
        assertFalse(CloroCalculator.esDesviacion(4.00));
    }
}
