package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;

public class AprobadaState extends AbstractSubjectState {

    @Override
    public void setState(SubjectDecorator materia) { materia.setEstadoColor(SemaforoColores.VERDE); }

    @Override
    public boolean hasAssignedGroup() { return true; }

    @Override
    public String getStatusName() { return "Aprobada"; }
}
