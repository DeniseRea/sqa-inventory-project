package com.sqa_inventory.tasks.sqa;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de cálculo de calidad basado en el Modelo de McCall.
 * Evalúa Factores de Operación, Revisión e Integridad.
 */
public class QualityScoringEngine {

    // Pesos definidos por los requerimientos del proyecto
    private static final double WEIGHT_CORRECTNESS = 0.3;   // Correctitud
    private static final double WEIGHT_TESTABILITY = 0.3;   // Testabilidad
    private static final double WEIGHT_MAINTAINABILITY = 0.2; // Mantenibilidad
    private static final double WEIGHT_INTEGRITY = 0.2;     // Integridad (Seguridad)

    private static final double MIN_ACCEPTABLE_SCORE = 80.0;
    private static final double MIN_ACCEPTABLE_COVERAGE = 85.0;

    private QualityScoringEngine() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Calcula el puntaje de calidad y genera un reporte detallado con recomendaciones (PDCA - ACT).
     */
    public static QualityReport calculateReport(double coverage, int bugs, int smells, int vulnerabilities) {
        double testabilityScore = Math.min(Math.max(coverage, 0.0), 100.0);
        double correctnessScore = Math.max(100.0 - (bugs * 10.0), 0.0);
        double maintainabilityScore = Math.max(100.0 - (smells * 2.0), 0.0);
        double integrityScore = (vulnerabilities == 0) ? 100.0 : 0.0;

        double finalScore = (testabilityScore * WEIGHT_TESTABILITY) +
                            (correctnessScore * WEIGHT_CORRECTNESS) +
                            (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
                            (integrityScore * WEIGHT_INTEGRITY);

        String status = (finalScore >= MIN_ACCEPTABLE_SCORE) ? "PASSED" : "FAILED";
        
        List<String> recommendations = generateRecommendations(coverage, bugs, smells, vulnerabilities);

        return new QualityReport(
            testabilityScore,
            bugs,
            smells,
            vulnerabilities,
            finalScore,
            status,
            recommendations
        );
    }

    private static List<String> generateRecommendations(double coverage, int bugs, int smells, int vulnerabilities) {
        List<String> recommendations = new ArrayList<>();

        if (vulnerabilities > 0) {
            recommendations.add("CRITICAL: Update dependencies and fix CVEs immediately (Integrity).");
        }
        if (bugs > 0) {
            recommendations.add("HIGH: Prioritize fixing functional defects to improve Correctness.");
        }
        if (coverage < MIN_ACCEPTABLE_COVERAGE) {
            recommendations.add("MEDIUM: Increase unit test coverage to reach at least 85% (Testability).");
        }
        if (smells > 10) {
            recommendations.add("LOW: Schedule a refactoring sprint to reduce technical debt (Maintainability).");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Quality standards met. Continue with continuous monitoring.");
        }

        return recommendations;
    }
}
