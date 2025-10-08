package edu.dosw.sirha.SIRHA_BackEnd.infrastructure.handler;

import edu.dosw.sirha.SIRHA_BackEnd.domain.exception.DomainException;
import edu.dosw.sirha.SIRHA_BackEnd.dto.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(DomainException ex, HttpServletRequest request) {
        log.error("Domain exception: {} - {}", ex.getCode(), ex.getMessage());

        HttpStatus status = switch (ex.getCode()) {
            case "RESOURCE_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "CONFLICT" -> HttpStatus.CONFLICT;
            case "VALIDATION_ERROR" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ApiErrorResponse error = new ApiErrorResponse(
            status,
            ex.getCode(),
            ex.getMessage(),
            "Consulte la documentación del error.",
            request.getRequestURI()
        );

        // Añadir sugerencias específicas
        addSuggestion(error, ex.getCode());

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument exception: {}", ex.getMessage());

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            "INVALID_ARGUMENT",
            ex.getMessage(),
            "Los datos proporcionados no son válidos.",
            request.getRequestURI()
        );

        error.setSuggestion("Verifique los parámetros enviados en la solicitud.");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception", ex);

        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "UNEXPECTED_ERROR",
            "Ha ocurrido un error inesperado en el servidor.",
            ex.getMessage(),
            request.getRequestURI()
        );

        error.setSuggestion("Intente nuevamente más tarde o contacte soporte técnico.");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addSuggestion(ApiErrorResponse error, String code) {
        String suggestion = switch (code) {
            case "RESOURCE_NOT_FOUND" -> "Verifique que el recurso solicitado existe y los parámetros son correctos.";
            case "CONFLICT" -> "El recurso ya existe o hay un conflicto con el estado actual.";
            case "VALIDATION_ERROR" -> "Revise que todos los datos requeridos estén presentes y sean válidos.";
            default -> "Consulte la documentación de la API para más información.";
        };
        error.setSuggestion(suggestion);
    }
}