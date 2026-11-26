package com.appcloro.mediciones.controller;

import com.appcloro.mediciones.dto.MedicionDto;
import com.appcloro.mediciones.dto.MedicionRequest;
import com.appcloro.mediciones.service.MedicionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionService medicionService;

    public MedicionController(MedicionService medicionService) {
        this.medicionService = medicionService;
    }

    @GetMapping
    public List<MedicionDto> listar(
            @RequestParam(required = false) String granja,
            @RequestParam(required = false) String puntoId) {
        return medicionService.listar(granja, puntoId);
    }

    @GetMapping("/ultimas")
    public List<MedicionDto> ultimas(@RequestParam(required = false) String granja) {
        return medicionService.ultimas(granja);
    }

    @GetMapping("/punto/{puntoId}")
    public List<MedicionDto> historialPunto(@PathVariable String puntoId) {
        return medicionService.historialPunto(puntoId);
    }

    @GetMapping("/{id}")
    public MedicionDto obtener(@PathVariable Long id) {
        return medicionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicionDto crear(@Valid @RequestBody MedicionRequest request) {
        return medicionService.crear(request);
    }

    @PutMapping("/{id}")
    public MedicionDto actualizar(@PathVariable Long id,
                                  @Valid @RequestBody MedicionRequest request) {
        return medicionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        medicionService.eliminar(id);
    }

    @PostMapping("/simular")
    public List<MedicionDto> simular(@RequestParam String granja) {
        return medicionService.simular(granja);
    }
}
