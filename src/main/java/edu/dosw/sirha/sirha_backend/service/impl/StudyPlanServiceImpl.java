package edu.dosw.sirha.sirha_backend.service.impl;

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
        logger.debug("Obteniendo planes de estudio para la carrera: {}", career);
        List<StudyPlan> plans = studyPlanRepository.findByCareer(career);
        if (plans.isEmpty()) {
            throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND);
        }
        logger.debug("Planes de estudio encontrados: {}", plans.size());
        return plans;
    }

    @Override
    @Transactional
    public StudyPlan saveStudyPlan(StudyPlan studyPlan) throws SirhaException {
        if (studyPlan == null) {
            logger.error("El plan de estudio proporcionado es nulo");
            throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT);
        }
        logger.debug("Guardando plan de estudio: {}", studyPlan.getName());
        return studyPlanRepository.save(studyPlan);
    }

    @Transactional
    @Override
    public StudyPlan addSubjectToStudyPlan(String studyPlanName, String subjectName) throws SirhaException {
        try {
            logger.debug("Agregando materia '{}' al plan '{}'", subjectName, studyPlanName);
            StudyPlan studyPlan = studyPlanRepository.findStudyPlanByName(studyPlanName);
            if (studyPlan == null) {
                logger.error("Plan de estudio '{}' no encontrado", studyPlanName);
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND);
            }

            Subject subject = subjectService.findByName(subjectName);
            if (subject == null) {
                logger.error("Materia '{}' no encontrada", subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            logger.debug("Materia encontrada: {}", subject.getName());
            studyPlan.addSubject(subject);
            logger.debug("Materia agregada al plan de estudio: {}", studyPlan.getName());
            return studyPlanRepository.save(studyPlan);
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, e.getMessage());
        }
    }


    @Override
    public StudyPlan getStudyPlanByName(String name) throws SirhaException {
        try {
            logger.debug("Buscando plan de estudio por nombre: {}", name);
            StudyPlan studyPlan = studyPlanRepository.findStudyPlanByName(name);
            if (studyPlan == null) {
                logger.error("Plan de estudio '{}' no encontrado", name);
                throw SirhaException.of(ErrorCodeSirha.STUDY_PLAN_NOT_FOUND);
            }
            logger.debug("Plan de estudio encontrado: {}", studyPlan.getName());
            return studyPlan;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, e.getMessage());
        }
    }
}

