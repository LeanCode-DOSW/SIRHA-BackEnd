package edu.dosw.sirha.SIRHA_BackEnd.domain.exception;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super("CONFLICT", message);
    }
}