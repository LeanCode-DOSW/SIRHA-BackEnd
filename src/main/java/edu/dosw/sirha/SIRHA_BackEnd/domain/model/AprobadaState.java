package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class AprobadaState implements SubjectState {

    @Override
    public void setEstado(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.VERDE);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        materia.setSemestreMateria(semestre);
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        System.out.println("No puedes reinscribir una materia aprobada.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        System.out.println("Ya está aprobada.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        System.out.println("No puedes reprobar una materia aprobada.");
    }
}
