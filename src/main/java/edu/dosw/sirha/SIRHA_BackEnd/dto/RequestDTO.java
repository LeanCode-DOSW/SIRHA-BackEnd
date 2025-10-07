package edu.dosw.sirha.SIRHA_BackEnd.dto;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestTo;
import java.time.LocalDateTime;

public class RequestDTO {
    private String id;
    private int prioridad;
    private LocalDateTime fecha;
    private RequestTo estado;

    // Getters & setters
}