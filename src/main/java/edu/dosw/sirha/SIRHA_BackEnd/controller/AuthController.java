package edu.dosw.sirha.sirha_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.service.AuthService;

/**
 * Controlador REST para el manejo de autenticación y registro de usuarios en el sistema SIRHA.
 * 
 * Este controlador proporciona endpoints para:
 * - Autenticación de usuarios existentes (login)
 * - Registro de nuevos usuarios en el sistema
 * 
 * Todos los endpoints están mapeados bajo la ruta base "/api/auth" y manejan
 * peticiones HTTP con formato JSON para el intercambio de datos.

 * @see AuthService
 * @see AuthResponse
 * @see LoginRequest
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
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
     * En caso de credenciales inválidas, retorna un código de error 401 (Unauthorized)
     * con un mensaje descriptivo del error.
     * 
     * @param request objeto que contiene las credenciales de autenticación
     *               (username y password). No debe ser null.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.loginStudent(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body(new LoginRequest(null, e.getMessage()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new LoginRequest(null, "Error interno del servidor"));
        }
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
     * - Contraseña con formato adecuado
     * - Rol válido para el sistema
     * 
     * @param user objeto User con la información del nuevo usuario.
     *            Debe contener username, passwordHash y rol válidos.
     *            No debe ser null.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try{
            AuthResponse response = authService.registerStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    

}
