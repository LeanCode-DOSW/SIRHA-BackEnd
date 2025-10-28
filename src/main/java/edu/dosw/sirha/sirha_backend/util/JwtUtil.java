package edu.dosw.sirha.sirha_backend.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

  private final SecretKey key;
  private final String issuer;
  private final long ttlMinutes;

  public JwtUtil(
      @Value("${JWT_SECRET:dev-secret-please-change}") String secret,
      @Value("${JWT_ISSUER:sirha}") String issuer,
      @Value("${JWT_ACCESS_TTL:60}") long ttlMinutes
  ) {
    this.key = buildKey(secret);
    this.issuer = issuer;
    this.ttlMinutes = ttlMinutes;
  }

  private SecretKey buildKey(String secret) {
    try {
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Base64
    } catch (IllegalArgumentException ex) {
      return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); 
    }
  }

  public String generate(String username, String role) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(ttlMinutes * 60);
    return Jwts.builder()
        .subject(username)
        .issuer(issuer)
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .claims(Map.of("role", role))
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
