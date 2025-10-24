package edu.dosw.sirha.sirha_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API
 * 
 * Una vez configurado, la documentación estará disponible en:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIRHA Backend API")
                        .version("1.0.0")
                        .description("API REST para el Sistema de Información de Recursos Humanos y Administrativos (SIRHA)")
                        .contact(new Contact()
                                .name("Equipo SIRHA")
                                .email("sirha@dosw.edu")
                                .url("https://dosw.edu")));
    }
}