package edu.dosw.sirha.SIRHA_BackEnd.domain.exception;

public class SirhaException extends Exception {
    private final ErrorCode errorCode;
    
    // Constructor básico con ErrorCode
    public SirhaException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    
    // Constructor con mensaje personalizado
    public SirhaException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
    
    // Constructor con mensaje formateado
    public SirhaException(ErrorCode errorCode, String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
        this.errorCode = errorCode;
    }
    
    // Constructor con causa
    public SirhaException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
    }
    
    // Getters
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public String getCode() {
        return errorCode.getCode();
    }
    
    // ========== FACTORY METHODS PARA REQUEST STATES ==========
    
    public static SirhaException requestAlreadyApproved(String requestId) {
        return new SirhaException(ErrorCode.REQUEST_ALREADY_APPROVED, 
            "La solicitud %s ya está aprobada y no puede ser aprobada nuevamente", requestId);
    }
    
    public static SirhaException requestAlreadyRejected(String requestId) {
        return new SirhaException(ErrorCode.REQUEST_ALREADY_REJECTED, 
            "La solicitud %s ya está rechazada y no puede ser rechazada nuevamente", requestId);
    }
    
    public static SirhaException invalidStateTransition(String requestId, String currentState, String targetState) {
        return new SirhaException(ErrorCode.INVALID_STATE_TRANSITION, 
            "No se puede cambiar la solicitud %s de estado %s a %s", requestId, currentState, targetState);
    }
    
    public static SirhaException requestNotInReview(String requestId) {
        return new SirhaException(ErrorCode.REQUEST_NOT_IN_REVIEW, 
            "La solicitud %s debe estar en revisión para realizar esta operación", requestId);
    }
    
    // ========== FACTORY METHODS PARA RESOURCES ==========
    
    public static SirhaException studentNotFound(String username) {
        return new SirhaException(ErrorCode.STUDENT_NOT_FOUND, 
            "Estudiante no encontrado con username: %s", username);
    }
    
    public static SirhaException subjectNotFound(String subjectName) {
        return new SirhaException(ErrorCode.SUBJECT_NOT_FOUND, 
            "Materia no encontrada: %s", subjectName);
    }
    
    public static SirhaException groupNotFound(String groupCode) {
        return new SirhaException(ErrorCode.GROUP_NOT_FOUND, 
            "Grupo no encontrado: %s", groupCode);
    }
    
    public static SirhaException requestNotFound(String requestId) {
        return new SirhaException(ErrorCode.REQUEST_NOT_FOUND, 
            "Solicitud no encontrada con ID: %s", requestId);
    }
    
    // ========== FACTORY METHODS PARA CONFLICTS ==========
    
    public static SirhaException emailAlreadyExists(String email) {
        return new SirhaException(ErrorCode.EMAIL_ALREADY_EXISTS, 
            "El email %s ya está registrado en el sistema", email);
    }
    
    public static SirhaException codeAlreadyExists(String code) {
        return new SirhaException(ErrorCode.CODE_ALREADY_EXISTS, 
            "El código estudiantil %s ya está registrado", code);
    }
    
    // ========== FACTORY METHODS PARA VALIDATIONS ==========
    
    public static SirhaException invalidCredentials() {
        return new SirhaException(ErrorCode.INVALID_CREDENTIALS, 
            "Las credenciales proporcionadas son inválidas");
    }
    
    public static SirhaException validationError(String fieldName, String reason) {
        return new SirhaException(ErrorCode.VALIDATION_ERROR, 
            "Error de validación en campo '%s': %s", fieldName, reason);
    }
}