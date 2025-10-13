package edu.dosw.sirha.sirha_backend.domain.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectStateProcess;

public class SubjectProgress implements SubjectStateProcess {
    @Id
    private String id;
    private SemaforoColores state;
    private LocalDateTime creadoEn;
    private int semestre;
    private Group group;
    private int grade;

    public SubjectProgress() {
        this.creadoEn = LocalDateTime.now();
    }

    public SubjectProgress(SemaforoColores state, int semestre, Group group, int grade) {
        this();
        setState(state);
        this.semestre = semestre;
        this.group = group;
        this.grade = grade;
    }

    public String getId() {return id;}
    public SemaforoColores getState() {return state;}
    public LocalDateTime getCreatedAt() {return creadoEn;}
    public int getSemester() {return semestre;}
    public int getGrade() {return grade;}
    public Group getGroup() {return group;}


    public void setState(SemaforoColores state) {this.state = state;}
    
}
