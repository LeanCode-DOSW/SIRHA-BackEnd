package edu.dosw.sirha.sirha_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import edu.dosw.sirha.sirha_backend.domain.model.Account;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;
import edu.dosw.sirha.sirha_backend.dto.AuthResponse;
import edu.dosw.sirha.sirha_backend.dto.LoginRequest;
import edu.dosw.sirha.sirha_backend.dto.MeResponse;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequest;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.AccountMongoRepository;
import edu.dosw.sirha.sirha_backend.service.DecanateService;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.util.JwtUtil;
import edu.dosw.sirha.sirha_backend.util.ValidationUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountMongoRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwt;
    private final StudentService studentService;
    private final DecanateService decanateService;

    public AuthController(AccountMongoRepository accounts,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          JwtUtil jwt,
                          StudentService studentService,
                          DecanateService decanateService) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwt = jwt;
        this.studentService = studentService;
        this.decanateService = decanateService;
    }

    @PostMapping("/register-student")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEAN')")
    public ResponseEntity<AuthResponse> registerStudent(@RequestBody RegisterRequest req) throws SirhaException {
        if (accounts.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(
                new AuthResponse(req.getUsername(), req.getEmail(), ErrorCodeSirha.USERNAME_ALREADY_EXISTS.getDefaultMessage())
            );
        }

        studentService.registerStudent(req);

        var acc = new Account(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()), Role.STUDENT);
        accounts.save(acc);

        String token = jwt.generate(acc.getUsername(), acc.getRole().name());
        return ResponseEntity.ok(new AuthResponse(acc.getUsername(), acc.getEmail(), token));
    }

    @PostMapping("/register-dean")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerDean(@RequestBody RegisterRequest req) throws SirhaException {
        if (accounts.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(
                new AuthResponse(req.getUsername(), req.getEmail(), ErrorCodeSirha.USERNAME_ALREADY_EXISTS.getDefaultMessage())
            );
        }

        decanateService.registerDecanate(req);

        var acc = new Account(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()), Role.DEAN, req.getCareer());
        accounts.save(acc);

        String token = jwt.generate(acc.getUsername(), acc.getRole().name());
        return ResponseEntity.ok(new AuthResponse(acc.getUsername(), acc.getEmail(), token));
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest req) {
        ValidationUtil.validateRegistration(req.getUsername(), req.getEmail(), req.getPassword(), req.getCodigo());
        if (accounts.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body(
                new AuthResponse(req.getUsername(), req.getEmail(), ErrorCodeSirha.USERNAME_ALREADY_EXISTS.getDefaultMessage())
            );
        }

        var acc = new Account(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()), Role.ADMIN);
        accounts.save(acc);

        String token = jwt.generate(acc.getUsername(), acc.getRole().name());
        return ResponseEntity.ok(new AuthResponse(acc.getUsername(), acc.getEmail(), token));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MeResponse> me(@RequestHeader("Authorization") String authHeader){
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : "";
        var claims = jwt.parse(token);
        return ResponseEntity.ok(new MeResponse(
            claims.getSubject(),
            claims.get("role", String.class)
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            var acc = accounts.findByUsername(req.getUsername()).orElseThrow();

            String token = jwt.generate(acc.getUsername(), acc.getRole().name());
            return ResponseEntity.ok(new AuthResponse(acc.getUsername(), acc.getEmail(), token));

        } catch (AuthenticationException ex) {
            AuthResponse resp = new AuthResponse(req.getUsername(), "invalid_credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(resp);
        }
    }
}
