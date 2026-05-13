package com.dongato.inventory.sqa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QualityServiceTest {

    private final QualityService qualityService = new QualityService();

    @Test
    @DisplayName("Debe delegar el calculo al motor de calidad y retornar el reporte")
    void shouldReturnQualityReportFromScoringEngine() {
        QualityReport report = qualityService.getQualityStatus(92.0, 0, 3, 0);

        assertEquals(92.0, report.coverage(), 0.001);
        assertEquals(96.4, report.score(), 0.001);
        assertEquals("PASSED", report.status());
    }

    @Test
    @DisplayName("Debe propagar errores de metricas invalidas")
    void shouldPropagateInvalidMetricsErrors() {
        assertThrows(IllegalArgumentException.class,
                () -> qualityService.getQualityStatus(101.0, 0, 0, 0));
    }
}
