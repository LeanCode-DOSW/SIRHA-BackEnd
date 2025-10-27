package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.dosw.sirha.sirha_backend.domain.model.Decanate;
import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.domain.model.staterequest.BaseRequest;
import edu.dosw.sirha.sirha_backend.dto.RegisterRequestDecanate;
import edu.dosw.sirha.sirha_backend.dto.StudentDTO;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.DecanateService;
import edu.dosw.sirha.sirha_backend.service.RequestService;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import edu.dosw.sirha.sirha_backend.service.StudyPlanService;
import edu.dosw.sirha.sirha_backend.util.ValidationUtil;
import edu.dosw.sirha.sirha_backend.repository.mongo.DecanateMongoRepository;

@Service
public class DecanateServiceImpl implements DecanateService {

    private static final Logger log = LoggerFactory.getLogger(DecanateServiceImpl.class);

    private final DecanateMongoRepository decanateRepository;
    private final StudentService studentService;
    private final RequestService requestService;
    private final StudyPlanService studyPlanService;

    public DecanateServiceImpl(DecanateMongoRepository decanateRepository, StudentService studentService, RequestService requestService, StudyPlanService studyPlanService) {
        this.decanateRepository = decanateRepository;
        this.studentService = studentService;
        this.requestService = requestService;
        this.studyPlanService = studyPlanService;
    }

    @Transactional
    @Override
    public Decanate saveDecanate(Careers decanate) throws SirhaException {
        try {
            Decanate newDecanate = new Decanate(decanate);
            return decanateRepository.save(newDecanate);
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al guardar la decanatura", e);
        }
    }

    private Decanate registerDecanate(Decanate decanate) throws SirhaException {
        log.info("Registrando decanatura: {}", decanate.getName() != null ? decanate.getName() : decanate.getCareer());
        try {
            Decanate registered = decanateRepository.save(decanate);
            log.info("Decanatura registrada exitosamente: {} con ID: {}", registered.getName(), registered.getId());
            return registered;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al registrar decanatura: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Decanate registerDecanate(RegisterRequestDecanate request) throws SirhaException {
        log.info("Iniciando registro de decanatura: {}", request.getUsername());
        try {
            log.debug("Validando datos de registro para decanatura: {}", request.getUsername());
            ValidationUtil.validateDecanateRegistration( request.getUsername(), request.getEmail(), request.getPassword());

            // Verificar duplicados por nombre
            if (decanateRepository.findByName(request.getUsername()).isPresent()) {
                log.warn("Intento de registro fallido: Decanatura ya existe con nombre: {}", request.getUsername());
                throw SirhaException.of(ErrorCodeSirha.USERNAME_ALREADY_EXISTS, "La decanatura ya existe: " + request.getUsername());
            }

            log.debug("Creando nueva Decanate: {} ({})", request.getUsername(), request.getCareer());
            Decanate decanate = new Decanate(request.getUsername(), request.getCareer());

            Decanate saved = registerDecanate(decanate);
            log.info("Registro de decanatura completado: {} ({})", saved.getName(), saved.getCareer());
            return saved;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno durante el registro de la decanatura: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Decanate> getAllDecanates() throws SirhaException {
        try {
            return decanateRepository.findAll();
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al obtener todas las decanaturas", e);
        }
    }
    
    @Transactional
    @Override
    public Decanate getDecanateByName(String name) throws SirhaException {
        if (name == null || name.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        
        return decanateRepository.findByName(name)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND));
    }

    @Transactional
    @Override
    public List<BaseRequest> getAllRequests() throws SirhaException {
        try {
            List<Decanate> decanates = decanateRepository.findAll();
            List<BaseRequest> allRequests = new ArrayList<>();
            for (Decanate decanate : decanates) {
                allRequests.addAll(decanate.getPendingRequests());
            }
            return allRequests;
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al obtener todas las solicitudes", e);
        }
    }

    @Transactional
    @Override
    public List<BaseRequest> getAllRequestsForDecanate(String decanateName) throws SirhaException {
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND));
        
        return decanate.getPendingRequests();
    }

    @Transactional
    @Override
    public BaseRequest getRequestById(String requestId) throws SirhaException {
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El ID de la solicitud no puede estar vacío");
        }
        
        return requestService.findById(requestId)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.REQUEST_NOT_FOUND));
    }

    @Transactional
    @Override
    public BaseRequest receiveRequest(String decanateName, String requestId) throws SirhaException {
        // Validar parámetros
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El ID de la solicitud no puede estar vacío");
        }
        
        // Buscar decanatura
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND));
        
        // Buscar solicitud
        BaseRequest request = requestService.findById(requestId)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.REQUEST_NOT_FOUND));
        
        try {
            // Recibir solicitud (puede lanzar excepciones de dominio)
            decanate.receiveRequest(request);
            decanateRepository.save(decanate);
            return request;
        } catch (IllegalStateException e) {
            throw new SirhaException(ErrorCodeSirha.INVALID_STATE_TRANSITION, "No se puede recibir la solicitud: %s", e.getMessage());
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al recibir la solicitud", e);
        }
    }

    @Transactional
    @Override
    public BaseRequest approveRequest(String decanateName, String requestId) throws SirhaException {
        // Validar parámetros
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El ID de la solicitud no puede estar vacío");
        }
        
        // Buscar decanatura
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND));
        
        // Buscar solicitud
        BaseRequest request = requestService.findById(requestId)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.REQUEST_NOT_FOUND, "Solicitud no encontrada: %s", requestId));
        
        try {
            // Aprobar solicitud (puede lanzar excepciones de dominio)
            decanate.approveRequest(request);
            decanateRepository.save(decanate);
            return request;
        } catch (IllegalStateException e) {
            throw new SirhaException(ErrorCodeSirha.INVALID_STATE_TRANSITION, "No se puede aprobar la solicitud: %s", e.getMessage());
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al aprobar la solicitud", e);
        }
    }

    @Transactional
    @Override
    public BaseRequest rejectRequest(String decanateName, String requestId) throws SirhaException {
        // Validar parámetros
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El ID de la solicitud no puede estar vacío");
        }
        
        // Buscar decanatura
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND));
        
        // Buscar solicitud
        BaseRequest request = requestService.findById(requestId)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.REQUEST_NOT_FOUND));

        try {
            // Rechazar solicitud (puede lanzar excepciones de dominio)
            decanate.rejectRequest(request);
            decanateRepository.save(decanate);
            return request;
        } catch (IllegalStateException e) {
            throw new SirhaException(ErrorCodeSirha.INVALID_STATE_TRANSITION, "No se puede rechazar la solicitud: %s", e.getMessage());
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al rechazar la solicitud", e);
        }
    }

    @Transactional
    @Override
    public StudentDTO getStudentBasicInfo(String username) throws SirhaException {
        return studentService.getStudentBasicInfo(username);
    }

    @Transactional
    @Override
    public List<StudyPlan> getStudyPlansByDecanateName(String decanateName) throws SirhaException {
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND, "Decanatura no encontrada: %s", decanateName));
        
        return decanate.getStudyPlans();
    }

    @Transactional
    @Override
    public List<StudyPlan> addPlanToDecanate(String decanateName, String studyPlanName) throws SirhaException {
        if (decanateName == null || decanateName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre de la decanatura no puede estar vacío");
        }
        if (studyPlanName == null || studyPlanName.trim().isEmpty()) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre del plan de estudio no puede estar vacío");
        }

        StudyPlan studyPlan = studyPlanService.getStudyPlanByName(studyPlanName);
        
        Decanate decanate = decanateRepository.findByName(decanateName)
                .orElseThrow(() -> new SirhaException(ErrorCodeSirha.DECANATE_NOT_FOUND, "Decanatura no encontrada: %s", decanateName));
        
        boolean planExists = decanate.getStudyPlans().stream()
                .anyMatch(plan -> plan.getName().equals(studyPlan.getName()));
        
        if (planExists) {
            throw new SirhaException(ErrorCodeSirha.INVALID_ARGUMENT, "El plan de estudio ya existe: %s", studyPlan.getName());
        }
        
        try {
            decanate.addStudyPlan(studyPlan);
            decanateRepository.save(decanate);
            return decanate.getStudyPlans();
        } catch (Exception e) {
            throw new SirhaException(ErrorCodeSirha.DATABASE_ERROR, "Error al agregar el plan de estudio", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException {
        return studyPlanService.addSubjectToStudyPlan(studyPlanName, subjectName);
    }
    
    @Override
    @Transactional
    public StudyPlan saveStudyPlan(Careers career) throws SirhaException {
        StudyPlan studyPlan = new StudyPlan(career);
        return studyPlanService.saveStudyPlan(studyPlan);
    }

    @Transactional
    @Override
    public List<StudyPlan> getStudyPlansByCareer(Careers career) throws SirhaException {
        return studyPlanService.getStudyPlansByCareer(career);
    }

}
