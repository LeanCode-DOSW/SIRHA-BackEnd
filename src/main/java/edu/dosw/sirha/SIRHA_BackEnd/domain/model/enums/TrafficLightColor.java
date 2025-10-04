package edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums;

/**
 * Enumeración que representa el sistema de semáforo académico
 * dentro de la plataforma SIRHA.
 *
 * Cada color indica el estado actual de una materia:
 * - GREEN: Materia aprobada.
 * - YELLOW: Materia en curso.
 * - RED: Materia reprobada.
 * - GRAY: Materia no cursada.
 */
public enum TrafficLightColor {
    GREEN,    // Materia aprobada
    YELLOW,   // Materia en curso
    RED,      // Materia reprobada
    GRAY      // Materia no cursada
}
