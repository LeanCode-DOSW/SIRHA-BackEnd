package edu.dosw.sirha.sirha_backend.exception;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;

public class SirhaException extends Exception {
    private final ErrorCodeSirha errorCode;
    
    public SirhaException(ErrorCodeSirha errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    
    public SirhaException(ErrorCodeSirha errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
    
    public SirhaException(ErrorCodeSirha errorCode, String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
        this.errorCode = errorCode;
    }
    
    
    public ErrorCodeSirha getErrorCode() {
        return errorCode;
    }
    
    public String getCode() {
        return errorCode.getCode();
    }
    
    public static SirhaException invalidStateTransition(String requestId, RequestStateEnum currentState, RequestStateEnum targetState) {
        return new SirhaException(ErrorCodeSirha.INVALID_STATE_TRANSITION, 
            "Solicitud %s: transición %s → %s no permitida", requestId, currentState, targetState);
    }

    public static SirhaException of(ErrorCodeSirha errorCode) {
        return new SirhaException(errorCode);
    }
    
    public static SirhaException of(ErrorCodeSirha errorCode, String context) {
        return new SirhaException(errorCode, errorCode.getDefaultMessage() + " - " + context);
    }
    
    public static SirhaException of(ErrorCodeSirha errorCode, String messageFormat, Object... args) {
        return new SirhaException(errorCode, messageFormat, args);
    }
}