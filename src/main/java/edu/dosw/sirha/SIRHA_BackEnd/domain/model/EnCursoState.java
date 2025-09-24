package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class EnCursoState implements SubjectState {

    @Override
    public void setEstado(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.AMARILLO);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        materia.setSemestreMateria(semestre); // semestre en que se cursa
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        System.out.println("Ya estás inscrito en esta materia.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        materia.setEstado(new AprobadaState());
        materia.setEstadoColor(SemaforoColores.VERDE);
        System.out.println("Materia aprobada.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        materia.setEstado(new ReprobadaState());
        materia.setEstadoColor(SemaforoColores.ROJO);
        System.out.println("Materia reprobada.");
    }
}
