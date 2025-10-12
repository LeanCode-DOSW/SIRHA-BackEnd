package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.time.LocalDateTime;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;

public interface SubjectStateProcess {
    String getId();
    SemaforoColores getState();
    LocalDateTime getCreatedAt();
    void setState(SemaforoColores state);
    int getSemester();
    int getGrade();
    Group getGroup();
}
