package edu.dosw.sirha.SIRHA_BackEnd.exception;

public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }
}