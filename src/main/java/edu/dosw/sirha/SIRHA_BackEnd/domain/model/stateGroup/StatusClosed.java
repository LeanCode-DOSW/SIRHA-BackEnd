package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;

public class StatusClosed implements GroupState {
    @Override
    public boolean addStudent(Group group, Student student) {
        throw new RuntimeException("El grupo está cerrado. No se pueden inscribir más estudiantes.");
    }
    @Override
    public boolean removeStudent(Group group, Student student) {
        if (group.getInscritos() > 0) {
            group.removeStudent(student);
            System.out.println("Estudiante desinscrito del grupo.");
            if (group.getCuposDisponibles() > 0) {
                group.setEstadoGrupo(new StatusOpen());
            }
            return true;
            } 
        throw new RuntimeException("No hay estudiantes inscritos para desinscribir.");
    }
}
