package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

public class StatusOpen implements GroupState {

    @Override
    public void inscribirEstudiante(Group grupo, Student estudiante) {
        if (grupo.getCuposDisponibles() > 0) {
            grupo.addEstudiante(estudiante);
            System.out.println("Estudiante inscrito en el grupo.");
            if (grupo.getCuposDisponibles() == 0) {
                grupo.setEstadoGrupo(new StatusClosed());
            }
        } else {
            grupo.setEstadoGrupo(new StatusClosed());
            grupo.getEstadoGrupo().inscribirEstudiante(grupo, estudiante);
        }
    }
}
