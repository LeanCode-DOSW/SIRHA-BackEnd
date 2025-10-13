package edu.dosw.sirha.sirha_backend.domain.model.enums;

public enum Careers {
    DEFAULT("Default Career"),
    INGENIERIA_DE_SISTEMAS("Ingeniería de Sistemas"),
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

    Careers(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}