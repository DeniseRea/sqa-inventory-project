package com.sqa_inventory.tasks.sqa;

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

    /**
     * Calcula el puntaje de calidad y genera un reporte detallado.
     */
    public static QualityReport calculateReport(double coverage, int bugs, int smells, int vulnerabilities) {
        double testabilityScore = Math.clamp(coverage, 0.0, 100.0);
        double correctnessScore = Math.max(100.0 - (bugs * 10.0), 0.0);
        double maintainabilityScore = Math.max(100.0 - (smells * 2.0), 0.0);
        double integrityScore = (vulnerabilities == 0) ? 100.0 : 0.0;

        double finalScore = (testabilityScore * WEIGHT_TESTABILITY) +
                            (correctnessScore * WEIGHT_CORRECTNESS) +
                            (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
                            (integrityScore * WEIGHT_INTEGRITY);

        String status = (finalScore >= MIN_ACCEPTABLE_SCORE) ? "PASSED" : "FAILED";

        return new QualityReport(
            testabilityScore,
            bugs,
            smells,
            vulnerabilities,
            finalScore,
            status
        );
    }
}
