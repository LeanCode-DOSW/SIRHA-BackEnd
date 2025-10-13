package edu.dosw.sirha.sirha_backend.domain.port;

import java.time.LocalDateTime;

import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;

public interface SubjectStateProcess {
    String getId();
    SemaforoColores getState();
    LocalDateTime getCreatedAt();
    void setState(SemaforoColores state);
    int getSemester();
    int getGrade();
    Group getGroup();
}
