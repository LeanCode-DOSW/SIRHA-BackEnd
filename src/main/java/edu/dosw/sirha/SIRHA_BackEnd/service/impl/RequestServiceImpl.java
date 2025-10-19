package edu.dosw.sirha.sirha_backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.Student;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.BaseRequestMongoRepository;
import edu.dosw.sirha.sirha_backend.service.RequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    
    private static final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);
    
    private final BaseRequestMongoRepository repository;
    private final StudentServiceImpl studentService;

    public RequestServiceImpl(BaseRequestMongoRepository repository, StudentServiceImpl studentService) {
        this.repository = repository;
        this.studentService = studentService;
        log.info("RequestServiceImpl inicializado correctamente");
    }

    @Transactional
    @Override
    public List<BaseRequest> findAll() throws SirhaException {
        log.info("Consultando todas las solicitudes");
        try {
            List<BaseRequest> requests = repository.findAll();
            log.info("Se encontraron {} solicitudes", requests.size());

            requests.forEach(request ->
                log.debug("Solicitud encontrada - ID: {}, Tipo: {}, Estado: {}",
                             request.getId(), 
                             request.getClass().getSimpleName(), 
                             request.getEnumState()));
            
            return requests;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar todas las solicitudes: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Optional<BaseRequest> findById(String id) throws SirhaException {
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
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar solicitud: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public BaseRequest save(BaseRequest request) throws SirhaException {
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
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al guardar solicitud: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public BaseRequest deleteById(String id) throws SirhaException {
        log.info("Eliminando solicitud por ID: {}", id);
        try {
            Optional<BaseRequest> existingRequest = findById(id);
            if (existingRequest.isEmpty()) {
                log.warn("No se encontró solicitud con ID: {} para eliminar", id);
                throw SirhaException.of(ErrorCodeSirha.REQUEST_NOT_FOUND, "Solicitud no encontrada");
            }
            Student student = existingRequest.get().getStudent();
            student.removeRequest(existingRequest.get());
            repository.deleteById(id);
            log.info("Solicitud eliminada exitosamente - ID: {}", id);
            
            return existingRequest.get();
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar solicitud: " + e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public List<BaseRequest> getAllRequests(String studentUsername) throws SirhaException {
        log.info("Consultando solicitudes para el estudiante: {}", studentUsername);
        try {
            List<BaseRequest> requests = studentService.getAllRequests(studentUsername);
            log.info("Se encontraron {} solicitudes para el estudiante {}", requests.size(), studentUsername);
            return requests;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar solicitudes: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public BaseRequest getRequestById(String studentUsername, String requestId) throws SirhaException {
        log.info("Consultando solicitud ID: {} para el estudiante: {}", requestId, studentUsername);
        try {
            BaseRequest request = studentService.getRequestById(studentUsername, requestId);
            log.info("Solicitud encontrada - ID: {}, Tipo: {}, Estado: {}", 
                    request.getId(), 
                    request.getClass().getSimpleName(),
                    request.getEnumState());
            return request;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar solicitud: " + e.getMessage(), e);
        }
    }
    @Transactional
    @Override
    public List<BaseRequest> getRequestsHistory(String studentUsername) throws SirhaException {
        log.info("Consultando historial de solicitudes para el estudiante: {}", studentUsername);
        try {
            List<BaseRequest> requests = studentService.getRequestsHistory(studentUsername);
            log.info("Se encontraron {} solicitudes en el historial para el estudiante {}", requests.size(), studentUsername);
            return requests;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al consultar historial de solicitudes: " + e.getMessage(), e);
        }
    }
}