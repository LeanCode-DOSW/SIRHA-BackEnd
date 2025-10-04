package edu.dosw.sirha.SIRHA_BackEnd.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

class SwaggerConfigTest {

    @Test
    void customOpenAPI_ShouldReturnConfiguredOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertEquals("SIRHA Backend API", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("API REST para el Sistema de Información de Recursos Humanos y Administrativos (SIRHA)",
                info.getDescription());
        assertNotNull(info.getContact());
        assertEquals("Equipo SIRHA", info.getContact().getName());
        assertEquals("sirha@dosw.edu", info.getContact().getEmail());
    }
}
