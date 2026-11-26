package com.appcloro.mediciones.controller;

import com.appcloro.mediciones.dto.GranjaDto;
import com.appcloro.mediciones.dto.PuntoDto;
import com.appcloro.mediciones.service.CatalogoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/granjas")
public class GranjaController {

    private final CatalogoService catalogoService;

    public GranjaController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public List<GranjaDto> listar() {
        return catalogoService.listarGranjas();
    }

    @GetMapping("/{granjaId}/puntos")
    public List<PuntoDto> listarPuntos(@PathVariable String granjaId) {
        return catalogoService.listarPuntos(granjaId);
    }
}
