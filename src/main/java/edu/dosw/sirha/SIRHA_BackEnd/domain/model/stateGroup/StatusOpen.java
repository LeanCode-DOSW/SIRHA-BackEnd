package edu.dosw.sirha.sirha_backend.domain.model.stateGroup;

import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.port.GroupState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class StatusOpen implements GroupState {

    @Override
    public boolean addStudent(Group group, Student student) throws SirhaException {
        if (!group.isFull()) {
            group.addStudent(student);
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
            if (group.getCuposDisponibles() > 0) {
                group.setEstadoGrupo(new StatusOpen());
            }
            return true;
        } else {
            throw SirhaException.of(ErrorCodeSirha.STUDENT_NOT_IN_GROUP);
        }
    }
}
