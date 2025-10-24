package edu.dosw.sirha.sirha_backend.service;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface AuthService {
    AuthResponse registerStudent(RegisterRequest request) throws SirhaException;
    AuthResponse loginStudent(LoginRequest request) throws SirhaException;
}
