package edu.dosw.sirha.SIRHA_BackEnd.domain.port;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;

/**
 * Interfaz que define las operaciones para manejar solicitudes.
 * Utilizando el patron de diseño template encontramos que existe un paso a paso a seguir para 
 * aprobar o rechazar, los cuales son:
 * - Verificar la solicitud(validateRequest)
 * - Aprobar o rechazar la solicitud(approveRequest)
 */
public interface Request {
    
    void pendingRequest(String comentario);
    void approveRequest(String comentario);
    void rejectRequest(String comentario);
    void reviewRequest(String comentario);

    boolean validateRequest();  //preguntamos : se puede resolver la solicitud?
    RequestStateEnum getActualState();
}
