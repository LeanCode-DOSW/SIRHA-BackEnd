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
      byte[] decoded = Decoders.BASE64.decode(secret); // try base64 first
      // Ensure key is at least 256 bits (32 bytes). If not, fall through to hashing below.
      if (decoded.length >= 32) {
        return Keys.hmacShaKeyFor(decoded);
      }
    } catch (IllegalArgumentException ex) {
      // ignore - we'll derive a proper-length key from the raw secret below
    }

    try {
      // Derive a 256-bit key from the provided secret using SHA-256 as a safe fallback
      byte[] hashed = java.security.MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
      return Keys.hmacShaKeyFor(hashed);
    } catch (java.security.NoSuchAlgorithmException e) {
      // Extremely unlikely - rethrow as runtime
      throw new IllegalStateException("SHA-256 not available for key derivation", e);
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
