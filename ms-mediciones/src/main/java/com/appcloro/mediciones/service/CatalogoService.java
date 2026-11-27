package com.appcloro.mediciones.service;

import com.appcloro.mediciones.dto.GranjaDto;
import com.appcloro.mediciones.dto.PuntoDto;
import com.appcloro.mediciones.entity.Granja;
import com.appcloro.mediciones.entity.PuntoMedicion;
import com.appcloro.mediciones.exception.RecursoNoEncontradoException;
import com.appcloro.mediciones.repository.GranjaRepository;
import com.appcloro.mediciones.repository.PuntoMedicionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<PuntoDto> listarPuntos(String granjaIdONombre) {
        List<PuntoMedicion> puntos = textoConValor(granjaIdONombre)
                ? puntosDeGranja(granjaIdONombre)
                : puntoRepository.findAllByOrderByGranjaIdAscGalponAsc();

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

    /**
     * Acepta el id de la granja ("G1") o su nombre ("Granja Melipilla"),
     * porque la app usa el nombre y las pruebas usan el id.
     */
    public List<PuntoMedicion> puntosDeGranja(String granjaIdONombre) {
        String granjaId = resolverGranjaId(granjaIdONombre);
        return puntoRepository.findByGranjaIdOrderByGalponAsc(granjaId);
    }

    public String resolverGranjaId(String granjaIdONombre) {
        return buscarGranja(granjaIdONombre)
                .map(Granja::getId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Granja no encontrada: " + granjaIdONombre));
    }

    /**
     * Devuelve el nombre de la granja tal como se guarda en las mediciones.
     * Si el valor no corresponde a una granja registrada, se devuelve igual
     * para que las consultas simplemente no encuentren resultados.
     */
    public String nombreDeGranja(String granjaIdONombre) {
        if (!textoConValor(granjaIdONombre)) {
            return null;
        }
        return buscarGranja(granjaIdONombre)
                .map(Granja::getNombre)
                .orElse(granjaIdONombre.trim());
    }

    private Optional<Granja> buscarGranja(String valor) {
        if (!textoConValor(valor)) {
            return Optional.empty();
        }

        String limpio = valor.trim();
        Optional<Granja> porId = granjaRepository.findById(limpio);
        if (porId.isPresent()) {
            return porId;
        }

        return granjaRepository.findAll().stream()
                .filter(granja -> granja.getNombre().equalsIgnoreCase(limpio))
                .findFirst();
    }

    private boolean textoConValor(String valor) {
        return valor != null && !valor.isBlank();
    }
}
