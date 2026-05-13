package com.dongato.inventory.sqa;

/**
 * Quality Scoring Engine based on the McCall Quality Model.
 * <p>
 * Calculates a composite quality score from metrics collected during CI/CD.
 * Each factor is weighted according to project priorities.
 *
 * McCall Factors measured:
 * - Testability:      Code coverage percentage (JaCoCo)
 * - Correctness:      Bug count (SpotBugs)
 * - Maintainability:  Code smell count (SonarQube)
 * - Integrity:        Vulnerability count (OWASP Dependency Check)
 */
public class QualityScoringEngine {

    // Weights assigned based on project importance
    private static final double WEIGHT_CORRECTNESS     = 0.30;
    private static final double WEIGHT_TESTABILITY     = 0.30;
    private static final double WEIGHT_MAINTAINABILITY = 0.20;
    private static final double WEIGHT_INTEGRITY       = 0.20;

    private QualityScoringEngine() {
        // Utility class — prevent instantiation
    }

    /**
     * Calculates the overall quality score (0-100).
     *
     * @param coveragePercent code coverage percentage from JaCoCo
     * @param bugs            bug count from SpotBugs
     * @param smells          code smell count from SonarQube
     * @param vulns           vulnerability count from OWASP Dependency Check
     * @return composite quality score (0-100)
     */
    public static double calculateScore(double coveragePercent, int bugs, int smells, int vulns) {
        if (coveragePercent < 0 || coveragePercent > 100) {
            throw new IllegalArgumentException("Coverage must be between 0 and 100");
        }
        if (bugs < 0 || smells < 0 || vulns < 0) {
            throw new IllegalArgumentException("Metric counts cannot be negative");
        }

        double testabilityScore      = Math.min(coveragePercent, 100.0);
        double correctnessScore      = Math.max(100.0 - (bugs * 10.0), 0.0);
        double maintainabilityScore  = Math.max(100.0 - (smells * 2.0), 0.0);
        double integrityScore        = vulns == 0 ? 100.0 : 0.0; // Zero tolerance

        return (testabilityScore     * WEIGHT_TESTABILITY)     +
               (correctnessScore     * WEIGHT_CORRECTNESS)     +
               (maintainabilityScore * WEIGHT_MAINTAINABILITY) +
               (integrityScore       * WEIGHT_INTEGRITY);
    }

    /**
     * Returns a human-readable quality grade.
     */
    public static String getGrade(double score) {
        if (score >= 90) return "A (Excellent)";
        if (score >= 80) return "B (Good)";
        if (score >= 70) return "C (Acceptable)";
        if (score >= 60) return "D (Needs Improvement)";
        return "F (Critical)";
    }
}
