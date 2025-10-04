package edu.dosw.sirha.SIRHA_BackEnd.dto;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestState;
import java.time.LocalDateTime;

/**
 * DTO para representar una solicitud académica en las respuestas o peticiones
 * del sistema SIRHA.
 *
 * Este objeto es utilizado para transferir datos entre el backend y la API,
 * evitando exponer directamente las entidades del dominio.
 *
 * Contiene información básica de la solicitud:
 * - Identificador único
 * - Nivel de prioridad
 * - Fecha de creación
 * - Estado actual (pendiente, aprobada, rechazada, en revisión)
 *
 * @see RequestState
 */
public class RequestDTO {
    private String id;
    private int prioridad;
    private LocalDateTime fecha;
    private RequestState estado;

    public RequestDTO() {}

    public RequestDTO(String id, int prioridad, LocalDateTime fecha, RequestState estado) {
        this.id = id;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public RequestState getEstado() { return estado; }
    public void setEstado(RequestState estado) { this.estado = estado; }
}
