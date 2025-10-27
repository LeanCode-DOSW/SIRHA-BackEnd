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

import edu.dosw.sirha.sirha_backend.util.JwtUtil;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Role;
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
            JwtUtil jwtService,
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
            // AUTH & DOCS 
            .requestMatchers(
                "/api/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/actuator/health"
            ).permitAll()

            // StudentController  (/api/students/**)
            // STUDENT 
            .requestMatchers(HttpMethod.POST,
                "/api/students/*/solicitudes/cambio-grupo",
                "/api/students/*/solicitudes/cambio-materia"
            ).hasRole(Role.STUDENT.name())
            // STUDENT / DEAN / ADMIN
            .requestMatchers(HttpMethod.GET,
                "/api/students/schedule/**",
                "/api/students/schedules/**",
                "/api/students/academicPensum/**",
                "/api/students/*/percentage-by-color",
                "/api/students/*/subjects/color/**",
                "/api/students/*/requests/**"
            ).hasAnyRole(Role.STUDENT.name(),Role.DEAN.name(),Role.ADMIN.name())
            // DEAN / ADMIN
            .requestMatchers(HttpMethod.GET,
                "/api/students",
                "/api/students/*",
                "/api/students/username/*",
                "/api/students/email/*",
                "/api/students/*/basic-info"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())

            // RequestController  (/api/requests/**)
            // DEAN / ADMIN
            .requestMatchers(
                "/api/requests/**"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())

            // DecanateController  (/api/decanates/**)
            // DEAN / ADMIN
            .requestMatchers(
                "/api/decanates/requests",
                "/api/decanates/requests/*",
                "/api/decanates/*/requests",
                "/api/decanates/*/requests/*/receive",
                "/api/decanates/*/requests/*/approve",
                "/api/decanates/*/requests/*/reject",
                "/api/decanates/*/study-plans",
                "/api/decanates/*/statistics/**"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())
            .requestMatchers(HttpMethod.GET,
                "/api/decanates",
                "/api/decanates/*",
                "/api/decanates/students/*/basic-info"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())

            // ADMIN
            .requestMatchers(HttpMethod.POST, "/api/decanates").hasRole(Role.ADMIN.name())

            // SubjectAndGroupController  (/api/subjects/**)

            // STUDENT / DEAN / ADMIN 
            .requestMatchers(HttpMethod.GET,
                "/api/subjects",
                "/api/subjects/*",
                "/api/subjects/*/exists",
                "/api/subjects/groups",
                "/api/subjects/*/groups",
                "/api/subjects/*/groups/open",
                "/api/subjects/groups/*",
                "/api/subjects/groups/*/professor",
                "/api/subjects/groups/*/schedules",
                "/api/subjects/groups/*/full",
                "/api/subjects/groups/*/available-seats"
            ).hasAnyRole(Role.STUDENT.name(),Role.DEAN.name(),Role.ADMIN.name())
            // DEAN / ADMIN 
            .requestMatchers(HttpMethod.POST,
                "/api/subjects/*/groups",
                "/api/subjects/*/groups/add",
                "/api/subjects/groups/*/schedules"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())
            .requestMatchers(HttpMethod.PUT,
                "/api/subjects/groups/*/professor",
                "/api/subjects/groups/*/close",
                "/api/subjects/groups/*/open"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE,
                "/api/subjects/groups/*",
                "/api/subjects/*/groups"
            ).hasAnyRole(Role.DEAN.name(),Role.ADMIN.name())
            // ADMIN
            .requestMatchers(HttpMethod.POST, "/api/subjects").hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, "/api/subjects/*").hasRole(Role.ADMIN.name())

            // any other request
            .anyRequest().authenticated()
        )
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
