package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;
import edu.dosw.sirha.SIRHA_BackEnd.exception.ErrorCodeSirha;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public class StatusClosed implements GroupState {
    @Override
    public boolean addStudent(Group group, Student student) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED);
    }
    @Override
    public boolean removeStudent(Group group, Student student) throws SirhaException {
        if (group.getInscritos() > 0) {
            group.removeStudent(student);
            System.out.println("Estudiante desinscrito del grupo.");
            if (group.getCuposDisponibles() > 0) {
                group.setEstadoGrupo(new StatusOpen());
            }
            return true;
            } 
        throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED);
    }
}
