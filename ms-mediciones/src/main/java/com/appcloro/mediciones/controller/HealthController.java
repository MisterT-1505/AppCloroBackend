package com.appcloro.mediciones.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "UP");
        respuesta.put("service", "ms-mediciones");
        respuesta.put("timestamp", LocalDateTime.now().toString());
        return respuesta;
    }
}
