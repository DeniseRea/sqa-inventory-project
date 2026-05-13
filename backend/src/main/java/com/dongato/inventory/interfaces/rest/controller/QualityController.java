package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.sqa.QualityReport;
import com.dongato.inventory.sqa.QualityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para exponer las métricas de calidad del Modelo de McCall.
 */
@RestController
@RequestMapping("/api/v1/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityService qualityService;

    /**
     * Endpoint para obtener el puntaje de calidad actual.
     * Ejemplo: GET /api/v1/quality/status?coverage=85&bugs=0&smells=5&vulns=0
     */
    @GetMapping("/status")
    public QualityReport getStatus(
            @RequestParam(defaultValue = "0") double coverage,
            @RequestParam(defaultValue = "0") int bugs,
            @RequestParam(defaultValue = "0") int smells,
            @RequestParam(defaultValue = "0") int vulns) {
        
        return qualityService.getQualityStatus(coverage, bugs, smells, vulns);
    }
}
