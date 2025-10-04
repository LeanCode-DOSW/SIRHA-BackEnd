package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;
import edu.dosw.sirha.SIRHA_BackEnd.dto.LoginRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.RegisterRequest;
import edu.dosw.sirha.SIRHA_BackEnd.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        controller = new AuthController(authService);
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest("user", "pass");
        AuthResponse response = new AuthResponse("token123", "user");
        when(authService.loginStudent(request)).thenReturn(response);

        ResponseEntity<?> result = controller.login(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void login_InvalidCredentials_ShouldReturn401() {
        LoginRequest request = new LoginRequest("user", "wrong");
        when(authService.loginStudent(request)).thenThrow(new IllegalArgumentException("Invalid"));

        ResponseEntity<?> result = controller.login(request);

        assertEquals(401, result.getStatusCodeValue());
    }

    @Test
    void login_InternalError_ShouldReturn500() {
        LoginRequest request = new LoginRequest("user", "pass");
        when(authService.loginStudent(request)).thenThrow(new RuntimeException("Fail"));

        ResponseEntity<?> result = controller.login(request);

        assertEquals(500, result.getStatusCodeValue());
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest("user", "pass", "ROLE_STUDENT");
        AuthResponse response = new AuthResponse("token456", "user");
        when(authService.registerStudent(request)).thenReturn(response);

        ResponseEntity<?> result = controller.register(request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void register_BadRequest_ShouldReturn400() {
        RegisterRequest request = new RegisterRequest("user", "pass", "ROLE_STUDENT");
        when(authService.registerStudent(request)).thenThrow(new IllegalArgumentException("Bad"));

        ResponseEntity<?> result = controller.register(request);

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void register_InternalError_ShouldReturn500() {
        RegisterRequest request = new RegisterRequest("user", "pass", "ROLE_STUDENT");
        when(authService.registerStudent(request)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> result = controller.register(request);

        assertEquals(500, result.getStatusCodeValue());
    }
}
