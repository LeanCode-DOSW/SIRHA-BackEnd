package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

public class SubjectDecorator {
    @Field("subject")
    private Subject subject;
    @Field("estadoColor")
    private SemaforoColores estadoColor;
    @Field("semestre")
    private int semestre;

    private SubjectState state;

    public SubjectDecorator(Subject subject) {
        this.subject = subject;
        this.state = new NoCursadaState();
    }


    public String getName() {return subject.getName();}
    public int getCreditos() {return subject.getCreditos();}
    public List<Group> getGroups() {return subject.getGroups();}

    public void setState(SubjectState state) {this.state = state;}
    public void setEstadoColor(SemaforoColores estadoColor) {this.estadoColor = estadoColor;}
    public void setSemestreMateria(int semestre){this.semestre = semestre;}


    public void inscribir() { state.inscribir(this); }
    public void aprobar()   { state.aprobar(this); }
    public void reprobar()  { state.reprobar(this); }
}
