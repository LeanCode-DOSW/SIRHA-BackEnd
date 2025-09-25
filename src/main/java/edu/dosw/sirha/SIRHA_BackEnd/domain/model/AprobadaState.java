package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class AprobadaState implements SubjectState {
    private static final Logger logger = LoggerFactory.getLogger(AprobadaState.class);

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.VERDE);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        throw new IllegalStateException("No se puede cambiar semestre de materia aprobada");
    }
    @Override
    public void setGroup(SubjectDecorator materia, Group grupo) {
        throw new IllegalStateException("No se puede asignar grupo a materia aprobada");
    }

    @Override
    public void inscribir(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede inscribir una materia ya aprobada");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        logger.info("Intento de aprobar materia que ya está aprobada: {}", 
                   materia.getSubject().getName());
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede reprobar una materia aprobada");
    }
    @Override
    public void retirar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede retirar una materia aprobada");
    }
    
    @Override
    public boolean puedeInscribirse() { return false; }
    @Override
    public boolean puedeAprobar() { return false; }
    @Override
    public boolean puedeReprobar() { return false; }
    @Override
    public boolean puedeRetirar() { return false; }
    @Override
    public boolean tieneGrupoAsignado() { return true; }
    @Override
    public String getEstadoNombre() { return "Aprobada"; }
}
