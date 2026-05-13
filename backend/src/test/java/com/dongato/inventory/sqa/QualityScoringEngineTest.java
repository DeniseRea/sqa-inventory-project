package com.dongato.inventory.sqa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QualityScoringEngineTest {

    @Test
    @DisplayName("Debe retornar 100 cuando la calidad es perfecta")
    void shouldReturnPerfectScore() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 0, 0, 0);
        assertEquals(100.0, report.score());
        assertEquals("PASSED", report.status());
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
        assertEquals(96.0, report.score());
    }

    @Test
    @DisplayName("Debe penalizar severamente los Bugs")
    void shouldPenalizeBugsSeverely() {
        QualityReport report = QualityScoringEngine.calculateReport(100.0, 5, 0, 0);
        // Correctness: 100 - (5 * 10) = 50
        // Score: (100*0.3) + (50*0.3) + (100*0.2) + (100*0.2) = 30 + 15 + 20 + 20 = 85
        assertEquals(85.0, report.score());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la cobertura está fuera de rango")
    void shouldThrowWhenCoverageOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(-1.0, 0, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(101.0, 0, 0, 0));
    }

    @Test
    @DisplayName("Debe lanzar excepción si las métricas son negativas")
    void shouldThrowWhenNegativeMetrics() {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(90.0, -1, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(90.0, 0, -2, 0));
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(90.0, 0, 0, -3));
    }

    @Test
    @DisplayName("Debe generar recomendaciones para métricas críticas")
    void shouldGenerateRecommendationsForIssues() {
        QualityReport report = QualityScoringEngine.calculateReport(70.0, 1, 12, 1);

        assertTrue(report.recommendations().stream().anyMatch(rec -> rec.contains("CRITICAL")));
        assertTrue(report.recommendations().stream().anyMatch(rec -> rec.contains("HIGH")));
        assertTrue(report.recommendations().stream().anyMatch(rec -> rec.contains("MEDIUM")));
        assertTrue(report.recommendations().stream().anyMatch(rec -> rec.contains("LOW")));
    }

    @Test
    @DisplayName("Debe devolver recomendación positiva cuando no hay hallazgos")
    void shouldReturnPositiveRecommendationWhenNoFindings() {
        QualityReport report = QualityScoringEngine.calculateReport(95.0, 0, 0, 0);

        assertEquals(1, report.recommendations().size());
        assertEquals("Quality standards met. Continue with continuous monitoring.",
                report.recommendations().get(0));
    }

    @Test
    @DisplayName("Debe asignar grado correcto según el puntaje")
    void shouldReturnCorrectGrade() {
        assertEquals("A (Excellent)", QualityScoringEngine.getGrade(95.0));
        assertEquals("B (Good)", QualityScoringEngine.getGrade(85.0));
        assertEquals("C (Acceptable)", QualityScoringEngine.getGrade(75.0));
        assertEquals("D (Needs Improvement)", QualityScoringEngine.getGrade(65.0));
        assertEquals("F (Critical)", QualityScoringEngine.getGrade(50.0));
    }
}
