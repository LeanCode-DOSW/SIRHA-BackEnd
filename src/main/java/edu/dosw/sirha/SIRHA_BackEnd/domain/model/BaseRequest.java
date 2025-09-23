package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

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
 * @author Equipo SIRHA
 * @version 1.0
 * @since 2023-12-01
 * 
 * @see RequestState
 * @see Request
 * @see CambioGrupo
 * @see CambioMateria
 */
public abstract class BaseRequest implements Request {
    
    /**
     * Identificador único de la solicitud en el sistema.
     * Generado automáticamente por la base de datos.
     */
    private String id;
    
    /**
     * Nivel de prioridad de la solicitud.
     * Valores más altos indican mayor prioridad.
     * Rango típico: 1 (baja) a 5 (crítica).
     */
    private int prioridad;
    
    /**
     * Estado actual de la solicitud.
     * Controla el flujo de procesamiento de la solicitud.
     */
    private RequestState estado;
    
    /**
     * Timestamp de cuándo fue creada la solicitud.
     * Se asigna automáticamente en el constructor.
     */
    private LocalDateTime creadoEn;

    /**
     * Constructor base para todas las solicitudes.
     * 
     * Inicializa una solicitud con una prioridad específica y establece
     * automáticamente el estado como PENDIENTE y el timestamp de creación.
     * 
     * @param prioridad nivel de prioridad de la solicitud.
     *                 Debe estar en el rango 1-5 donde:
     *                 1 = Baja prioridad
     *                 2 = Normal 
     *                 3 = Media
     *                 4 = Alta
     *                 5 = Crítica/Urgente
     * @throws IllegalArgumentException si la prioridad está fuera del rango válido
     * 
     * @example
     * <pre>
     * public class CambioGrupo extends BaseRequest {
     *     public CambioGrupo(int prioridad, String grupoOrigen, String grupoDestino) {
     *         super(prioridad);  // Inicializa con prioridad específica
     *         // ... inicialización específica del cambio de grupo
     *     }
     * }
     * </pre>
     */
    public BaseRequest(int prioridad) {
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
        
        this.prioridad = prioridad;
        this.estado = RequestState.PENDIENTE;
        this.creadoEn = LocalDateTime.now();
    }

    /**
     * Método abstracto para aprobar la solicitud.
     * 
     * Cada tipo de solicitud debe implementar su lógica específica de aprobación.
     * La implementación debe:
     * 1. Validar que la solicitud se puede aprobar
     * 2. Realizar los cambios correspondientes en el sistema
     * 3. Cambiar el estado a APROBADA
     * 4. Notificar al estudiante del resultado
     * 
     * @throws IllegalStateException si la solicitud no se puede aprobar en su estado actual
     * @throws RuntimeException si ocurre un error durante el procesamiento
     * 
     * @implNote Las clases concretas deben validar el estado antes de procesar
     */
    public abstract void aprobar();

    /**
     * Método abstracto para rechazar la solicitud.
     * 
     * Cada tipo de solicitud debe implementar su lógica específica de rechazo.
     * La implementación debe:
     * 1. Establecer la razón del rechazo
     * 2. Cambiar el estado a RECHAZADA
     * 3. Notificar al estudiante con la razón del rechazo
     * 4. Registrar el rechazo para auditoría
     * 
     * @throws IllegalStateException si la solicitud no se puede rechazar en su estado actual
     * 
     * @implNote Las clases concretas deben proporcionar razones específicas del rechazo
     */
    public abstract void rechazar();

    /**
     * Verifica si la solicitud puede ser procesada (aprobada o rechazada).
     * 
     * @return true si está en estado PENDIENTE, false en caso contrario
     */
    public boolean puedeSerProcesada() {
        return estado == RequestState.PENDIENTE;
    }

    /**
     * Verifica si la solicitud está en estado final (aprobada o rechazada).
     * 
     * @return true si está aprobada o rechazada, false si está pendiente
     */
    public boolean estaFinalizada() {
        return estado == RequestState.APROBADA || estado == RequestState.RECHAZADA;
    }

    /**
     * Calcula la edad de la solicitud en días.
     * 
     * @return número de días desde que se creó la solicitud
     */
    public long getDiasDesdeCreacion() {
        return java.time.temporal.ChronoUnit.DAYS.between(creadoEn.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Verifica si la solicitud es urgente basada en su prioridad.
     * 
     * @return true si la prioridad es 4 o 5 (alta/crítica)
     */
    public boolean esUrgente() {
        return prioridad >= 4;
    }

    // Getters y Setters con documentación

    /**
     * Obtiene el identificador único de la solicitud.
     * @return ID de la solicitud, puede ser null si no se ha persistido
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador de la solicitud.
     * Normalmente usado por el sistema de persistencia.
     * 
     * @param id nuevo identificador único
     */
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
        if (!puedeSerProcesada()) {
            throw new IllegalStateException("No se puede cambiar la prioridad de una solicitud finalizada");
        }
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
     * Compara solicitudes para determinar igualdad basada en ID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BaseRequest request = (BaseRequest) obj;
        return Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
