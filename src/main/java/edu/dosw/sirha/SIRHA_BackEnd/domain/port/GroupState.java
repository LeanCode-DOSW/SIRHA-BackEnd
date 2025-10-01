package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface GroupState {
    boolean addStudent(Group group, Student student);
    boolean removeStudent(Group group, Student student);
}
