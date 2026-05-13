package com.dongato.inventory.sqa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QualityServiceTest {

    private final QualityService qualityService = new QualityService();

    @Test
    @DisplayName("Should delegate to QualityScoringEngine and return report")
    void shouldReturnQualityReport() {
        QualityReport report = qualityService.getQualityStatus(88.0, 1, 2, 0);

        assertNotNull(report);
        assertEquals(88.0, report.coverage());
        assertEquals(1, report.bugs());
        assertEquals(2, report.smells());
        assertEquals(0, report.vulnerabilities());
    }
}
