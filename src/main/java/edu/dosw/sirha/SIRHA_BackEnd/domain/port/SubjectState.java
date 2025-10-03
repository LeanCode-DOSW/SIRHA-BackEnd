package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.*;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateGroup.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateSubjectDec.SubjectDecorator;

public interface SubjectState  {
    void setState(SubjectDecorator subject);
    void setSemester(SubjectDecorator subject, int semester);
    void setGroup(SubjectDecorator subject, Group group);
    void setGrade(SubjectDecorator subject, int grade);

    void inscribir(SubjectDecorator subject, Group group);
    void aprobar(SubjectDecorator subject);
    void reprobar(SubjectDecorator subject);
    void retirar(SubjectDecorator subject);

    boolean puedeInscribirse();
    boolean puedeAprobar();
    boolean puedeReprobar();
    boolean puedeRetirar();
    boolean tieneGrupoAsignado();
    String getEstadoNombre();

}
