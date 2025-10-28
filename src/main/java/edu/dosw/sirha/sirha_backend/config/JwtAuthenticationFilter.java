package edu.dosw.sirha.sirha_backend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.dosw.sirha.sirha_backend.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private static final AntPathMatcher matcher = new AntPathMatcher();

  private static final List<String> PUBLIC_PATHS = List.of(
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/actuator/**",
      "/api/auth/login",
      "/api/auth/refresh-token"
  );

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return PUBLIC_PATHS.stream().anyMatch(p -> matcher.match(p, path));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    try {
      var token = header.substring(7);
      var claims = jwtUtil.parse(token);
      var username = claims.getSubject();
      var role = claims.get("role", String.class);  

      var auth = new UsernamePasswordAuthenticationToken(
          username,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_" + role)) 
      );
      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }

    chain.doFilter(request, response);
  }
}
