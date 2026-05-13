package com.dongato.inventory.sqa;

import java.util.ArrayList;
import java.util.List;

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

    private static final double MIN_ACCEPTABLE_SCORE = 80.0;
    private static final double MIN_ACCEPTABLE_COVERAGE = 85.0;

    private QualityScoringEngine() {
        // Utility class — prevent instantiation
    }

    /**
     * Calculates the overall quality score (0-100).
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
     * Calculates the quality report with recommendations.
     */
    public static QualityReport calculateReport(double coverage, int bugs, int smells, int vulnerabilities) {
        double score = calculateScore(coverage, bugs, smells, vulnerabilities);
        String status = (score >= MIN_ACCEPTABLE_SCORE) ? "PASSED" : "FAILED";
        List<String> recommendations = generateRecommendations(coverage, bugs, smells, vulnerabilities);

        return new QualityReport(
            coverage,
            bugs,
            smells,
            vulnerabilities,
            score,
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
