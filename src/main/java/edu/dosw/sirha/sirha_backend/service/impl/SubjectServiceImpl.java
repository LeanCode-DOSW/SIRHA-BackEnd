package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.domain.port.PrerequisiteRule;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.sirha_backend.service.GroupService;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Transactional
    @Override
    public Subject findById(String id) throws SirhaException {
        try {
            if (id == null || id.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "El ID no puede ser null o vacío");
            }

            log.info("Buscando materia por ID: {}", id);
            return subjectRepository.findById(id)
                    .orElseThrow(() -> SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "Materia no encontrada con ID: " + id));

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error interno al buscar materia por ID '{}': {}", id, e.getMessage(), e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al buscar materia");
        }
    }

    private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectMongoRepository subjectRepository;
    private final GroupService groupService; // Inyección para delegación

    public SubjectServiceImpl(SubjectMongoRepository subjectRepository, GroupService groupService) {
        this.subjectRepository = subjectRepository;
        this.groupService = groupService;
        log.info("SubjectServiceImpl inicializado correctamente");
    }

    @Transactional
    @Override
    public List<Subject> findAll() {
        log.info("Consultando todas las materias");
        return subjectRepository.findAll();
    }

    @Transactional
    @Override
    public Subject findByName(String name) throws SirhaException {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            log.info("Buscando materia por nombre: {}", name);
            Subject subject = subjectRepository.findByName(name).orElse(null);

            if (subject == null) {
                log.warn(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(), "{}", name);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            log.info("Materia encontrada - Nombre: {}, Créditos: {}",
                    subject.getName(), subject.getCredits());
            return subject;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception i) {
            log.error("Error inesperado al buscar materia por nombre '{}': {}", name, i.getMessage(), i);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al buscar materia");
        }
    }

    @Transactional
    @Override
    public Subject save(Subject subject) throws SirhaException {
        try {
            if (subject == null) {
                log.error("Error: La materia no puede ser null");
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "La materia no puede ser null");
            }

            log.info("Guardando materia: {} (Créditos: {})",
                    subject.getName(), subject.getCredits());
            boolean exists = subjectRepository.existsByName(subject.getName());
            if (exists) {
                log.warn("Materia con nombre '{}' ya existe", subject.getName());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_EXISTS, "Materia con nombre '" + subject.getName() + "' ya existe");
            }

            Subject savedSubject = subjectRepository.save(subject);

            log.info("Materia guardada exitosamente - ID: {}, Nombre: {}",
                    savedSubject.getId(), savedSubject.getName());

            return savedSubject;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al guardar materia: {}", e.getMessage(), e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al guardar materia");
        }
    }

    @Transactional
    @Override
    public Subject deleteByName(String name) throws SirhaException {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            log.info("Eliminando materia por nombre: {}", name);
            Subject subject = subjectRepository.findByName(name).orElse(null);

            if (subject == null) {
                log.warn("Intento de eliminar materia inexistente: {}", name);
                return null;
            }

            log.info("Materia encontrada para eliminar - Nombre: {}, Grupos asociados: {}",
                    subject.getName(), subject.getGroups().size());

            if (!subject.getGroups().isEmpty()) {
                log.warn("Materia '{}' tiene {} grupos asociados, eliminando grupos primero",
                        name, subject.getGroups().size());

                // DELEGACIÓN a GroupService
                List<Group> deletedGroups = groupService.deleteBySubjectName(name);
                log.info("Grupos eliminados asociados a materia '{}' - Total: {}",
                        name, deletedGroups.size());
                subject.deleteGroups();
            }

            subjectRepository.delete(subject);

            log.info("Materia eliminada exitosamente: {}", name);
            return subject;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al eliminar materia: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public boolean existsByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.debug("Verificación de existencia con nombre null/vacío, retornando false");
                return false;
            }

            log.debug("Verificando existencia de materia por nombre: {}", name);
            boolean exists = subjectRepository.existsByName(name);

            log.debug("Materia '{}' existe: {}", name, exists);
            return exists;

        } catch (Exception e) {
            log.error("Error al verificar existencia de materia: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public Subject addPrerequisite(String subjectName, PrerequisiteRule prerequisite) throws SirhaException {
        log.info("Agregando prerrequisito a la materia: {}", subjectName);
        try {
            if (subjectName == null || subjectName.isEmpty()) {
                log.error("Error: El nombre de la materia no puede ser null o vacío");
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "El nombre de la materia no puede ser null o vacío");
            }
            if (prerequisite == null) {
                log.error("Error: El prerrequisito no puede ser null");
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT, "El prerrequisito no puede ser null");
            }
            Subject subject = subjectRepository.findByName(subjectName).orElse(null);

            if (subject == null) {
                log.warn("No se encontró la materia con nombre: {}", subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND, "Materia no encontrada: " + subjectName);
            }

            subject.addPrerequisite(prerequisite);
            Subject updatedSubject = subjectRepository.save(subject);
            log.info("Prerrequisito agregado exitosamente a la materia: {}", subjectName);
            return updatedSubject;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al agregar prerrequisito: " + e.getMessage(), e);
        }
    }

    // DELEGACIÓN a GroupService para consultas de grupos
    @Transactional
    @Override
    public List<Group> getGroupsBySubjectName(String subjectName) throws SirhaException {
        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            log.info("Obteniendo grupos de la materia: {}", subjectName);

            // Verificar que la materia existe
            Subject subject = subjectRepository.findByName(subjectName).orElse(null);
            if (subject == null) {
                log.error(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(), subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            // DELEGAR a GroupService
            List<Group> groups = groupService.findBySubjectName(subjectName);

            log.info("Grupos obtenidos de materia '{}' - Total: {}", subjectName, groups.size());
            return groups;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener grupos de materia: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Group> getOpenGroupsBySubjectName(String subjectName) throws SirhaException {
        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            log.info("Obteniendo grupos abiertos de la materia: {}", subjectName);

            // Verificar que la materia existe
            Subject subject = subjectRepository.findByName(subjectName).orElse(null);
            if (subject == null) {
                log.error(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(), subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            // DELEGAR a GroupService
            List<Group> openGroups = groupService.findOpenGroupsBySubjectName(subjectName);

            log.info("Grupos abiertos obtenidos de materia '{}' - Total: {}", subjectName, openGroups.size());
            return openGroups;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR, "Error interno al obtener grupos abiertos: " + e.getMessage(), e);
        }
    }
}