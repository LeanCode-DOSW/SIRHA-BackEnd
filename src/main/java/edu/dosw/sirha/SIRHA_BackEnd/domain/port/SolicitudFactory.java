package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Subject;

public interface SolicitudFactory {
    BaseRequest requestConstructor(Subject previosSubject, Group previousGroup, Subject newSubject, Group newGroup, String motivo);
}
