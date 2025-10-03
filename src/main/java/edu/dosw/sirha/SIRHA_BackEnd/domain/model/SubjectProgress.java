package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectStateProcess;

public class SubjectProgress implements SubjectStateProcess {
    @Id
    private int id;
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

    public int getId() {return id;}
    public SemaforoColores getState() {return state;}
    public LocalDateTime getCreadoEn() {return creadoEn;}
    public int getSemester() {return semestre;}
    public int getGrade() {return grade;}
    public Group getGroup() {return group;}


    public void setState(SemaforoColores state) {this.state = state;}
    
}
