package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

public class StatusClosed implements GroupState {
    @Override
    public void inscribirEstudiante(Group grupo, Student estudiante) {
        System.out.println("No se puede inscribir, el grupo está cerrado.");
    }
}
