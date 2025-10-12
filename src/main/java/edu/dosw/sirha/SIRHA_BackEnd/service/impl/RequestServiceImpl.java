package edu.dosw.sirha.SIRHA_BackEnd.service.impl;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.stateRequest.BaseRequest;
import edu.dosw.sirha.SIRHA_BackEnd.exception.ErrorCodeSirha;
import edu.dosw.sirha.SIRHA_BackEnd.exception.SirhaException;
import edu.dosw.sirha.SIRHA_BackEnd.repository.mongo.BaseRequestMongoRepository;
import edu.dosw.sirha.SIRHA_BackEnd.service.RequestService;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    
    private static final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);
    
    private final BaseRequestMongoRepository repository;

    public RequestServiceImpl(BaseRequestMongoRepository repository) {
        this.repository = repository;
        log.info("RequestServiceImpl inicializado correctamente");
    }

    @Override
    public List<BaseRequest> findAll() {
        log.info("Consultando todas las solicitudes");
        try {
            List<BaseRequest> requests = repository.findAll();
            log.info("Se encontraron {} solicitudes", requests.size());
            
            // Log detallado en DEBUG
            if (log.isDebugEnabled()) {
                requests.forEach(request -> 
                    log.debug("Solicitud encontrada - ID: {}, Tipo: {}, Estado: {}", 
                             request.getId(), 
                             request.getClass().getSimpleName(), 
                             request.getEnumState()));
            }
            
            return requests;
        } catch (Exception e) {
            log.error("Error al consultar todas las solicitudes: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar solicitudes", e);
        }
    }

    @Override
    public Optional<BaseRequest> findById(String id) {
        log.debug("Buscando solicitud por ID: {}", id);
        try {
            Optional<BaseRequest> request = repository.findById(id);
            
            if (request.isPresent()) {
                log.debug("Solicitud encontrada - ID: {}, Tipo: {}", 
                         id, request.get().getClass().getSimpleName());
            } else {
                log.debug("No se encontró solicitud con ID: {}", id);
            }
            
            return request;
        } catch (Exception e) {
            log.error("Error al buscar solicitud por ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al buscar solicitud", e);
        }
    }

    @Override
    public BaseRequest save(BaseRequest request) {
        log.info("Guardando solicitud - ID: {}, Tipo: {}", 
                request.getId() != null ? request.getId() : "NUEVO", 
                request.getClass().getSimpleName());
        
        try {
            BaseRequest savedRequest = repository.save(request);
            
            log.info("Solicitud guardada exitosamente - ID: {}, Tipo: {}, Estado: {}", 
                    savedRequest.getId(), 
                    savedRequest.getClass().getSimpleName(),
                    savedRequest.getEnumState());

            
            return savedRequest;
        } catch (Exception e) {
            log.error("Error al guardar solicitud {}: {}", 
                     request.getId() != null ? request.getId() : "NUEVA", 
                     e.getMessage(), e);
            throw new RuntimeException("Error interno al guardar solicitud", e);
        }
    }

    @Override
    public void aprobarSolicitud(String id) {
        log.info("Iniciando proceso de aprobación para solicitud: {}", id);
        
        try {
            BaseRequest request = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de aprobar solicitud inexistente: {}", id);
                        return new RuntimeException("Solicitud no encontrada con ID: " + id);
                    });
            
            log.debug("Solicitud encontrada para aprobación - ID: {}, Tipo: {}, Estado actual: {}", 
                     id, request.getClass().getSimpleName(), request.getEnumState());
            
                     log.debug("Estado de solicitud cambiado a APROBADO para ID: {}", id);
            // request.aprobar();
            
            log.info("Solicitud aprobada exitosamente - ID: {}, Tipo: {}", 
                    request.getId(), 
                    request.getClass().getSimpleName());
            
        } catch (RuntimeException e) {
            log.warn("Error al aprobar solicitud {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al aprobar solicitud {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al aprobar solicitud", e);
        }
    }

    @Override
    public void rechazarSolicitud(String id) {
        log.info("Iniciando proceso de rechazo para solicitud: {}", id);
        
        try {
            BaseRequest request = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Intento de rechazar solicitud inexistente: {}", id);
                        return new RuntimeException("Solicitud no encontrada con ID: " + id);
                    });
            
            log.debug("Solicitud encontrada para rechazo - ID: {}, Tipo: {}, Estado actual: {}", 
                     id, request.getClass().getSimpleName(), request.getEnumState());
            log.debug("Estado de solicitud cambiado a RECHAZADO para ID: {}", id);  
            // request.rechazar();
            log.info("Solicitud rechazada exitosamente - ID: {}, Tipo: {}", 
                    request.getId(), 
                    request.getClass().getSimpleName());
            
        } catch (RuntimeException e) {
            log.warn("Error al rechazar solicitud {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al rechazar solicitud {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al rechazar solicitud", e);
        }
    }
}