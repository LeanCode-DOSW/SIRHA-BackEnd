package edu.dosw.sirha.sirha_backend.domain.model.statesubjectdec;
import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.SubjectState;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

public class ReprobadaState implements SubjectState {

    public void setState(SubjectDecorator materia) {materia.setEstadoColor(SemaforoColores.ROJO);}
    public void setSemester(SubjectDecorator materia, int semester) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_SEMESTER,"No se puede cambiar semestre de materia reprobada hasta aprobarla");}
    public void setGroup(SubjectDecorator materia, Group grupo) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GROUP,"No se puede cambiar grupo de materia reprobada hasta aprobarla");}
    public void setGrade(SubjectDecorator materia, int grade) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_CHANGE_GRADE,"No se puede cambiar la nota de una materia reprobada");}

    public void inscribir(SubjectDecorator materia, Group grupo) throws SirhaException {
        materia.setState(new EnCursoState());
        materia.setGroup(grupo);        
        materia.getState().setState(materia);
    }

    public void aprobar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_APPROVE,"No se puede aprobar una materia reprobada sin inscribirse de nuevo");}
    public void reprobar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_FAIL,"No se puede reprobar una materia que ya está reprobada");}
    public void retirar(SubjectDecorator materia) throws SirhaException {throw SirhaException.of(ErrorCodeSirha.CANNOT_DROP_SUBJECT,"No se puede retirar una materia reprobada");}

    public boolean canEnroll() { return true; }
    public boolean canApprove() { return true; }
    public boolean canFail() { return false; }
    public boolean canDropSubject() { return false; }
    public boolean hasAssignedGroup() { return true; }
    public String getStatusName() { return "Reprobada"; }
}
