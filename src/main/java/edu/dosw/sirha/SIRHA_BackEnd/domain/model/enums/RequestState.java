package edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums;

/**
 * Enumeración que representa los posibles estados de una solicitud académica
 * dentro del sistema SIRHA.
 *
 * Estados:
 * - PENDING: La solicitud ha sido creada pero aún no ha sido procesada.
 * - APPROVED: La solicitud ha sido revisada y aceptada.
 * - REJECTED: La solicitud ha sido revisada y rechazada.
 * - UNDER_REVIEW: La solicitud está actualmente en proceso de evaluación.
 */
public enum RequestState {
    PENDING,
    APPROVED,
    REJECTED,
    UNDER_REVIEW
}

