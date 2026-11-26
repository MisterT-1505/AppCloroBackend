package com.appcloro.mediciones.controller;

import com.appcloro.mediciones.dto.PuntoDto;
import com.appcloro.mediciones.service.CatalogoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
public class PuntoController {

    private final CatalogoService catalogoService;

    public PuntoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public List<PuntoDto> listar(@RequestParam(required = false) String granja) {
        return catalogoService.listarPuntos(granja);
    }

    @GetMapping("/{puntoId}")
    public PuntoDto obtener(@PathVariable String puntoId) {
        return catalogoService.obtenerPunto(puntoId);
    }
}
