package edu.dosw.sirha.sirha_backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class ApiErrorResponse {
    private String status; // "error"
    private int statusCode;
    private String code; // código interno, ej. "RESOURCE_NOT_FOUND"
    private String message; // descripción
    private String details; // más contexto
    private String path; // endpoint
    private LocalDateTime timestamp;
    private String requestId; // UUID generado
    private String suggestion; // sugerencia opcional

    public ApiErrorResponse(HttpStatus status, String code, String message, String details, String path) {
        this.status = "error";
        this.statusCode = status.value();
        this.code = code;
        this.message = message;
        this.details = details;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.requestId = UUID.randomUUID().toString();
    }

    // Getters y setters
    public String getStatus() { return status; }
    public int getStatusCode() { return statusCode; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRequestId() { return requestId; }
    public String getSuggestion() { return suggestion; }
    
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
}