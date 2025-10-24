package edu.dosw.sirha.sirha_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import edu.dosw.sirha.sirha_backend.util.JwtService;
import edu.dosw.sirha.sirha_backend.service.impl.CustomUserDetailsService;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService uds, PasswordEncoder enc){
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(enc);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtService jwtService,
            @Value("${FRONTEND_URL:http://localhost:5173}") String frontendUrl
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(req -> {
                var c = new CorsConfiguration();
                c.setAllowedOrigins(List.of(frontendUrl, "http://localhost:3000", "http://localhost:5173"));
                c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                c.setAllowedHeaders(List.of("*"));
                c.setAllowCredentials(true);
                return c;
            }))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health"
                ).permitAll()

                .requestMatchers(HttpMethod.GET,
                    "/api/students/schedule/**",
                    "/api/students/academicPensum/**",
                    "/api/requests/student/**",
                    "/api/requests/status/**",
                    "/api/requests/**",
                    "/api/subjects/**"
                ).hasAnyRole("STUDENT","DEAN","ADMIN") 
                .requestMatchers(HttpMethod.POST,
                    "/api/students/*/solicitudes/cambio-materia",
                    "/api/students/*/solicitudes/cambio-grupo",
                    "/api/requests"
                ).hasRole("STUDENT")

                .requestMatchers(
                    "/api/students/**",
                    "/api/subjects/**",
                    "/api/requests/**"
                ).hasAnyRole("DEAN","ADMIN")

                .requestMatchers(
                    "/api/subjects/**",
                    "/api/students"
                ).hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
