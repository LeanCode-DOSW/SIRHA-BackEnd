package edu.dosw.sirha.sirha_backend.domain.model.stateGroup;
import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.port.GroupState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class StatusClosed implements GroupState {
    @Override
    public boolean addStudent(Group group, Student student) throws SirhaException {
        throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED);
    }
    @Override
    public boolean removeStudent(Group group, Student student) throws SirhaException {
        if (group.getInscritos() > 0) {
            group.removeStudent(student);
            if (group.getCuposDisponibles() > 0) {
                group.setEstadoGrupo(new StatusOpen());
            }
            return true;
            } 
        throw SirhaException.of(ErrorCodeSirha.GROUP_CLOSED);
    }
}
