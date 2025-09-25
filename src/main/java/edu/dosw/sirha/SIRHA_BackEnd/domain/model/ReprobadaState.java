package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class ReprobadaState implements SubjectState {

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.ROJO);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        materia.setSemestreMateria(semestre);
    }

    @Override
    public void agregarGrupo(SubjectDecorator materia, Group grupo) {
        System.out.println("No puedes agregar un grupo a una materia reprobada, tiene el grupo con el que viste y repobaste hasta que la vuelvas a ver.");
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        materia.setState(new EnCursoState());
        materia.setEstadoColor(SemaforoColores.AMARILLO);
        System.out.println("Reinscripción de materia reprobada.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        System.out.println("Debes cursar de nuevo antes de aprobar.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        System.out.println("Ya está reprobada.");
    }
}
