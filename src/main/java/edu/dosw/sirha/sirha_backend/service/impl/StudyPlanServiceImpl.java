package edu.dosw.sirha.sirha_backend.service.impl;

import edu.dosw.sirha.sirha_backend.domain.model.Semaforo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.StudyPlan;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.StudyPlanMongoRepository;
import edu.dosw.sirha.sirha_backend.service.StudyPlanService;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    private static final Logger logger = LoggerFactory.getLogger(StudyPlanServiceImpl.class);

    private final SubjectService subjectService;
    private final StudyPlanMongoRepository studyPlanRepository;

    public StudyPlanServiceImpl(SubjectService subjectService, StudyPlanMongoRepository studyPlanRepository) {
        this.subjectService = subjectService;
        this.studyPlanRepository = studyPlanRepository;
    }

    @Transactional
    @Override
    public List<StudyPlan> getStudyPlansByCareer(Careers career) throws SirhaException {
        logger.info("Obteniendo planes de estudio para la carrera: {}", career);
        try {
            List<StudyPlan> plans = studyPlanRepository.findByCareer(career);
            if (plans.isEmpty()) {
                logger.warn("No se encontraron planes de estudio para la carrera: {}", career);
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND);
            }
            logger.info("Planes de estudio encontrados ({}) para la carrera: {}", plans.size(), career);
            return plans;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al obtener planes por carrera", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al obtener planes de estudio", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan saveStudyPlan(StudyPlan studyPlan) throws SirhaException {
        logger.info("Guardando plan de estudio");
        try {
            if (studyPlan == null) {
                logger.error("El plan de estudio proporcionado es nulo");
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El plan de estudio no puede ser null");
            }
            logger.debug("Guardando plan de estudio: {}", studyPlan.getName());
            StudyPlan saved = studyPlanRepository.save(studyPlan);
            logger.info("Plan de estudio guardado: {}", saved.getName());
            return saved;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al guardar plan de estudio", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al guardar plan de estudio", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan createStudyPlan(Careers career) throws SirhaException {
        logger.info("Creando plan de estudio para la carrera: {}", career);
        try {
            if (career == null) {
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La carrera no puede ser null");
            }

            StudyPlan studyPlan = new StudyPlan(career);
            StudyPlan saved = studyPlanRepository.save(studyPlan);
            logger.info("Plan de estudio creado: {}", saved.getName());
            return saved;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al crear plan de estudio", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al crear plan de estudio", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException {
        try {
            logger.info("Agregando materia '{}' al plan '{}'", subjectName, studyPlanName);

            // Buscar plan
            StudyPlan studyPlan = studyPlanRepository.findStudyPlanByName(studyPlanName);
            if (studyPlan == null) {
                logger.error("Plan de estudio '{}' no encontrado", studyPlanName);
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND, "Plan no encontrado: " + studyPlanName);
            }

            // Buscar materia
            Subject subject = subjectService.findByName(subjectName);
            if (subject == null) {
                logger.error("Materia '{}' no encontrada", subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "Materia no encontrada: " + subjectName);
            }

            // Verificar si ya existe
            if (studyPlan.hasSubject(subject.getId())) {
                logger.warn("La materia '{}' ya existe en el plan '{}'", subjectName, studyPlanName);
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "La materia ya existe en el plan");
            }

            // Agregar materia (solo el ID)
            studyPlan.addSubject(subject);
            StudyPlan saved = studyPlanRepository.save(studyPlan);

            logger.info("Materia '{}' agregada al plan '{}'", subjectName, saved.getName());
            return saved;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al agregar materia al plan", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al agregar materia", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan removeSubjectFromStudyPlan(String studyPlanName, String subjectName) throws SirhaException {
        try {
            logger.info("Eliminando materia '{}' del plan '{}'", subjectName, studyPlanName);

            StudyPlan studyPlan = studyPlanRepository.findStudyPlanByName(studyPlanName);
            if (studyPlan == null) {
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND);
            }

            Subject subject = subjectService.findByName(subjectName);
            if (subject == null) {
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            studyPlan.removeSubject(subject);
            StudyPlan saved = studyPlanRepository.save(studyPlan);

            logger.info("Materia '{}' eliminada del plan '{}'", subjectName, studyPlanName);
            return saved;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al eliminar materia del plan", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al eliminar materia", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan getStudyPlanByName(String name) throws SirhaException {
        try {
            logger.info("Buscando plan de estudio por nombre: {}", name);

            if (name == null || name.trim().isEmpty()) {
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El nombre no puede estar vacío");
            }

            StudyPlan studyPlan = studyPlanRepository.findStudyPlanByName(name);
            if (studyPlan == null) {
                logger.warn("Plan de estudio '{}' no encontrado", name);
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND, "Plan no encontrado: " + name);
            }

            logger.info("Plan de estudio encontrado: {}", studyPlan.getName());
            return studyPlan;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar plan por nombre", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al buscar plan", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan getStudyPlanById(String id) throws SirhaException {
        try {
            logger.info("Buscando plan de estudio por ID: {}", id);

            if (id == null || id.trim().isEmpty()) {
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El ID no puede estar vacío");
            }

            StudyPlan studyPlan = studyPlanRepository.findById(id)
                    .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND, "Plan no encontrado con ID: " + id));

            logger.info("Plan de estudio encontrado: {}", studyPlan.getName());
            return studyPlan;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al buscar plan por ID", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al buscar plan", e);
        }
    }

    @Transactional
    @Override
    public List<StudyPlan> getAllStudyPlans() throws SirhaException {
        try {
            logger.info("Obteniendo todos los planes de estudio");
            List<StudyPlan> plans = studyPlanRepository.findAll();
            logger.info("Total de planes encontrados: {}", plans.size());
            return plans;
        } catch (Exception e) {
            logger.error("Error al obtener todos los planes", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al obtener planes", e);
        }
    }

    @Transactional
    @Override
    public StudyPlan deleteStudyPlan(String name) throws SirhaException {
        try {
            logger.info("Eliminando plan de estudio: {}", name);

            StudyPlan studyPlan = getStudyPlanByName(name);
            studyPlanRepository.delete(studyPlan);

            logger.info("Plan de estudio '{}' eliminado", name);
            return studyPlan;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al eliminar plan", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al eliminar plan", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return studyPlanRepository.findStudyPlanByName(name) != null;
    }

    // ✅ NUEVO: Obtener las materias completas de un plan
    @Transactional
    @Override
    public List<Subject> getSubjectsOfStudyPlan(String studyPlanName) throws SirhaException {
        try {
            logger.info("Obteniendo materias del plan: {}", studyPlanName);

            StudyPlan studyPlan = getStudyPlanByName(studyPlanName);
            List<Subject> subjects = new ArrayList<>();

            // Cargar cada materia por su ID
            for (String subjectId : studyPlan.getSubjectIds()) {
                try {
                    Subject subject = subjectService.findById(subjectId);
                    subjects.add(subject);
                } catch (SirhaException e) {
                    logger.warn("Materia con ID '{}' no encontrada en el plan '{}'", subjectId, studyPlanName);
                    // Continuar con las demás materias
                }
            }

            logger.info("Total de materias cargadas: {}", subjects.size());
            return subjects;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al obtener materias del plan", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al obtener materias", e);
        }
    }

    // ✅ NUEVO: Calcular créditos totales
    @Transactional
    @Override
    public int calculateTotalCredits(String studyPlanName) throws SirhaException {
        try {
            logger.info("Calculando créditos totales del plan: {}", studyPlanName);

            List<Subject> subjects = getSubjectsOfStudyPlan(studyPlanName);
            int totalCredits = subjects.stream()
                    .mapToInt(Subject::getCredits)
                    .sum();

            logger.info("Créditos totales del plan '{}': {}", studyPlanName, totalCredits);
            return totalCredits;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al calcular créditos", e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al calcular créditos", e);
        }
    }
    @Override
    public Semaforo createSemaforoForStudent(String studyPlanId) throws SirhaException {
        try {
            // 1. Obtener el plan
            StudyPlan studyPlan = getStudyPlanById(studyPlanId);

            // 2. Obtener las materias
            List<Subject> subjects = getSubjectsOfStudyPlan(studyPlan.getName());

            // 3. Calcular créditos
            int totalCredits = calculateTotalCredits(studyPlan.getName());

            // 4. Crear y devolver el Semaforo
            return new Semaforo(
                    studyPlan.getId(),
                    studyPlan.getCareer(),
                    totalCredits,
                    subjects
            );

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error al crear semáforo", e);
        }
    }
}