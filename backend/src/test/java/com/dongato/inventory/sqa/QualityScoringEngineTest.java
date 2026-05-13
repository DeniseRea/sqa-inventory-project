package com.dongato.inventory.sqa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class QualityScoringEngineTest {

    @Test
    @DisplayName("Debe retornar 100 cuando la calidad es perfecta")
    void shouldReturnPerfectScore() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 0, 0, 0);
        assertEquals(100.0, report.score(), 0.001);
        assertEquals("PASSED", report.status());
        assertEquals("Quality standards met. Continue with continuous monitoring.",
                report.recommendations().get(0));
    }

    @Test
    @DisplayName("Debe fallar (FAILED) si existen vulnerabilidades y el score baja de 80")
    void shouldFailIfVulnerabilitiesExistAndScoreIsLow() {
        // Score: (70*0.3) + (100*0.3) + (100*0.2) + (0*0.2) = 21 + 30 + 20 + 0 = 71
        QualityReport report = QualityScoringEngine.calculateReport(70.0, 0, 0, 1);
        assertTrue(report.score() < 80.0);
        assertEquals("FAILED", report.status());
    }

    @Test
    @DisplayName("Debe penalizar correctamente los Code Smells")
    void shouldPenalizeCodeSmells() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 0, 10, 0);
        // Maintainability: 100 - (10 * 2) = 80
        // Score: (100*0.3) + (100*0.3) + (80*0.2) + (100*0.2) = 30 + 30 + 16 + 20 = 96
        assertEquals(96.0, report.score(), 0.001);
    }

    @Test
    @DisplayName("Debe penalizar severamente los Bugs")
    void shouldPenalizeBugsSeverely() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 5, 0, 0);
        // Correctness: 100 - (5 * 10) = 50
        // Score: (100*0.3) + (50*0.3) + (100*0.2) + (100*0.2) = 30 + 15 + 20 + 20 = 85
        assertEquals(85.0, report.score(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular el score directamente sin construir reporte")
    void shouldCalculateScoreDirectly() {
        double score = QualityScoringEngine.calculateScore(95.0, 1, 5, 0);

        assertEquals(93.5, score, 0.001);
    }

    @Test
    @DisplayName("Debe aprobar cuando el score queda exactamente en el umbral minimo")
    void shouldPassAtMinimumScoreThreshold() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 0, 50, 0);

        assertEquals(80.0, report.score(), 0.001);
        assertEquals("PASSED", report.status());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 100.1})
    @DisplayName("Debe rechazar porcentajes de cobertura fuera del rango 0-100")
    void shouldRejectInvalidCoverage(double coverage) {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(coverage, 0, 0, 0));
    }

    @ParameterizedTest
    @CsvSource({
            "90.0, -1, 0, 0",
            "90.0, 0, -1, 0",
            "90.0, 0, 0, -1"
    })
    @DisplayName("Debe rechazar metricas negativas")
    void shouldRejectNegativeMetricCounts(double coverage, int bugs, int smells, int vulnerabilities) {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(coverage, bugs, smells, vulnerabilities));
    }

    @Test
    @DisplayName("Debe generar recomendaciones para cada factor de McCall afectado")
    void shouldGenerateRecommendationsForAllAffectedFactors() {
        QualityReport report = QualityScoringEngine.calculateReport(70.0, 1, 11, 1);

        assertEquals(4, report.recommendations().size());
        assertTrue(report.recommendations().get(0).contains("CRITICAL"));
        assertTrue(report.recommendations().get(1).contains("HIGH"));
        assertTrue(report.recommendations().get(2).contains("MEDIUM"));
        assertTrue(report.recommendations().get(3).contains("LOW"));
    }

    @ParameterizedTest
    @CsvSource({
            "95.0, A (Excellent)",
            "85.0, B (Good)",
            "75.0, C (Acceptable)",
            "65.0, D (Needs Improvement)",
            "55.0, F (Critical)"
    })
    @DisplayName("Debe clasificar el score por rangos")
    void shouldReturnExpectedGrade(double score, String expectedGrade) {
        assertEquals(expectedGrade, QualityScoringEngine.getGrade(score));
    }
}
