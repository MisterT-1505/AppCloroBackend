package com.appcloro.mediciones.service;

import com.appcloro.mediciones.dto.GranjaDto;
import com.appcloro.mediciones.dto.PuntoDto;
import com.appcloro.mediciones.entity.PuntoMedicion;
import com.appcloro.mediciones.exception.RecursoNoEncontradoException;
import com.appcloro.mediciones.repository.GranjaRepository;
import com.appcloro.mediciones.repository.PuntoMedicionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoService {

    private final GranjaRepository granjaRepository;
    private final PuntoMedicionRepository puntoRepository;

    public CatalogoService(GranjaRepository granjaRepository,
                           PuntoMedicionRepository puntoRepository) {
        this.granjaRepository = granjaRepository;
        this.puntoRepository = puntoRepository;
    }

    public List<GranjaDto> listarGranjas() {
        return granjaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(GranjaDto::from)
                .toList();
    }

    public List<PuntoDto> listarPuntos(String granjaId) {
        List<PuntoMedicion> puntos = (granjaId == null || granjaId.isBlank())
                ? puntoRepository.findAllByOrderByGranjaIdAscGalponAsc()
                : puntoRepository.findByGranjaIdOrderByGalponAsc(granjaId);

        return puntos.stream()
                .map(PuntoDto::from)
                .toList();
    }

    public PuntoDto obtenerPunto(String puntoId) {
        return PuntoDto.from(obtenerEntidadPunto(puntoId));
    }

    public PuntoMedicion obtenerEntidadPunto(String puntoId) {
        return puntoRepository.findById(puntoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Punto de medición no encontrado: " + puntoId));
    }

    public List<PuntoMedicion> puntosDeGranja(String granjaId) {
        return puntoRepository.findByGranjaIdOrderByGalponAsc(granjaId);
    }
}
