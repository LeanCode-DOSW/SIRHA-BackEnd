package edu.dosw.sirha.sirha_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desactiva CSRF solo para pruebas
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .formLogin().disable(); // quita el login HTML por defecto
        return http.build();
    }
}