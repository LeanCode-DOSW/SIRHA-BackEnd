package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class NoCursadaState implements SubjectState {

    public NoCursadaState() {}

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.GRIS);
    }

    @Override
    public void setSemestre(SubjectDecorator materia,int semestre) {
        materia.setSemestreMateria(semestre);
    }
    @Override
    public void setGroup(SubjectDecorator materia, Group grupo) {
        if (grupo != null) {
            materia.setGroupDirect(grupo);
        } else {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
    }

    @Override
    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.getState().setState(materia);
        materia.setGroup(grupo);
        System.out.println("Materia inscrita. Ahora está en curso.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede aprobar una materia no cursada");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede reprobar una materia no cursada");
    }
    @Override
    public void retirar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede retirar una materia no cursada");
    }

    @Override
    public boolean puedeInscribirse() { return true; }
    @Override
    public boolean puedeAprobar() { return false; }
    @Override
    public boolean puedeReprobar() { return false; }
    @Override
    public boolean puedeRetirar() { return false; }
    @Override
    public boolean tieneGrupoAsignado() { return false; }
    @Override
    public String getEstadoNombre() { return "No Cursada"; }
}
