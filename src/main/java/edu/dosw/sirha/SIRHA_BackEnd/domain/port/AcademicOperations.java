package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;

public interface AcademicOperations {
    boolean canEnroll(Subject subject);

    void enrollSubject(Subject subject);
    void unenrollSubject(Subject subject);
    
    boolean hasSubject(Subject subject);
    
}
