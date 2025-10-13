package edu.dosw.sirha.sirha_backend.infrastructure.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.dosw.sirha.sirha_backend.dto.ApiErrorResponse;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja todas las excepciones SirhaException (checked exceptions del dominio)
     */
    @ExceptionHandler(SirhaException.class)
    public ResponseEntity<ApiErrorResponse> handleSirhaException(SirhaException ex, HttpServletRequest request) {
        log.error("SIRHA exception: {} - {}", ex.getCode(), ex.getMessage());

        HttpStatus status = determineHttpStatus(ex.getCode());

        ApiErrorResponse error = new ApiErrorResponse(
            status,
            ex.getCode(),
            ex.getMessage(),
            "Error de lógica de negocio en SIRHA.",
            request.getRequestURI()
        );

        addSuggestion(error, ex.getCode());
        return new ResponseEntity<>(error, status);
    }

    /**
     * Maneja RuntimeExceptions que pueden envolver SirhaExceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        
        // Verificar si es una SirhaException envuelta
        if (ex.getCause() instanceof SirhaException sirhaEx) {
            log.error("Wrapped SIRHA exception: {} - {}", sirhaEx.getCode(), sirhaEx.getMessage());
            
            HttpStatus status = determineHttpStatus(sirhaEx.getCode());
            
            ApiErrorResponse error = new ApiErrorResponse(
                status,
                sirhaEx.getCode(),
                sirhaEx.getMessage(),
                "Error de lógica de negocio en SIRHA.",
                request.getRequestURI()
            );
            
            addSuggestion(error, sirhaEx.getCode());
            return new ResponseEntity<>(error, status);
        }
        
        // Manejar otras RuntimeExceptions
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        
        ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_ERROR",
            "Ha ocurrido un error interno en el servidor.",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        error.setSuggestion("Intente nuevamente más tarde o contacte soporte técnico.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja IllegalArgumentException
     */
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

    /**
     * Maneja excepciones genéricas no capturadas por otros handlers
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        
        // Verificar si es una SirhaException envuelta en Exception
        if (ex.getCause() instanceof SirhaException sirhaEx) {
            log.error("Exception wrapping SIRHA exception: {} - {}", sirhaEx.getCode(), sirhaEx.getMessage());
            
            HttpStatus status = determineHttpStatus(sirhaEx.getCode());
            
            ApiErrorResponse error = new ApiErrorResponse(
                status,
                sirhaEx.getCode(),
                sirhaEx.getMessage(),
                "Error de lógica de negocio en SIRHA.",
                request.getRequestURI()
            );
            
            addSuggestion(error, sirhaEx.getCode());
            return new ResponseEntity<>(error, status);
        }
        
        // Manejar excepciones completamente inesperadas
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

    /**
     * Determina el HttpStatus basado en el código de error
     */
    private HttpStatus determineHttpStatus(String errorCode) {
        return switch (errorCode) {
            // 404 Not Found - Recursos no encontrados
            case "STUDENT_NOT_FOUND", "SUBJECT_NOT_FOUND", "GROUP_NOT_FOUND", 
                 "REQUEST_NOT_FOUND", "ACADEMIC_PERIOD_NOT_FOUND" -> 
                HttpStatus.NOT_FOUND;
            
            // 409 Conflict - Conflictos de estado o datos
            case "REQUEST_ALREADY_APPROVED", "REQUEST_ALREADY_REJECTED", "REQUEST_ALREADY_PENDING",
                 "REQUEST_ALREADY_IN_REVIEW", "INVALID_STATE_TRANSITION", "EMAIL_ALREADY_EXISTS",
                 "CODE_ALREADY_EXISTS", "USERNAME_ALREADY_EXISTS", "DUPLICATE_REQUEST",
                 "SCHEDULE_CONFLICT" -> 
                HttpStatus.CONFLICT;
            
            // 400 Bad Request - Datos inválidos o errores de validación
            case "INVALID_CREDENTIALS", "VALIDATION_ERROR", "INVALID_ARGUMENT", 
                 "MISSING_REQUIRED_FIELD", "REQUEST_NOT_IN_REVIEW" -> 
                HttpStatus.BAD_REQUEST;
            
            // 403 Forbidden - Permisos insuficientes
            case "INSUFFICIENT_PERMISSIONS", "OPERATION_NOT_ALLOWED" -> 
                HttpStatus.FORBIDDEN;
            
            // 502 Bad Gateway - Errores de servicios externos
            case "EXTERNAL_SERVICE_ERROR" -> 
                HttpStatus.BAD_GATEWAY;
            
            // 500 Internal Server Error - Errores del sistema
            case "INTERNAL_ERROR", "DATABASE_ERROR" -> 
                HttpStatus.INTERNAL_SERVER_ERROR;
            
            // Default para códigos no reconocidos
            default -> {
                log.warn("Unrecognized error code: {}, defaulting to INTERNAL_SERVER_ERROR", errorCode);
                yield HttpStatus.INTERNAL_SERVER_ERROR;
            }
        };
    }

    /**
     * Añade sugerencias específicas basadas en el código de error
     */
    private void addSuggestion(ApiErrorResponse error, String code) {
        String suggestion = switch (code) {
            // Errores de recursos no encontrados
            case "STUDENT_NOT_FOUND", "SUBJECT_NOT_FOUND", "GROUP_NOT_FOUND", 
                 "REQUEST_NOT_FOUND", "ACADEMIC_PERIOD_NOT_FOUND" -> 
                "Verifique que el recurso existe y los parámetros son correctos.";
            
            // Errores de transición de estado
            case "REQUEST_ALREADY_APPROVED" -> 
                "Esta solicitud ya fue aprobada previamente. No se requiere acción adicional.";
                
            case "REQUEST_ALREADY_REJECTED" -> 
                "Esta solicitud ya fue rechazada. Puede crear una nueva solicitud si es necesario.";
                
            case "INVALID_STATE_TRANSITION" -> 
                "La transición de estado solicitada no es válida para el estado actual de la solicitud.";
            
            // Errores de conflictos de datos
            case "EMAIL_ALREADY_EXISTS" -> 
                "Use un email diferente que no esté registrado en el sistema.";
                
            case "CODE_ALREADY_EXISTS" -> 
                "Use un código estudiantil diferente que no esté registrado.";
                
            case "USERNAME_ALREADY_EXISTS" -> 
                "Elija un nombre de usuario diferente que esté disponible.";
                
            case "DUPLICATE_REQUEST" -> 
                "Ya existe una solicitud similar. Revise sus solicitudes pendientes.";
            
            // Errores de validación
            case "INVALID_CREDENTIALS" -> 
                "Verifique que su usuario y contraseña sean correctos.";
                
            case "VALIDATION_ERROR", "INVALID_ARGUMENT" -> 
                "Revise que todos los campos estén correctamente completados.";
                
            case "MISSING_REQUIRED_FIELD" -> 
                "Complete todos los campos obligatorios antes de continuar.";
            
            // Errores de permisos
            case "INSUFFICIENT_PERMISSIONS" -> 
                "Contacte al administrador del sistema para obtener los permisos necesarios.";
                
            case "OPERATION_NOT_ALLOWED" -> 
                "Esta operación no está permitida en las circunstancias actuales.";
            
            // Errores de horarios
            case "SCHEDULE_CONFLICT" -> 
                "Existe un conflicto de horarios. Seleccione un horario diferente.";
            
            // Errores técnicos
            case "EXTERNAL_SERVICE_ERROR" -> 
                "Problema con un servicio externo. Intente nuevamente en unos minutos.";
                
            case "DATABASE_ERROR" -> 
                "Error de base de datos. Contacte soporte técnico si persiste.";
            
            // Default
            default -> "Consulte la documentación de la API para más información.";
        };
        error.setSuggestion(suggestion);
    }
}