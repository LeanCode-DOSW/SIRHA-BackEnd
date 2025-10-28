package edu.dosw.sirha.sirha_backend.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Arrays;

public enum Careers {
    DEFAULT("Default Career"),
    INGENIERIA_DE_SISTEMAS("Ingenieria de Sistemas"),
    INGENIERIA_INDUSTRIAL("Ingeniería Industrial"),
    INGENIERIA_CIVIL("Ingeniería Civil"),
    INGENIERIA_ELECTRONICA("Ingeniería Electrónica"),
    INGENIERIA_ELECTRICA("Ingeniería Eléctrica"),
    INGENIERIA_MECANICA("Ingeniería Mecánica"),
    INGENIERIA_BIOMEDICA("Ingeniería Biomédica"),
    INGENIERIA_AMBIENTAL("Ingeniería Ambiental"),
    INGENIERIA_ESTADISTICA("Ingeniería Estadística"),
    INGENIERIA_EN_BIOTECNOLOGIA("Ingeniería en Biotecnología"),
    INGENIERIA_DE_INTELIGENCIA_ARTIFICIAL("Ingeniería de Inteligencia Artificial"),
    INGENIERIA_DE_CIBERSEGURIDAD("Ingeniería de Ciberseguridad"),
    ADMINISTRACION_DE_EMPRESAS("Administración de Empresas"),
    ECONOMIA("Economía"),
    MATEMATICAS("Matemáticas");

    private final String displayName;

    Careers(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }

    private static String normalize(String s) {
        // quita acentos y normaliza
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                              .replaceAll("\\p{M}+", ""); // sin tildes
        n = n.trim().replace('ñ','n')
                    .replace('Ñ','N')
                    .replace(' ', '_')
                    .replace('-', '_')
                    .toUpperCase();
        return n;
    }

    @JsonCreator
    public static Careers fromJson(String raw) {
        final String normalized = normalize(raw == null ? "" : raw);
        final String key = alias(normalized);

        return Arrays.stream(values())
                .filter(c -> c.name().equals(key) || normalize(c.getDisplayName()).equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Career inválido: " + raw + ". Permitidos: " + Arrays.toString(values())
                ));
    }

    // Mapea variantes toleradas a la constante real
    private static String alias(String key) {
        if ("INGENIERIA_SISTEMAS".equals(key)) return "INGENIERIA_DE_SISTEMAS";
        // aquí puedes añadir más alias si quieres
        return key;
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
