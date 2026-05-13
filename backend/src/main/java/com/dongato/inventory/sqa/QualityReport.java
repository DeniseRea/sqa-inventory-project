package com.dongato.inventory.sqa;

import java.util.List;

/**
 * Representa el resultado de la evaluación de calidad basada en el Modelo de McCall.
 *
 * @param coverage Cobertura de código (0-100).
 * @param bugs Número de defectos funcionales.
 * @param smells Número de problemas de mantenibilidad.
 * @param vulnerabilities Número de fallos de seguridad detectados.
 * @param score Puntaje final calculado (0-100).
 * @param status Estado final (PASSED si score >= 80, de lo contrario FAILED).
 * @param recommendations Lista de acciones sugeridas para la mejora continua.
 */
public record QualityReport(
    double coverage,
    int bugs,
    int smells,
    int vulnerabilities,
    double score,
    String status,
    List<String> recommendations
) {}
