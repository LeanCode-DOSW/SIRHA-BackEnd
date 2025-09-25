package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class EnCursoState implements SubjectState {

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.AMARILLO);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        throw new IllegalStateException("No se puede cambiar semestre mientras está en curso");
    }
    @Override
    public void setGroup(SubjectDecorator materia, Group grupo) {
        throw new IllegalStateException("La materia ya tiene un grupo asignado");
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        throw new IllegalStateException("La materia ya está inscrita");
    }
    @Override
    public void retirar(SubjectDecorator materia) {
        /*if (materia.getGroup() != null) {
            materia.getGroup().desinscribirEstudiante();
        }*/
        materia.setState(new NoCursadaState());
        materia.getState().setState(materia);
        materia.setGroup(null);
        System.out.println("Materia retirada.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        materia.setState(new AprobadaState());
        materia.getState().setState(materia);
        System.out.println("Materia aprobada.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        materia.setState(new ReprobadaState());
        materia.getState().setState(materia); 
        System.out.println("Materia reprobada.");
    }

    @Override
    public boolean puedeInscribirse() { return false; }
    @Override
    public boolean puedeAprobar() { return true; }
    @Override
    public boolean puedeReprobar() { return true; }
    @Override
    public boolean puedeRetirar() { return true; }
    @Override
    public boolean tieneGrupoAsignado() { return true; }
    @Override
    public String getEstadoNombre() { return "En Curso"; }
}
