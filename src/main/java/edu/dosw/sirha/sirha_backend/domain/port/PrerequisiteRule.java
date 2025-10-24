package edu.dosw.sirha.sirha_backend.domain.port;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;

public interface PrerequisiteRule  {
    boolean canEnroll(Subject subject, AcademicProgress progress);
}    