package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public interface GroupState {
    boolean addStudent(Group group, Student student) throws SirhaException;
    boolean removeStudent(Group group, Student student) throws SirhaException;
}
