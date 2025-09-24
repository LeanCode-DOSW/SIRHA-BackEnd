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

 * @see AuthService
 * @see AuthRequest
 * @see AuthResponse
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
     * @return ResponseEntity con AuthResponse conteniendo:
     *         - Código 200: token y username si las credenciales son válidas
     *         - Código 401: mensaje de error si las credenciales son inválidas
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
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        boolean exists = authService.existsByEmail(email);
        return ResponseEntity.ok(new AuthResponse(null, exists ? "Email ya en uso" : "Email disponible"));
    }

}
