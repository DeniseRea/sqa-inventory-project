package com.sqa_inventory.tasks.sqa;

/**
 * Representa el resultado de la evaluación de calidad basada en el Modelo de McCall.
 * Implementado como un record de Java 25 para inmutabilidad y concisión.
 *
 * @param coverage Cobertura de código (0-100).
 * @param bugs Número de defectos funcionales.
 * @param smells Número de problemas de mantenibilidad.
 * @param vulnerabilities Número de fallos de seguridad detectados.
 * @param score Puntaje final calculado (0-100).
 * @param status Estado final (PASSED si score >= 80, de lo contrario FAILED).
 */
public record QualityReport(
    double coverage,
    int bugs,
    int smells,
    int vulnerabilities,
    double score,
    String status
) {}
