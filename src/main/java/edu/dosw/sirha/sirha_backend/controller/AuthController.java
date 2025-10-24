package edu.dosw.sirha.sirha_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para el manejo de autenticación y registro de usuarios en el sistema SIRHA.
 * 
 * Este controlador proporciona endpoints para:
 * - Autenticación de usuarios existentes (login)
 * - Registro de nuevos usuarios en el sistema
 * 
 * Todos los endpoints están mapeados bajo la ruta base "/api/auth" y manejan
 * peticiones HTTP con formato JSON para el intercambio de datos.
 * 
 * @see AuthService
 * @see AuthResponse
 * @see LoginRequest
 * @see RegisterRequest
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "API para autenticación y registro de usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para autenticar usuarios en el sistema.
     * 
     * Recibe las credenciales del usuario (username y password) y valida
     * su autenticidad contra la base de datos. Si las credenciales son válidas,
     * retorna un token de autenticación junto con el nombre de usuario.
     * 
     * @param request objeto que contiene las credenciales de autenticación
     *               (username y password). No debe ser null.
     * @return ResponseEntity con AuthResponse conteniendo token y datos del usuario
     * @throws SirhaException si las credenciales son inválidas o hay error en el proceso
     */
    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario con sus credenciales y retorna un token de acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) throws SirhaException {
        AuthResponse response = authService.loginStudent(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar nuevos usuarios en el sistema.
     * 
     * Crea un nuevo usuario en la base de datos con la información proporcionada.
     * El sistema asigna automáticamente un ID único y hashea la contraseña
     * antes del almacenamiento por seguridad.
     * 
     * Validaciones realizadas:
     * - Username único en el sistema
     * - Email único en el sistema
     * - Código de estudiante único
     * - Contraseña con formato adecuado
     * - Datos obligatorios presentes
     * 
     * @param request objeto RegisterRequest con la información del nuevo usuario.
     *               Debe contener username, email, password y datos personales válidos.
     *               No debe ser null.
     * @return ResponseEntity con AuthResponse conteniendo token y datos del usuario registrado
     * @throws SirhaException si hay datos duplicados, inválidos o error en el proceso
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo estudiante en el sistema y retorna un token de acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Usuario ya existe (username, email o código duplicado)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) throws SirhaException {
        AuthResponse response = authService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
