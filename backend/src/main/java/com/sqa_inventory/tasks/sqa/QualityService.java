package com.sqa_inventory.tasks.sqa;

import org.springframework.stereotype.Service;

/**
 * Servicio que orquesta la recolección de métricas y la generación de reportes de calidad.
 * En una fase posterior, este servicio se integrará con SonarQube y JaCoCo.
 * Por ahora, proporciona una interfaz para obtener el estado de calidad actual.
 */
@Service
public class QualityService {

    /**
     * Obtiene el reporte de calidad basado en métricas proporcionadas.
     * Este método actúa como un puente entre la recolección de datos y el motor de cálculo.
     */
    public QualityReport getQualityStatus(double coverage, int bugs, int smells, int vulnerabilities) {
        return QualityScoringEngine.calculateReport(coverage, bugs, smells, vulnerabilities);
    }
}
