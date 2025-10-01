package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.port.AcademicProgress;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.PrerequisiteRule;

public class MustHaveApprovedSubject implements PrerequisiteRule {

    private final Subject requiredSubject;

    public MustHaveApprovedSubject(Subject requiredSubject) {
        this.requiredSubject = requiredSubject;
    }

    @Override
    public boolean canEnroll(Subject subject, AcademicProgress progress) {
        if (progress == null || requiredSubject == null) {
            return false;
        }
        return progress.isSubjectApproved(requiredSubject);
    }
    
}
