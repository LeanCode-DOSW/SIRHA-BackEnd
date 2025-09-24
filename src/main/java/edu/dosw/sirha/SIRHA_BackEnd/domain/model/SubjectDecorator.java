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

    private SubjectState estado;

    public SubjectDecorator(Subject subject) {
        this.subject = subject;
        this.estado = new NoCursadaState();
    }


    public String getNombre() {return subject.getNombre();}
    public String getCodigo() {return subject.getCodigo();}
    public int getCreditos() {return subject.getCreditos();}
    public List<Group> getGrupos() {return subject.getGrupos();}

    public void setEstado(SubjectState estado) {this.estado = estado;}
    public void setEstadoColor(SemaforoColores estadoColor) {this.estadoColor = estadoColor;}
    public void setSemestreMateria(int semestre){this.semestre = semestre;}


    public void inscribir() { estado.inscribir(this); }
    public void aprobar()   { estado.aprobar(this); }
    public void reprobar()  { estado.reprobar(this); }
}
