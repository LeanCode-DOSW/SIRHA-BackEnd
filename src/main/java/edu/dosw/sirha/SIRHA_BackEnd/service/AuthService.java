package edu.dosw.sirha.SIRHA_BackEnd.service;


import java.util.Optional;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;

public interface AuthService {
    AuthResponse registerStudent(RegisterRequest request);
    AuthResponse loginStudent(LoginRequest request);
}
