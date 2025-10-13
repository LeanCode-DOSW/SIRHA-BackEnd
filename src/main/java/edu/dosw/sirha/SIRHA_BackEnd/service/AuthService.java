package edu.dosw.sirha.sirha_backend.service;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;

public interface AuthService {
    AuthResponse registerStudent(RegisterRequest request);
    AuthResponse loginStudent(LoginRequest request);
}
