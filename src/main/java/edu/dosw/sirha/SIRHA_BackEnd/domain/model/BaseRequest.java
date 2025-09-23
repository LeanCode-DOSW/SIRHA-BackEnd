package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

/**
 * Clase abstracta base para todas las solicitudes académicas en el sistema SIRHA.
 * 
 * Esta clase define la estructura común y comportamiento básico para todos los tipos
 * de solicitudes que pueden realizar los estudiantes, como cambios de grupo,
 * cambios de materia, solicitudes de reinscripción, etc.
 * 
 * Implementa el patrón Template Method, donde define la estructura común de las
 * solicitudes y delega la implementación específica de aprobar() y rechazar()
 * a las clases concretas.
 * 
 * Características principales:
 * - Sistema de prioridades para ordenamiento de solicitudes
 * - Estados bien definidos (PENDIENTE, APROBADA, RECHAZADA)
 * - Timestamp automático de creación
 * - Interface común para todas las solicitudes
 * 
 * Estados de solicitud:
 * - PENDIENTE: Solicitud creada, esperando revisión
 * - APROBADA: Solicitud aceptada y procesada
 * - RECHAZADA: Solicitud denegada con razón específica
 * 
 * @see RequestState
 * @see Request
 * @see CambioGrupo
 * @see CambioMateria
 */
public abstract class BaseRequest implements Request {
    private String id;
    private int prioridad;
    private RequestState estado;
    private LocalDateTime creadoEn;

    /**
     * Constructor base para todas las solicitudes.
     * 
     * Inicializa una solicitud con una prioridad específica y establece
     * automáticamente el estado como PENDIENTE y el timestamp de creación.
     * 
     * @param prioridad nivel de prioridad de la solicitud.
     */
    public BaseRequest(int prioridad) {
        this.prioridad = prioridad;
        this.estado = RequestState.PENDIENTE;
        this.creadoEn = LocalDateTime.now();
    }


    public abstract void aprobar();
    public abstract void rechazar();

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nivel de prioridad de la solicitud.
     * @return prioridad en escala 1-5
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Establece el nivel de prioridad de la solicitud.
     * Solo se permite cambiar si la solicitud está pendiente.
     * 
     * @param prioridad nueva prioridad (1-5)
     * @throws IllegalArgumentException si la prioridad está fuera del rango
     * @throws IllegalStateException si la solicitud no está pendiente
     */
    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
        /*
        if (!puedeSerProcesada()) {
            throw new IllegalStateException("No se puede cambiar la prioridad de una solicitud finalizada");
        }
            */
        this.prioridad = prioridad;
    }

    /**
     * Obtiene el estado actual de la solicitud.
     * @return estado de la solicitud
     */
    public RequestState getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la solicitud.
     * Usado internamente por los métodos aprobar() y rechazar().
     * 
     * @param estado nuevo estado de la solicitud
     * @throws IllegalArgumentException si el estado es null
     */
    protected void setEstado(RequestState estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        this.estado = estado;
    }

    /**
     * Obtiene la fecha y hora de creación de la solicitud.
     * @return timestamp de creación
     */
    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    /**
     * Establece la fecha de creación de la solicitud.
     * Usado para pruebas o migración de datos.
     * 
     * @param creadoEn nueva fecha de creación
     * @throws IllegalArgumentException si la fecha es null o futura
     */
    public void setCreadoEn(LocalDateTime creadoEn) {
        if (creadoEn == null) {
            throw new IllegalArgumentException("La fecha de creación no puede ser null");
        }
        if (creadoEn.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede ser futura");
        }
        this.creadoEn = creadoEn;
    }

    /**
     * Representación en string de la solicitud con información básica.
     */
    @Override
    public String toString() {
        return String.format("%s{id='%s', prioridad=%d, estado=%s, creadoEn=%s}", 
                            getClass().getSimpleName(), id, prioridad, estado, creadoEn);
    }
}
