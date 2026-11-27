package com.appcloro.mediciones.service;

import com.appcloro.mediciones.dto.MedicionDto;
import com.appcloro.mediciones.dto.MedicionRequest;
import com.appcloro.mediciones.entity.Medicion;
import com.appcloro.mediciones.entity.PuntoMedicion;
import com.appcloro.mediciones.exception.RecursoNoEncontradoException;
import com.appcloro.mediciones.repository.MedicionRepository;
import com.appcloro.mediciones.repository.PuntoMedicionRepository;
import com.appcloro.mediciones.util.CloroCalculator;
import com.appcloro.mediciones.util.FechaUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MedicionService {

    private final MedicionRepository medicionRepository;
    private final PuntoMedicionRepository puntoRepository;
    private final CatalogoService catalogoService;

    public MedicionService(MedicionRepository medicionRepository,
                           PuntoMedicionRepository puntoRepository,
                           CatalogoService catalogoService) {
        this.medicionRepository = medicionRepository;
        this.puntoRepository = puntoRepository;
        this.catalogoService = catalogoService;
    }

    public List<MedicionDto> listar(String granja, String puntoId) {
        String granjaNombre = catalogoService.nombreDeGranja(granja);
        List<Medicion> mediciones;

        if (textoConValor(granjaNombre) && textoConValor(puntoId)) {
            mediciones = medicionRepository.findByGranjaAndPuntoIdOrderByFechaHoraDesc(granjaNombre, puntoId);
        } else if (textoConValor(granjaNombre)) {
            mediciones = medicionRepository.findByGranjaOrderByFechaHoraDesc(granjaNombre);
        } else if (textoConValor(puntoId)) {
            mediciones = medicionRepository.findByPuntoIdOrderByFechaHoraDesc(puntoId);
        } else {
            mediciones = medicionRepository.findAllByOrderByFechaHoraDesc();
        }

        return mediciones.stream()
                .map(MedicionDto::from)
                .toList();
    }

    /**
     * Última medición por punto. Se calcula sobre el historial ordenado por
     * fecha para que un registro antiguo insertado después no pise al real.
     */
    public List<MedicionDto> ultimas(String granja) {
        String granjaNombre = catalogoService.nombreDeGranja(granja);
        List<Medicion> base = textoConValor(granjaNombre)
                ? medicionRepository.findByGranjaOrderByFechaHoraDesc(granjaNombre)
                : medicionRepository.findAllByOrderByFechaHoraDesc();

        Map<String, Medicion> ultimasPorPunto = new LinkedHashMap<>();
        for (Medicion medicion : base) {
            ultimasPorPunto.putIfAbsent(medicion.getPuntoId(), medicion);
        }

        return ultimasPorPunto.values().stream()
                .map(MedicionDto::from)
                .toList();
    }

    public List<MedicionDto> historialPunto(String puntoId) {
        return medicionRepository.findByPuntoIdOrderByFechaHoraDesc(puntoId).stream()
                .map(MedicionDto::from)
                .toList();
    }

    public MedicionDto obtener(Long id) {
        return MedicionDto.from(obtenerEntidad(id));
    }

    @Transactional
    public MedicionDto crear(MedicionRequest request) {
        PuntoMedicion punto = puntoRepository.findById(request.puntoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Punto de medición no encontrado: " + request.puntoId()));

        double ppm = redondear(request.nivelCloroPpm());
        String estado = CloroCalculator.calcularEstado(ppm);
        LocalDateTime fecha = FechaUtil.parseFlexible(request.fechaHora());

        Medicion medicion = new Medicion(
                punto.getId(),
                punto.getGranjaNombre(),
                punto.getGalpon(),
                ppm,
                estado,
                fecha
        );

        medicion = medicionRepository.save(medicion);
        actualizarPunto(punto, ppm, estado, fecha);

        return MedicionDto.from(medicion);
    }

    @Transactional
    public MedicionDto actualizar(Long id, MedicionRequest request) {
        Medicion medicion = obtenerEntidad(id);

        PuntoMedicion punto = puntoRepository.findById(request.puntoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Punto de medición no encontrado: " + request.puntoId()));

        double ppm = redondear(request.nivelCloroPpm());
        String estado = CloroCalculator.calcularEstado(ppm);
        LocalDateTime fecha = FechaUtil.parseFlexible(request.fechaHora());

        medicion.setPuntoId(punto.getId());
        medicion.setGranja(punto.getGranjaNombre());
        medicion.setGalpon(punto.getGalpon());
        medicion.setNivelCloroPpm(ppm);
        medicion.setEstado(estado);
        medicion.setFechaHora(fecha);

        medicion = medicionRepository.save(medicion);
        actualizarPuntoDesdeUltima(punto);

        return MedicionDto.from(medicion);
    }

    @Transactional
    public void eliminar(Long id) {
        Medicion medicion = obtenerEntidad(id);
        String puntoId = medicion.getPuntoId();

        medicionRepository.delete(medicion);

        puntoRepository.findById(puntoId)
                .ifPresent(this::actualizarPuntoDesdeUltima);
    }

    @Transactional
    public List<MedicionDto> simular(String granja) {
        List<PuntoMedicion> puntos = catalogoService.puntosDeGranja(granja);

        if (puntos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No hay puntos de medición para la granja: " + granja);
        }

        List<MedicionDto> resultado = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now().withNano(0);

        for (int i = 0; i < puntos.size(); i++) {
            PuntoMedicion punto = puntos.get(i);
            double ppm = redondear(ThreadLocalRandom.current().nextDouble(1.50, 6.50));
            String estado = CloroCalculator.calcularEstado(ppm);
            LocalDateTime fecha = ahora.plusSeconds(i);

            Medicion medicion = new Medicion(
                    punto.getId(),
                    punto.getGranjaNombre(),
                    punto.getGalpon(),
                    ppm,
                    estado,
                    fecha
            );

            medicion = medicionRepository.save(medicion);
            actualizarPunto(punto, ppm, estado, fecha);

            resultado.add(MedicionDto.from(medicion));
        }

        return resultado;
    }

    private Medicion obtenerEntidad(Long id) {
        return medicionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Medición no encontrada: " + id));
    }

    private void actualizarPunto(PuntoMedicion punto, double ppm, String estado, LocalDateTime fecha) {
        punto.setNivelCloroPpm(ppm);
        punto.setEstado(estado);
        punto.setUltimaActualizacion(fecha);
        puntoRepository.save(punto);
    }

    private void actualizarPuntoDesdeUltima(PuntoMedicion punto) {
        medicionRepository.findFirstByPuntoIdOrderByFechaHoraDesc(punto.getId())
                .ifPresent(ultima -> actualizarPunto(
                        punto,
                        ultima.getNivelCloroPpm(),
                        ultima.getEstado(),
                        ultima.getFechaHora()
                ));
    }

    private boolean textoConValor(String valor) {
        return valor != null && !valor.isBlank();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
