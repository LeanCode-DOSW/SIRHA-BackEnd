package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

/**
 * Clase abstracta base para todas las solicitudes académicas en el sistema SIRHA.
 *
 * Esta clase define la estructura y el comportamiento común de todas las solicitudes
 * que pueden realizar los estudiantes, tales como:
 * - Cambio de grupo
 * - Cambio de materia
 * - Reinscripción
 *
 * Implementa el patrón Template Method: define un flujo general
 * y delega la implementación específica de {@link #approve()} y {@link #reject()}
 * a las subclases concretas.
 *
 * Características principales:
 * - Sistema de prioridad para ordenar solicitudes
 * - Estados bien definidos (PENDING, APPROVED, REJECTED, UNDER_REVIEW)
 * - Asignación automática de la fecha de creación
 *
 * Estados posibles de la solicitud:
 * - PENDING: Solicitud creada y en espera de revisión
 * - APPROVED: Solicitud aceptada y procesada
 * - REJECTED: Solicitud rechazada con justificación
 * - UNDER_REVIEW: Solicitud actualmente en evaluación
 *
 * @see RequestState
 * @see Request
 */
public abstract class BaseRequest implements Request {
    private String id;
    private int priority;
    private RequestState state;
    private LocalDateTime createdAt;

    /**
     * Constructor base para inicializar una solicitud.
     *
     * @param priority nivel de prioridad de la solicitud (1-5).
     *                 Se asigna automáticamente el estado PENDING
     *                 y la fecha/hora actual como timestamp de creación.
     */
    public BaseRequest(int priority) {
        this.priority = priority;
        this.state = RequestState.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Aprueba la solicitud.
     * La lógica de aprobación debe ser implementada en las subclases.
     */
    public abstract void approve();

    /**
     * Rechaza la solicitud.
     * La lógica de rechazo debe ser implementada en las subclases.
     */
    public abstract void reject();

    // ---------- Getters & Setters ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la prioridad de la solicitud.
     *
     * @return valor de prioridad (1-5).
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Establece la prioridad de la solicitud.
     *
     * @param priority nueva prioridad (1-5).
     * @throws IllegalArgumentException si la prioridad está fuera del rango permitido.
     */
    public void setPriority(int priority) {
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
        this.priority = priority;
    }

    /**
     * Obtiene el estado actual de la solicitud.
     *
     * @return estado de la solicitud.
     */
    public RequestState getState() {
        return state;
    }

    /**
     * Cambia el estado de la solicitud.
     *
     * @param state nuevo estado.
     * @throws IllegalArgumentException si el estado es nulo.
     */
    protected void setState(RequestState state) {
        if (state == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.state = state;
    }

    /**
     * Obtiene la fecha y hora de creación de la solicitud.
     *
     * @return timestamp de creación.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece manualmente la fecha de creación de la solicitud.
     *
     * @param createdAt fecha a asignar.
     * @throws IllegalArgumentException si la fecha es nula o futura.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("La fecha de creación no puede ser nula");
        }
        if (createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede ser futura");
        }
        this.createdAt = createdAt;
    }

    /**
     * Representación en texto de la solicitud.
     *
     * @return cadena con id, prioridad, estado y fecha de creación.
     */
    @Override
    public String toString() {
        return String.format("%s{id='%s', priority=%d, state=%s, createdAt=%s}",
                getClass().getSimpleName(), id, priority, state, createdAt);
    }
}
