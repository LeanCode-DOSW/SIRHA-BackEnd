package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;

public interface AcademicOperations {
    boolean canEnroll(Subject subject);
    boolean canEnrollInGroup(Subject subject, Group group);
    void enrollSubject(Subject subject, Group group) throws SirhaException;
    void unenrollSubject(Subject subject, Group group) throws SirhaException;
    void approveSubject(Subject subject) throws SirhaException;
    void failSubject(Subject subject) throws SirhaException;
    
    boolean hasSubject(Subject subject);
    boolean hasActiveRequests();
}
