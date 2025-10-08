package edu.dosw.sirha.SIRHA_BackEnd.domain.exception;

public enum ErrorCode {
    // ========== REQUEST STATE ERRORS ==========
    REQUEST_ALREADY_APPROVED("REQUEST_ALREADY_APPROVED", "La solicitud ya está aprobada"),
    REQUEST_ALREADY_REJECTED("REQUEST_ALREADY_REJECTED", "La solicitud ya está rechazada"),
    REQUEST_ALREADY_PENDING("REQUEST_ALREADY_PENDING", "La solicitud ya está en estado pendiente"),
    REQUEST_ALREADY_IN_REVIEW("REQUEST_ALREADY_IN_REVIEW", "La solicitud ya está en revisión"),
    INVALID_STATE_TRANSITION("INVALID_STATE_TRANSITION", "Transición de estado inválida"),
    REQUEST_NOT_IN_REVIEW("REQUEST_NOT_IN_REVIEW", "La solicitud debe estar en revisión para esta operación"),
    
    // ========== RESOURCE NOT FOUND ERRORS ==========
    STUDENT_NOT_FOUND("STUDENT_NOT_FOUND", "Estudiante no encontrado"),
    SUBJECT_NOT_FOUND("SUBJECT_NOT_FOUND", "Materia no encontrada"),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", "Grupo no encontrado"),
    REQUEST_NOT_FOUND("REQUEST_NOT_FOUND", "Solicitud no encontrada"),
    ACADEMIC_PERIOD_NOT_FOUND("ACADEMIC_PERIOD_NOT_FOUND", "Período académico no encontrado"),
    
    // ========== VALIDATION ERRORS ==========
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Credenciales inválidas"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Error de validación de datos"),
    INVALID_ARGUMENT("INVALID_ARGUMENT", "Argumento inválido proporcionado"),
    MISSING_REQUIRED_FIELD("MISSING_REQUIRED_FIELD", "Campo requerido faltante"),
    
    // ========== CONFLICT ERRORS ==========
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "El email ya está registrado"),
    CODE_ALREADY_EXISTS("CODE_ALREADY_EXISTS", "El código estudiantil ya está registrado"),
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS", "El nombre de usuario ya existe"),
    DUPLICATE_REQUEST("DUPLICATE_REQUEST", "Ya existe una solicitud similar"),
    
    // ========== BUSINESS LOGIC ERRORS ==========
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS", "Permisos insuficientes para realizar esta operación"),
    OPERATION_NOT_ALLOWED("OPERATION_NOT_ALLOWED", "Operación no permitida en el estado actual"),
    SCHEDULE_CONFLICT("SCHEDULE_CONFLICT", "Conflicto de horarios detectado"),
    
    // ========== SYSTEM ERRORS ==========
    INTERNAL_ERROR("INTERNAL_ERROR", "Error interno del sistema"),
    EXTERNAL_SERVICE_ERROR("EXTERNAL_SERVICE_ERROR", "Error en servicio externo"),
    DATABASE_ERROR("DATABASE_ERROR", "Error de base de datos");
    
    private final String code;
    private final String defaultMessage;
    
    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    
    public String getCode() { 
        return code; 
    }
    
    public String getDefaultMessage() { 
        return defaultMessage; 
    }
}