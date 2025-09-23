package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class Semaforo {
    private Map<String, SemaforoColores> estados = new HashMap<>();

    public SemaforoColores getColor(String materiaCodigo) {
        return estados.getOrDefault(materiaCodigo, SemaforoColores.GRIS);
    }

    public void actualizarEstado(String materiaCodigo, SemaforoColores color) {
        estados.put(materiaCodigo, color);
    }
}
