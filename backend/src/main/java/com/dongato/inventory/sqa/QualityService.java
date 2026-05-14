package com.dongato.inventory.sqa;

import org.springframework.stereotype.Service;

/**
 * Servicio que orquesta la recolección de métricas y la generación de reportes de calidad.
 */
@Service
public class QualityService {

    private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    /**
     * Obtiene el reporte de calidad basado en métricas proporcionadas.
     */
    public QualityReport getQualityStatus(double coverage, int bugs, int smells, int vulnerabilities) {
        return QualityScoringEngine.calculateReport(coverage, bugs, smells, vulnerabilities);
    }
}
