package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public interface GroupState {
    boolean addStudent(Group group, Student student) throws SirhaException;
    boolean removeStudent(Group group, Student student) throws SirhaException;
}
