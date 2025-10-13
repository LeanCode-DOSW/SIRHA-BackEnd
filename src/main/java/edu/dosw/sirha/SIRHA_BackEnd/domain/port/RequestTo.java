package edu.dosw.sirha.sirha_backend.domain.port;
import edu.dosw.sirha.sirha_backend.domain.model.enums.*;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;

/**
 * Interfaz que define las operaciones para manejar solicitudes.
 * Utilizando el patron de diseño template encontramos que existe un paso a paso a seguir para 
 * aprobar o rechazar, los cuales son:
 * - Verificar la solicitud(validateRequest)
 * - Aprobar o rechazar la solicitud(approveRequest)
 */
public interface RequestTo {
    
    void approveRequest(ResponseRequest response) throws SirhaException;
    void rejectRequest(ResponseRequest response) throws SirhaException;
    void reviewRequest(ResponseRequest response) throws SirhaException;

    String getId();
    void setId(String id);

    boolean validateRequest();  //preguntamos : se puede resolver la solicitud?
    RequestStateEnum getActualState();
}
