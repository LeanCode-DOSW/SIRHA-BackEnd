package edu.dosw.sirha.sirha_backend.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtService {

    private final SecretKey key;
    private final String issuer;
    private final long accessTtlMinutes;

    public JwtService(
        @Value("${JWT_SECRET}") String secret,
        @Value("${JWT_ISSUER:sirha}") String issuer,
        @Value("${JWT_ACCESS_TTL:60}") long accessTtlMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.issuer = issuer;
        this.accessTtlMinutes = accessTtlMinutes;
    }

    public String generateAccessToken(String username, Role role) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessTtlMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
            .subject(username)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claims(Map.of("role", role.name()))
            .signWith(key)
            .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
