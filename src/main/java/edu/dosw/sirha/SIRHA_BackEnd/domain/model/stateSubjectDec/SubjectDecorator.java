package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;

import java.util.List;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.*;

public class SubjectDecorator {
    private final Subject subject;
    private SemaforoColores estadoColor;
    private int semestre;
    private SubjectState state;
    private Group group;

    public SubjectDecorator(Subject subject) {
        this.subject = subject;
        this.state = new NoCursadaState();
        state.setState(this);
    }
    
    
    public String getName() {return subject.getName();}
    public int getCreditos() {return subject.getCreditos();}
    public List<Group> getGroups() {return subject.getGroups();}

    void setState(SubjectState state) {this.state = state;}
    void setEstadoColor(SemaforoColores estadoColor) {this.estadoColor = estadoColor;}
    public void setSemestreMateria(int semestre){this.semestre = semestre;}
    public void setGroup(Group group) { state.setGroup(this, group); }
    void setGroupDirect(Group group) { this.group = group; }

    public SemaforoColores getEstadoColor() {return estadoColor;}
    public int getSemestre() {return semestre;}
    public Subject getSubject() {return subject;}
    public SubjectState getState() {return state;}
    public Group getGroup() { return group; }

    public void inscribir(Group grupo) { state.inscribir(this, grupo); }
    public void aprobar()   { state.aprobar(this); }
    public void reprobar()  { state.reprobar(this); }
    public void retirar()   { state.retirar(this); }
    
    public boolean puedeInscribirse() {
        return state.puedeInscribirse();
    }

    public boolean estaCursando() {
        return estadoColor == SemaforoColores.AMARILLO;
    }
    public boolean estaAprobada() {
        return estadoColor == SemaforoColores.VERDE;
    }
    public boolean estaReprobada() {
        return estadoColor == SemaforoColores.ROJO;
    }

    public int getId() { return subject.getId(); }
}
