package edu.dosw.sirha.sirha_backend.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String path;
    private String error;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(LocalDateTime timestamp, String path, String error, String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
