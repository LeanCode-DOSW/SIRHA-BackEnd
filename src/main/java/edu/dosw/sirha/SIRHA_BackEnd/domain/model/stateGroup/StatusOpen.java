package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.GroupState;
import edu.dosw.sirha.SIRHA_BackEnd.exception.ErrorCodeSirha;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public class StatusOpen implements GroupState {

    @Override
    public boolean addStudent(Group group, Student student) throws SirhaException {
        if (!group.isFull()) {
            group.addStudent(student);
            System.out.println("Estudiante inscrito en el grupo.");
            if (group.getCuposDisponibles() == 0) {
                group.setEstadoGrupo(new StatusClosed());
            }
            return true;
        } else {
            group.setEstadoGrupo(new StatusClosed());
            return group.enrollStudent(student);

        }
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
        } else {
            throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_IN_GROUP);
        }
    }
}
