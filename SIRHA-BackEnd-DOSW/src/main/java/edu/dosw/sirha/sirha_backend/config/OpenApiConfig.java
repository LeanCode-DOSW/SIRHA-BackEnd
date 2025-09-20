package edu.dosw.sirha.sirha_backend.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI sirhaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIRHA API")
                        .version("v1")
                        .description("API para el Sistema de Reasignación de Horarios Académicos - Avance 1"));
    }
}
