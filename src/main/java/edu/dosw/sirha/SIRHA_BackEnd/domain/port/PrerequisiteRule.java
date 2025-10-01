package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;

public interface PrerequisiteRule  {
    boolean canEnroll(Subject subject, AcademicProgress progress);
}    