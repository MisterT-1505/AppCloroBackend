package com.appcloro.mediciones.config;

import com.appcloro.mediciones.entity.Granja;
import com.appcloro.mediciones.entity.Medicion;
import com.appcloro.mediciones.entity.PuntoMedicion;
import com.appcloro.mediciones.repository.GranjaRepository;
import com.appcloro.mediciones.repository.MedicionRepository;
import com.appcloro.mediciones.repository.PuntoMedicionRepository;
import com.appcloro.mediciones.util.CloroCalculator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final GranjaRepository granjaRepository;
    private final PuntoMedicionRepository puntoRepository;
    private final MedicionRepository medicionRepository;

    public DataInitializer(GranjaRepository granjaRepository,
                           PuntoMedicionRepository puntoRepository,
                           MedicionRepository medicionRepository) {
        this.granjaRepository = granjaRepository;
        this.puntoRepository = puntoRepository;
        this.medicionRepository = medicionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (granjaRepository.count() > 0) {
            return;
        }

        crearGranja("G1", "Granja Melipilla", "Melipilla, Región Metropolitana", 4,
                new double[]{3.80, 2.40, 1.60, 4.20});

        crearGranja("G2", "Granja Paine", "Paine, Región Metropolitana", 4,
                new double[]{3.20, 3.50, 5.40, 2.80});

        crearGranja("G3", "Granja Talagante", "Talagante, Región Metropolitana", 4,
                new double[]{4.10, 1.80, 3.90, 5.80});
    }

    private void crearGranja(String id, String nombre, String ubicacion, int totalGalpones,
                             double[] niveles) {
        granjaRepository.save(new Granja(id, nombre, ubicacion, totalGalpones));

        LocalDateTime base = LocalDateTime.now().withNano(0).minusMinutes(30);

        for (int i = 0; i < totalGalpones; i++) {
            String puntoId = id + "-P" + (i + 1);
            String galpon = "Galpón " + (i + 1);
            double ppm = niveles[i];
            String estado = CloroCalculator.calcularEstado(ppm);
            LocalDateTime fecha = base.plusMinutes(i * 5L);

            PuntoMedicion punto = new PuntoMedicion(
                    puntoId,
                    id,
                    nombre,
                    galpon,
                    "Sector " + (i + 1),
                    ppm,
                    estado,
                    fecha
            );
            puntoRepository.save(punto);

            Medicion medicion = new Medicion(puntoId, nombre, galpon, ppm, estado, fecha);
            medicionRepository.save(medicion);
        }
    }
}
