package edu.dosw.sirha.SIRHA_BackEnd.domain.port;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.*;

/**
 * Interfaz que define las operaciones para manejar solicitudes.
 * Utilizando el patron de diseño template encontramos que existe un paso a paso a seguir para 
 * aprobar o rechazar, los cuales son:
 * - Verificar la solicitud(valideResquest)
 * - cambiar el estado de la solicitud(setState)
 * - Aprobar o rechazar la solicitud(approveRequest)
 * - procesar la solicitud(proccessRequest)
 * - cambiar el estado de la solicitud (setState)
 */
public interface Request {

    boolean valideResquest();
    AdminState approveRequest();
    void proccessRequest();

    // metodos para validar la solicitud
    boolean validar();
    boolean aplicar();
}
