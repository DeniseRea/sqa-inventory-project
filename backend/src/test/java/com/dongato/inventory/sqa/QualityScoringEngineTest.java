package com.dongato.inventory.sqa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QualityScoringEngine.
 * McCall Factor: Testability — validates quality scoring calculations.
 */
class QualityScoringEngineTest {

    @Test
    @DisplayName("Should return perfect score with ideal metrics")
    void shouldReturnPerfectScore() {
        double score = QualityScoringEngine.calculateScore(100.0, 0, 0, 0);
        assertEquals(100.0, score, 0.01);
        assertEquals("A (Excellent)", QualityScoringEngine.getGrade(score));
    }

    @Test
    @DisplayName("Should return zero integrity score with vulnerabilities")
    void shouldPenalizeVulnerabilities() {
        double withVulns = QualityScoringEngine.calculateScore(100.0, 0, 0, 1);
        double withoutVulns = QualityScoringEngine.calculateScore(100.0, 0, 0, 0);

        assertTrue(withVulns < withoutVulns);
        assertEquals(80.0, withVulns, 0.01); // 100*0.3 + 100*0.3 + 100*0.2 + 0*0.2
    }

    @Test
    @DisplayName("Should degrade correctness score with bugs")
    void shouldDegradeWithBugs() {
        double score = QualityScoringEngine.calculateScore(100.0, 5, 0, 0);
        // Correctness: max(100 - 50, 0) = 50
        // 100*0.3 + 50*0.3 + 100*0.2 + 100*0.2 = 30+15+20+20 = 85
        assertEquals(85.0, score, 0.01);
    }

    @Test
    @DisplayName("Should degrade maintainability with code smells")
    void shouldDegradeWithSmells() {
        double score = QualityScoringEngine.calculateScore(100.0, 0, 25, 0);
        // Maintainability: max(100 - 50, 0) = 50
        // 100*0.3 + 100*0.3 + 50*0.2 + 100*0.2 = 30+30+10+20 = 90
        assertEquals(90.0, score, 0.01);
    }

    @Test
    @DisplayName("Should throw on invalid coverage")
    void shouldThrowOnInvalidCoverage() {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(-1.0, 0, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(101.0, 0, 0, 0));
    }

    @Test
    @DisplayName("Should throw on negative metrics")
    void shouldThrowOnNegativeMetrics() {
        assertThrows(IllegalArgumentException.class,
                () -> QualityScoringEngine.calculateScore(80.0, -1, 0, 0));
    }

    @Test
    @DisplayName("Should grade correctly at boundaries")
    void shouldGradeCorrectly() {
        assertEquals("A (Excellent)", QualityScoringEngine.getGrade(90));
        assertEquals("B (Good)", QualityScoringEngine.getGrade(85));
        assertEquals("C (Acceptable)", QualityScoringEngine.getGrade(75));
        assertEquals("D (Needs Improvement)", QualityScoringEngine.getGrade(65));
        assertEquals("F (Critical)", QualityScoringEngine.getGrade(50));
    }
}
