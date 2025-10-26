package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.domain.model.auth.Account;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;
import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.util.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountMongoRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final StudentService studentService;

    public AuthController(AccountMongoRepository accounts,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          JwtService jwt,
                          StudentService studentService) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwt = jwt;
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerStudent(@RequestBody RegisterRequest req) throws SirhaException {
        var studentAuth = studentService.registerStudent(req);

        if (accounts.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, req.getUsername(), req.getEmail(), req.getCodigo(),
                        "Username ya existe"));
        }
        var acc = new Account(
            req.getUsername(),
            req.getEmail(),
            passwordEncoder.encode(req.getPassword()),
            Role.STUDENT,
            studentAuth.getId() 
        );
        accounts.save(acc);

        String token = jwt.generateAccessToken(acc.getUsername(), acc.getRole());
        return ResponseEntity.ok(new AuthResponse(
            studentAuth.getId(), acc.getUsername(), acc.getEmail(), req.getCodigo(), token
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        var acc = accounts.findByUsername(req.getUsername()).orElseThrow();
        String token = jwt.generateAccessToken(acc.getUsername(), acc.getRole());
        return ResponseEntity.ok(new AuthResponse(acc.getId(), acc.getUsername(), acc.getEmail(), null, token));
    }

    public static class AdminRegisterRequest {
        public String username;
        public String email;
        public String password;
    }
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterRequest req) {
        if (accounts.existsByUsername(req.username)) return ResponseEntity.badRequest().body("Username ya existe");
        var acc = new Account(req.username, req.email, passwordEncoder.encode(req.password), Role.ADMIN, null);
        accounts.save(acc);
        return ResponseEntity.ok().build();
    }

    public static class DeanRegisterRequest {
        public String username;
        public String email;
        public String password;
        public String linkedDecanateId;
    }
    @PostMapping("/register-dean")
    public ResponseEntity<?> registerDean(@RequestBody DeanRegisterRequest req) {
        if (accounts.existsByUsername(req.username)) return ResponseEntity.badRequest().body("Username ya existe");
        var acc = new Account(req.username, req.email, passwordEncoder.encode(req.password), Role.DEAN, req.linkedDecanateId);
        accounts.save(acc);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader){
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : "";
        var claims = jwt.parse(token);
        return ResponseEntity.ok(
            java.util.Map.of("username", claims.getSubject(), "role", claims.get("role", String.class))
        );
    }
}
