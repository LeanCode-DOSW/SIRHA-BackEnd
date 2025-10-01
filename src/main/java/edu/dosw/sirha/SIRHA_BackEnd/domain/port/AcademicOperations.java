package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface AcademicOperations {
    boolean canEnroll(Subject subject);
    boolean canEnrollInGroup(Subject subject, Group group);
    void enrollSubject(Subject subject, Group group);
    void unenrollSubject(Subject subject, Group group);
    
    boolean hasSubject(Subject subject);

}
