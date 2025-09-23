package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthRequest;
import edu.dosw.sirha.SIRHA_BackEnd.dto.AuthResponse;   
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.User;
import edu.dosw.sirha.SIRHA_BackEnd.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
 * La autenticación actualmente utiliza un token JWT simulado ("fake-jwt-token")
 * para fines de desarrollo. En producción, debe implementarse un sistema
 * completo de generación y validación de tokens JWT.
 * 
 * @author Equipo SIRHA
 * @version 1.0
 * @since 2023-12-01
 * 
 * @see AuthService
 * @see AuthRequest
 * @see AuthResponse
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Servicio de autenticación inyectado para manejar la lógica de negocio
     * relacionada con login, registro y validación de credenciales.
     */
    private final AuthService authService;

    /**
     * Constructor del controlador con inyección de dependencias.
     * 
     * @param authService el servicio de autenticación a utilizar.
     *                   No debe ser null.
     */
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
     * @return ResponseEntity con AuthResponse conteniendo:
     *         - Código 200: token y username si las credenciales son válidas
     *         - Código 401: mensaje de error si las credenciales son inválidas
     * 
     * @example
     * POST /api/auth/login
     * Content-Type: application/json
     * 
     * {
     *   "username": "juan.perez",
     *   "password": "miContraseña123"
     * }
     * 
     * Respuesta exitosa (200):
     * {
     *   "token": "fake-jwt-token",
     *   "username": "juan.perez"
     * }
     * 
     * Respuesta de error (401):
     * {
     *   "token": null,
     *   "username": "Credenciales inválidas"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.login(request.getUsername(), request.getPassword())
                .map(user -> ResponseEntity.ok(new AuthResponse("fake-jwt-token", user.getUsername())))
                .orElse(ResponseEntity.status(401).body(new AuthResponse(null, "Credenciales inválidas")));
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
     * @return ResponseEntity<User> con código 200 y el usuario creado,
     *         incluyendo el ID asignado automáticamente
     * 
     * @throws IllegalArgumentException si los datos del usuario son inválidos
     * @throws RuntimeException si el username ya existe en el sistema
     * 
     * @example
     * POST /api/auth/register
     * Content-Type: application/json
     * 
     * {
     *   "username": "maria.garcia",
     *   "passwordHash": "contraseñaSegura456",
     *   "rol": "ESTUDIANTE"
     * }
     * 
     * Respuesta (200):
     * {
     *   "id": "generated-id-12345",
     *   "username": "maria.garcia",
     *   "passwordHash": "$2a$10$hashGeneradoAutomaticamente...",
     *   "rol": "ESTUDIANTE"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
}
