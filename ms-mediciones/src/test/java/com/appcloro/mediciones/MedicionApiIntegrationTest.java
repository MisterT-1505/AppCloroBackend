package com.appcloro.mediciones;

import com.appcloro.mediciones.dto.GranjaDto;
import com.appcloro.mediciones.dto.MedicionDto;
import com.appcloro.mediciones.dto.MedicionRequest;
import com.appcloro.mediciones.dto.PuntoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MedicionApiIntegrationTest {

    private static final ParameterizedTypeReference<List<MedicionDto>> LISTA_MEDICIONES =
            new ParameterizedTypeReference<>() {
            };

    @Autowired
    private TestRestTemplate rest;

    @Test
    void entregaElCatalogoSembrado() {
        ResponseEntity<List<GranjaDto>> granjas = rest.exchange(
                "/api/granjas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, granjas.getStatusCode());
        assertNotNull(granjas.getBody());
        assertEquals(3, granjas.getBody().size());

        ResponseEntity<List<PuntoDto>> puntos = rest.exchange(
                "/api/puntos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, puntos.getStatusCode());
        assertNotNull(puntos.getBody());
        assertEquals(12, puntos.getBody().size());
    }

    @Test
    void devuelveUnaUltimaMedicionPorPuntoUsandoNombreOId() {
        ResponseEntity<List<MedicionDto>> porNombre = rest.exchange(
                "/api/mediciones/ultimas?granja={granja}",
                HttpMethod.GET,
                null,
                LISTA_MEDICIONES,
                "Granja Melipilla");

        ResponseEntity<List<MedicionDto>> porId = rest.exchange(
                "/api/mediciones/ultimas?granja={granja}",
                HttpMethod.GET,
                null,
                LISTA_MEDICIONES,
                "G1");

        assertEquals(HttpStatus.OK, porNombre.getStatusCode());
        assertNotNull(porNombre.getBody());
        assertEquals(4, porNombre.getBody().size());

        assertEquals(HttpStatus.OK, porId.getStatusCode());
        assertNotNull(porId.getBody());
        assertEquals(4, porId.getBody().size());
    }

    @Test
    void simulaMedicionesAceptandoElNombreDeLaGranja() {
        ResponseEntity<List<MedicionDto>> respuesta = rest.exchange(
                "/api/mediciones/simular?granja={granja}",
                HttpMethod.POST,
                null,
                LISTA_MEDICIONES,
                "Granja Talagante");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(4, respuesta.getBody().size());

        respuesta.getBody().forEach(medicion -> {
            assertEquals("Granja Talagante", medicion.granja());
            assertFalse(medicion.estado().isBlank());
        });
    }

    @Test
    void creaUnaMedicionYCalculaElEstadoEnElBackend() {
        MedicionRequest request = new MedicionRequest("G2-P1", null, null, 4.35, null);

        ResponseEntity<MedicionDto> respuesta = rest.exchange(
                "/api/mediciones",
                HttpMethod.POST,
                new HttpEntity<>(request),
                MedicionDto.class);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("Normal", respuesta.getBody().estado());
        assertEquals("Granja Paine", respuesta.getBody().granja());
        assertEquals("Galpón 1", respuesta.getBody().galpon());
        assertNotNull(respuesta.getBody().fechaHora());
    }

    @Test
    void rechazaUnPuntoInexistente() {
        MedicionRequest request = new MedicionRequest("NO-EXISTE", null, null, 3.50, null);

        ResponseEntity<String> respuesta = rest.exchange(
                "/api/mediciones",
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }
}
