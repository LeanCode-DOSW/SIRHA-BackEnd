package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class Semaforo {
    private Map<String, SemaforoColores> materias; // materia -> color

    public Semaforo() {
        this.materias = new HashMap<>();
    }

    public void setColor(String materia, SemaforoColores color) {
        materias.put(materia, color);
    }

    public SemaforoColores getColor(String materia) {
        return materias.getOrDefault(materia, SemaforoColores.VERDE);
    }

    public Map<String, SemaforoColores> getAll() {
        return Collections.unmodifiableMap(materias);
    }
}
