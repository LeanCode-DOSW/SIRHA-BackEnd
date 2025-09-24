package edu.dosw.sirha.SIRHA_BackEnd.dto;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.Request;
import java.time.LocalDateTime;

public class RequestDTO {
    private String id;
    private int prioridad;
    private LocalDateTime fecha;
    private Request estado;

    // Getters & setters
}