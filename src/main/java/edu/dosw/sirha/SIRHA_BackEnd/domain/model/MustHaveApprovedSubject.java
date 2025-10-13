package edu.dosw.sirha.sirha_backend.domain.model;
import edu.dosw.sirha.sirha_backend.domain.port.AcademicProgress;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;

public class MustHaveApprovedSubject implements PrerequisiteRule {

    private final Subject requiredSubject;

    public MustHaveApprovedSubject(Subject requiredSubject) {
        this.requiredSubject = requiredSubject;
    }

    @Override
    public boolean canEnroll(Subject subject, AcademicProgress progress) {
        return progress.isSubjectApproved(requiredSubject);
    }
    
}
