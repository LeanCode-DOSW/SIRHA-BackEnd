package edu.dosw.sirha.sirha_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sirhaOpenAPI() {
        final String scheme = "bearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("SIRHA Backend API")
                .version("1.0.0")
                .description("API REST para el Sistema de Información de Recursos Humanos y Administrativos (SIRHA)")
                .contact(new Contact()
                    .name("Equipo SIRHA")
                    .email("sirha@dosw.edu")
                    .url("https://dosw.edu")))
            .components(new Components()
                .addSecuritySchemes(scheme, new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList(scheme));
    }
}
