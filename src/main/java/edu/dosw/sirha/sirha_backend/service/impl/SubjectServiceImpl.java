package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.exception.ErrorCodeSirha;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.sirha_backend.service.GroupService;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectMongoRepository subjectRepository;
    private final GroupService groupService;

    public SubjectServiceImpl(SubjectMongoRepository subjectRepository, GroupService groupService ) {
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
                log.warn(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(),"{}", name);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }

            log.info("Materia encontrada - Nombre: {}, Créditos: {}", 
                        subject.getName(), subject.getCredits());
            return subject;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception i) {
            log.error("Error inesperado al buscar materia por nombre '{}': {}", name, i.getMessage(), i);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar materia");
        }
    }

    @Transactional
    @Override
    public Subject save(Subject subject) throws SirhaException {
        try {
            if (subject == null) {
                log.error("Error: La materia no puede ser null");
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND,"La materia no puede ser null");
            }

            log.info("Guardando materia: {} (Créditos: {})", 
                    subject.getName(), subject.getCredits());
            boolean exists = subjectRepository.existsByName(subject.getName());
            if (exists) {
                log.warn("Materia con nombre '{}' ya existe", subject.getName());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_ALREADY_EXISTS,"Materia con nombre '" + subject.getName() + "' ya existe");
            }
            
            Subject savedSubject = subjectRepository.save(subject);
            
            log.info("Materia guardada exitosamente - ID: {}, Nombre: {}", 
                    savedSubject.getId(), savedSubject.getName());
            
            return savedSubject;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al guardar materia: {}", e.getMessage(), e);
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al guardar materia");
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
                
                List<Group> deletedGroups = deleteGroupsBySubjectName(name);
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
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar materia: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    @Override
    public boolean existsByName(String name) throws SirhaException {
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
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al verificar existencia de materia: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Group> getGroupsOfSubject(String subjectName) throws SirhaException {
        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            
            log.info("Obteniendo grupos de la materia: {}", subjectName);

            Subject subject = subjectRepository.findByName(subjectName).orElse(null);

            if (subject == null) {
                log.error(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(), subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            
            List<Group> groups = subject.getGroups();
            
            log.info("Grupos obtenidos de materia '{}' - Total: {}", subjectName, groups.size());

            return groups;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener grupos de materia: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Group> getOpenGroupsOfSubject(String subjectName) throws SirhaException {

        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            
            log.info("Obteniendo grupos abiertos de la materia: {}", subjectName);

            Subject subject = subjectRepository.findByName(subjectName).orElse(null);

            if (subject == null) {
                log.error(ErrorCodeSirha.SUBJECT_NOT_FOUND.getDefaultMessage(), subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            
            List<Group> openGroups = subject.getOpenGroups();
            
            log.info("Grupos abiertos obtenidos de materia '{}' - Total: {}", subjectName, openGroups.size());
            
            return openGroups;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener grupos abiertos: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Group getGroupById(String id) throws SirhaException {
        try {
            if (id == null || id.trim().isEmpty()) {
                log.error("Error: ID de grupo no puede ser null o vacío");
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND,"El ID del grupo no puede ser null o vacío");
            }
            
            log.info("Obteniendo grupo por ID: {}", id);
            
            Group group = groupService.findById(id);
            
            if (group == null) {
                log.warn("Grupo no encontrado con ID: {}", id);
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND,"Grupo no encontrado con ID: " + id);
            }
            log.info("Grupo encontrado - ID: {}, Código: {}", id, group.getCode());
            return group;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener grupo: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Group saveGroup(String subjectName, Group group) throws SirhaException {

        try {
            if (subjectName == null || subjectName.trim().isEmpty()) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND);
            }
            
            if (group == null) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
            }
            
            log.info("Guardando grupo para la materia: {} - Capacidad: {}", 
                    subjectName, group.getCapacity());

            Subject subject = subjectRepository.findByName(subjectName).orElse(null);

            if (subject == null) {
                log.error("No se encontró la materia con nombre: {}", subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND,"Materia no encontrada: " + subjectName);
            }
            
            Group savedGroup = groupService.saveGroup(subject, group);

            subjectRepository.save(subject);
            
            log.info("Grupo guardado exitosamente para materia '{}' - ID: {}, Código: {}", 
                    subjectName, savedGroup.getId(), savedGroup.getCode());
            
            return savedGroup;
            
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al guardar grupo: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Group deleteGroupById(String id) throws SirhaException {
        try {
            if (id == null || id.trim().isEmpty()) {
                log.error("Error: ID de grupo no puede ser null o vacío");
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND,"El ID del grupo no puede ser null o vacío");
            }
            
            log.info("Eliminando grupo por ID: {}", id);
                        
            Group deletedGroup = groupService.deleteGroupById(id);
            
            log.info("Grupo eliminado exitosamente - ID: {}, Código: {}", 
                    id, deletedGroup != null ? deletedGroup.getCode() : "null");
            
            return deletedGroup;

        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar grupo: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    @Override
    public boolean existsGroupById(String id) throws SirhaException {

        try {
            if (id == null || id.trim().isEmpty()) {
                log.debug("Verificación de existencia de grupo con ID null/vacío, retornando false");
                return false;
            }
            
            log.debug("Verificando existencia de grupo por ID: {}", id);
            
            Group group = groupService.findById(id);
            boolean exists = group != null;
            
            log.debug("Grupo con ID '{}' existe: {}", id, exists);
            return exists;
        } catch (Exception e) {
            log.warn("Error al verificar existencia de grupo '{}': {}, retornando false", id, e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public List<Group> findAllGroups() throws SirhaException {
        log.info("Consultando todos los grupos");
        
        try {
            List<Group> groups = groupService.findAllGroups();
            
            log.info("Todos los grupos consultados exitosamente - Total: {}", groups.size());
            
            return groups;
        } catch (SirhaException e) {
            throw e;    
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar grupo: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Group assignProfessor(String groupId, Professor professor) throws SirhaException {
        if (groupId == null) {
            log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
            throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
        }
        
        if (professor == null) {
            log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
            throw SirhaException.of(ErrorCodeSirha.PROFESSOR_NOT_FOUND);
        }
        
        log.info("Asignando profesor {} al grupo con ID: {}", professor.getUsername(), groupId);
        
        try {
            Group updatedGroup = groupService.assignProfessor(groupId, professor);
            
            log.info("Profesor asignado exitosamente al grupo ID: {}", groupId);
            return updatedGroup;
        } catch (SirhaException e) {
            throw e;  
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al asignar profesor: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Group addSchedule(String groupId, Schedule schedule) throws SirhaException {
        try {
            if (groupId == null) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
            }
            
            if (schedule == null) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.INVALID_ARGUMENT,"El horario no puede ser null");
            }
            
            log.info("Agregando horario al grupo con ID: {}", groupId);
                   
            Group updatedGroup = groupService.addSchedule(groupId, schedule);
            
            log.info("Horario agregado exitosamente al grupo ID: {}", groupId);
            return updatedGroup;
        } catch (SirhaException e) {
            throw e; 
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al agregar horario: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Professor getProfessor(String groupId) throws SirhaException {
        try {
            if (groupId == null) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
            }
            
            log.info("Obteniendo profesor del grupo con ID: {}", groupId);
                        
            Professor professor = groupService.getProfessor(groupId);
            
            if (professor == null) {
                log.info("Grupo {} no tiene profesor asignado", groupId);
                throw SirhaException.of(ErrorCodeSirha.PROFESSOR_NOT_FOUND,"El grupo no tiene profesor asignado");
            }

            log.info("Profesor encontrado para grupo {} - Nombre: {}", groupId, professor.getUsername());
            return professor;
        } catch (SirhaException e) {
            throw e;  
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener profesor: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Schedule> getSchedules(String groupId) throws SirhaException {
        try {
            if (groupId == null) {
                log.error(ErrorCodeSirha.INVALID_ARGUMENT.getDefaultMessage());
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND);
            }
            
            log.info("Obteniendo horarios del grupo con ID: {}", groupId);
                    
            List<Schedule> schedules = groupService.getSchedules(groupId);
            
            log.info("Horarios obtenidos del grupo {} - Total: {}", groupId, schedules.size());
            
            return schedules;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener horarios: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public boolean isFull(String groupId) throws SirhaException {
        try {
            if (groupId == null) {
                log.error("Error: ID de grupo no puede ser null");
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND,"El ID del grupo no puede ser null");
            }
            
            log.debug("Verificando si el grupo con ID: {} está lleno", groupId);
                    
            boolean isFull = groupService.isFull(groupId);
            
            log.debug("Grupo {} está lleno: {}", groupId, isFull);
            return isFull;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al verificar capacidad: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public int getAvailableSeats(String groupId) throws SirhaException {
        try {
            if (groupId == null) {
                log.error("Error: ID de grupo no puede ser null");
                throw SirhaException.of(ErrorCodeSirha.GROUP_NOT_FOUND,"El ID del grupo no puede ser null");
            }
            
            log.debug("Obteniendo asientos disponibles del grupo con ID: {}", groupId);
                        
            int availableSeats = groupService.getAvailableSeats(groupId);
            
            log.debug("Asientos disponibles en grupo {}: {}", groupId, availableSeats);
            return availableSeats;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al obtener asientos disponibles: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public List<Group> deleteGroupsBySubjectName(String subjectName) throws SirhaException {
        log.info("Eliminando todos los grupos asociados a la materia: {}", subjectName);
        try {
            if (subjectName == null || subjectName.isEmpty()) {
                log.error("Error: El nombre de la materia no puede ser null o vacío");
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND,"El nombre de la materia no puede ser null o vacío");
            }
            Subject subject = subjectRepository.findByName(subjectName).orElse(null);

            if (subject == null) {
                log.warn("No se encontró la materia con nombre: {}", subjectName);
                throw SirhaException.of(ErrorCodeSirha.SUBJECT_NOT_FOUND,"Materia no encontrada: " + subjectName);
            }

            List<Group> groupsToDelete = subject.getGroups();
            if (groupsToDelete.isEmpty()) {
                log.warn("No se encontraron grupos asociados a la materia: {}", subjectName);
                return groupsToDelete;
            }

            log.info("Total de grupos encontrados para eliminar: {}", groupsToDelete.size());
            for (Group group : groupsToDelete) {
                groupService.deleteGroupById(group.getId());
                log.info("Grupo con ID {} eliminado", group.getId());
            }
            subject.deleteGroups();
            log.info("Todos los grupos asociados a la materia '{}' han sido eliminados", subjectName);
            return groupsToDelete;
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al eliminar grupos por materia: " + e.getMessage(), e);
        }
    }
    @Transactional
    @Override
    public Group closeGroup(String groupId) throws SirhaException {
        try {
            log.info("Cerrando grupo con ID: {}", groupId);
            return groupService.closeGroup(groupId);
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al cerrar el grupo: " + e.getMessage(), e);
        }
    }
    @Transactional
    @Override
    public Group openGroup(String groupId) throws SirhaException {
        try {
            log.info("Abriendo grupo con ID: {}", groupId);
            return groupService.openGroup(groupId);
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al abrir el grupo: " + e.getMessage(), e);
        }
    }

    @Override
    public Group saveGroup(Subject subject, Group group) throws SirhaException {
        try {
            log.info("Guardando grupo (overload) para materia: {} - Grupo ID/capacidad: {}/{}", 
                     subject != null ? subject.getName() : "null",
                     group != null ? group.getId() : "null",
                     group != null ? group.getCapacity() : 0);
            return groupService.saveGroup(subject, group);
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al guardar grupo: " + e.getMessage(), e);
        }
    }

    @Override
    public Group findById(String id) throws SirhaException {
        try {
            log.info("Buscando grupo por ID (delegado): {}", id);
            return groupService.findById(id);
        } catch (SirhaException e) {
            throw e;
        } catch (Exception e) {
            throw SirhaException.of(ErrorCodeSirha.INTERNAL_ERROR,"Error interno al buscar grupo por ID: " + e.getMessage(), e);
        }
    }

}