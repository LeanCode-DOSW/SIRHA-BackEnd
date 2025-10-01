package edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.SubjectState;

public class ReprobadaState implements SubjectState {

    @Override
    public void setState(SubjectDecorator materia) {
        materia.setEstadoColor(SemaforoColores.ROJO);
    }

    @Override
    public void setSemestre(SubjectDecorator materia, int semestre) {
        throw new IllegalStateException("No se puede cambiar semestre de materia reprobada hasta aprobarla");
    }

    @Override
    public void setGroup(SubjectDecorator materia, Group grupo) {
        throw new IllegalStateException("No se puede cambiar grupo de materia reprobada hasta aprobarla");
    }

    @Override
    public void inscribir(SubjectDecorator materia, Group grupo) {
        materia.setState(new EnCursoState());
        materia.getState().setState(materia);
        materia.setGroup(grupo);
        System.out.println("Materia re-inscrita después de reprobar.");
    }

    @Override
    public void aprobar(SubjectDecorator materia) {
        materia.setState(new AprobadaState());
        materia.getState().setState(materia);
        System.out.println("Materia finalmente aprobada después de reprobar.");
    }

    @Override
    public void reprobar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede reprobar una materia que ya está reprobada");
    }
    @Override
    public void retirar(SubjectDecorator materia) {
        throw new IllegalStateException("No se puede retirar una materia reprobada");
    }
    
    @Override
    public boolean puedeInscribirse() { return true; }
    @Override
    public boolean puedeAprobar() { return true; }
    @Override
    public boolean puedeReprobar() { return false; }
    @Override
    public boolean puedeRetirar() { return false; }
    @Override
    public boolean tieneGrupoAsignado() { return true; }
    @Override
    public String getEstadoNombre() { return "Reprobada"; }
}
