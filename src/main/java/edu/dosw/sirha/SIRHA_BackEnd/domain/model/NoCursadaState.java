package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class NoCursadaState implements SubjectState {

    public NoCursadaState() {}

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.GRIS);
    }

    @Override
    public void setSemestre(SubjectDecorator materia,int semestre) {
        materia.setSemestreMateria(0); // No aplica lanzar excepcion
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        materia.setState(new EnCursoState());
        materia.setEstadoColor(SemaforoColores.AMARILLO);
        materia.setSemestreMateria(1);  //semestreActual()
        System.out.println("Materia inscrita. Ahora está en curso.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        System.out.println("No puedes aprobar una materia no cursada.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        System.out.println("No puedes reprobar una materia no cursada.");
    }
}
