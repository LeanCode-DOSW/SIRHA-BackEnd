package edu.dosw.sirha.sirha_backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.repository.mongo.SubjectMongoRepository;
import edu.dosw.sirha.sirha_backend.service.GroupService;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectMongoRepository subjectRepository;
    private final GroupService groupService;

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
    public Subject findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Error: Nombre de materia no puede ser null o vacío");
            throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
        }
        
        log.info("Buscando materia por nombre: {}", name);
        
        try {
            Subject subject = subjectRepository.findByName(name).orElse(null);
            
            if (subject == null) {
                log.warn("Materia no encontrada con nombre: {}", name);
                throw new RuntimeException("Materia no encontrada: " + name);
            }

            log.info("Materia encontrada - Nombre: {}, Créditos: {}", 
                        subject.getName(), subject.getCredits());
            return subject;
            
        } catch (Exception e) {
            log.error("Error inesperado al buscar materia por nombre '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Error interno al buscar materia", e);
        }
    }

    @Transactional
    @Override
    public Subject save(Subject subject) {
        if (subject == null) {
            log.error("Error: La materia no puede ser null");
            throw new IllegalArgumentException("La materia no puede ser null");
        }

        log.info("Guardando materia: {} (Créditos: {})", 
                subject.getName(), subject.getCredits());

        try {
            boolean exists = subjectRepository.existsByName(subject.getName());
            if (exists) {
                log.warn("Materia con nombre '{}' ya existe", subject.getName());
                throw new RuntimeException("Materia con nombre '" + subject.getName() + "' ya existe");
            }
            
            Subject savedSubject = subjectRepository.save(subject);
            
            log.info("Materia guardada exitosamente - ID: {}, Nombre: {}", 
                    savedSubject.getId(), savedSubject.getName());
            
            return savedSubject;
            
        } catch (Exception e) {
            log.error("Error al guardar materia '{}': {}", subject.getName(), e.getMessage(), e);
            throw new RuntimeException("Error interno al guardar materia", e);
        }
    }

    @Transactional
    @Override
    public Subject deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Error: Nombre de materia no puede ser null o vacío");
            throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
        }
        
        log.info("Eliminando materia por nombre: {}", name);
        
        try {
            Subject subject = findByName(name);
            
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
            
        } catch (Exception e) {
            log.error("Error inesperado al eliminar materia '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Error interno al eliminar materia", e);
        }
    }
    @Transactional
    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.debug("Verificación de existencia con nombre null/vacío, retornando false");
            return false;
        }
        
        log.debug("Verificando existencia de materia por nombre: {}", name);
        
        try {
            boolean exists = subjectRepository.existsByName(name);
            
            log.debug("Materia '{}' existe: {}", name, exists);
            return exists;
            
        } catch (Exception e) {
            log.error("Error al verificar existencia de materia '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Error interno al verificar existencia de materia", e);
        }
    }
    @Transactional
    @Override
    public List<Group> getGroupsOfSubject(String subjectName) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            log.error("Error: Nombre de materia no puede ser null o vacío");
            throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
        }
        
        log.info("Obteniendo grupos de la materia: {}", subjectName);
        
        try {
            Subject subject = findByName(subjectName);
            
            if (subject == null) {
                log.error("No se encontró la materia con nombre: {}", subjectName);
                throw new RuntimeException("Materia no encontrada: " + subjectName);
            }
            
            List<Group> groups = subject.getGroups();
            
            log.info("Grupos obtenidos de materia '{}' - Total: {}", subjectName, groups.size());
            
            
            return groups;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al obtener grupos de materia '{}': {}", 
                    subjectName, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener grupos de materia", e);
        }
    }
    @Transactional
    @Override
    public List<Group> getOpenGroupsOfSubject(String subjectName) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            log.error("Error: Nombre de materia no puede ser null o vacío");
            throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
        }
        
        log.info("Obteniendo grupos abiertos de la materia: {}", subjectName);
        
        try {
            Subject subject = findByName(subjectName);
            
            if (subject == null) {
                log.error("No se encontró la materia con nombre: {}", subjectName);
                throw new RuntimeException("Materia no encontrada: " + subjectName);
            }
            
            List<Group> openGroups = subject.getOpenGroups();
            
            log.info("Grupos abiertos obtenidos de materia '{}' - Total: {}", subjectName, openGroups.size());
            
            return openGroups;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al obtener grupos abiertos de materia '{}': {}", 
                    subjectName, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener grupos abiertos", e);
        }
    }
    @Transactional
    @Override
    public Group getGroupById(String id) {
        if (id == null || id.trim().isEmpty()) {
            log.error("Error: ID de grupo no puede ser null o vacío");
            throw new IllegalArgumentException("El ID del grupo no puede ser null o vacío");
        }
        
        log.info("Obteniendo grupo por ID: {}", id);
        
        try {

            Group group = groupService.findById(id);
            
            if (group == null) {
                log.warn("Grupo no encontrado con ID: {}", id);
                throw new RuntimeException("Grupo no encontrado con ID: " + id);
            }
            log.info("Grupo encontrado - ID: {}, Código: {}, Materia: {}", 
                        id, group.getCode(), 
                        group.getCurso() != null ? group.getCurso().getName() : "null");
            return group;
            
        } catch (NumberFormatException e) {
            log.error("ID de grupo inválido (no es numérico): {}", id);
            throw new IllegalArgumentException("ID de grupo debe ser numérico");
        } catch (Exception e) {
            log.error("Error inesperado al obtener grupo por ID '{}': {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener grupo", e);
        }
    }

    @Transactional
    @Override
    public Group saveGroup(String subjectName, Group group) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            log.error("Error: Nombre de materia no puede ser null o vacío");
            throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
        }
        
        if (group == null) {
            log.error("Error: El grupo no puede ser null");
            throw new IllegalArgumentException("El grupo no puede ser null");
        }
        
        log.info("Guardando grupo para la materia: {} - Capacidad: {}", 
                subjectName, group.getCapacidad());
        
        try {
            Subject subject = findByName(subjectName);
            
            if (subject == null) {
                log.error("No se encontró la materia con nombre: {}", subjectName);
                throw new RuntimeException("Materia no encontrada: " + subjectName);
            }
            
            Group savedGroup = groupService.saveGroup(subject, group);
            
            log.info("Grupo guardado exitosamente para materia '{}' - ID: {}, Código: {}", 
                    subjectName, savedGroup.getId(), savedGroup.getCode());
            
            return savedGroup;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al guardar grupo para materia '{}': {}", 
                    subjectName, e.getMessage(), e);
            throw new RuntimeException("Error interno al guardar grupo", e);
        }
    }

    @Transactional
    @Override
    public Group deleteGroupById(String id) {
        if (id == null || id.trim().isEmpty()) {
            log.error("Error: ID de grupo no puede ser null o vacío");
            throw new IllegalArgumentException("El ID del grupo no puede ser null o vacío");
        }
        
        log.info("Eliminando grupo por ID: {}", id);
        
        try {
            Group deletedGroup = groupService.deleteGroupById(id);
            
            log.info("Grupo eliminado exitosamente - ID: {}, Código: {}", 
                    id, deletedGroup != null ? deletedGroup.getCode() : "null");
            
            return deletedGroup;
            
        } catch (NumberFormatException e) {
            log.error("ID de grupo inválido (no es numérico): {}", id);
            throw new IllegalArgumentException("ID de grupo debe ser numérico");
        } catch (Exception e) {
            log.error("Error inesperado al eliminar grupo por ID '{}': {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al eliminar grupo", e);
        }
    }
    @Transactional
    @Override
    public boolean existsGroupById(String id) {
        if (id == null || id.trim().isEmpty()) {
            log.debug("Verificación de existencia de grupo con ID null/vacío, retornando false");
            return false;
        }
        
        log.debug("Verificando existencia de grupo por ID: {}", id);
        
        try {
            Group group = groupService.findById(id);
            boolean exists = group != null;
            
            log.debug("Grupo con ID '{}' existe: {}", id, exists);
            return exists;
            
        } catch (NumberFormatException e) {
            log.debug("ID de grupo inválido (no es numérico): {}, retornando false", id);
            return false;
        } catch (Exception e) {
            log.warn("Error al verificar existencia de grupo '{}': {}, retornando false", 
                    id, e.getMessage());
            return false;
        }
    }
    @Transactional
    @Override
    public List<Group> findAllGroups() {
        log.info("Consultando todos los grupos");
        
        try {
            List<Group> groups = groupService.findAllGroups();
            
            log.info("Todos los grupos consultados exitosamente - Total: {}", groups.size());
            
            return groups;
            
        } catch (Exception e) {
            log.error("Error inesperado al consultar todos los grupos: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar grupos", e);
        }
    }
    @Transactional
    @Override
    public Group assignProfessor(String groupId, Professor professor) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        if (professor == null) {
            log.error("Error: El profesor no puede ser null");
            throw new IllegalArgumentException("El profesor no puede ser null");
        }
        
        log.info("Asignando profesor {} al grupo con ID: {}", professor.getUsername(), groupId);
        
        try {
            Group updatedGroup = groupService.assignProfessor(groupId, professor);
            
            log.info("Profesor asignado exitosamente al grupo ID: {}", groupId);
            return updatedGroup;
            
        } catch (Exception e) {
            log.error("Error inesperado al asignar profesor al grupo {}: {}", groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al asignar profesor", e);
        }
    }
    @Transactional
    @Override
    public Group addSchedule(String groupId, Schedule schedule) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        if (schedule == null) {
            log.error("Error: El horario no puede ser null");
            throw new IllegalArgumentException("El horario no puede ser null");
        }
        
        log.info("Agregando horario al grupo con ID: {}", groupId);
        
        try {
            Group updatedGroup = groupService.addSchedule(groupId, schedule);
            
            log.info("Horario agregado exitosamente al grupo ID: {}", groupId);
            return updatedGroup;
            
        } catch (Exception e) {
            log.error("Error inesperado al agregar horario al grupo {}: {}", groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al agregar horario", e);
        }
    }
    @Transactional
    @Override
    public Professor getProfessor(String groupId) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        log.info("Obteniendo profesor del grupo con ID: {}", groupId);
        
        try {
            Professor professor = groupService.getProfessor(groupId);
            
            if (professor != null) {
                log.info("Profesor encontrado para grupo {} - Nombre: {}", 
                        groupId, professor.getUsername());
            } else {
                log.info("Grupo {} no tiene profesor asignado", groupId);
            }
            
            return professor;
            
        } catch (Exception e) {
            log.error("Error inesperado al obtener profesor del grupo {}: {}", groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener profesor", e);
        }
    }
    @Transactional
    @Override
    public List<Schedule> getSchedules(String groupId) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        log.info("Obteniendo horarios del grupo con ID: {}", groupId);
        
        try {
            List<Schedule> schedules = groupService.getSchedules(groupId);
            
            log.info("Horarios obtenidos del grupo {} - Total: {}", groupId, schedules.size());
            
            return schedules;
            
        } catch (Exception e) {
            log.error("Error inesperado al obtener horarios del grupo {}: {}", groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener horarios", e);
        }
    }
    @Transactional
    @Override
    public boolean isFull(String groupId) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        log.debug("Verificando si el grupo con ID: {} está lleno", groupId);
        
        try {
            boolean isFull = groupService.isFull(groupId);
            
            log.debug("Grupo {} está lleno: {}", groupId, isFull);
            return isFull;
            
        } catch (Exception e) {
            log.error("Error inesperado al verificar si grupo {} está lleno: {}", groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al verificar capacidad", e);
        }
    }
    @Transactional
    @Override
    public int getAvailableSeats(String groupId) {
        if (groupId == null) {
            log.error("Error: ID de grupo no puede ser null");
            throw new IllegalArgumentException("El ID del grupo no puede ser null");
        }
        
        log.debug("Obteniendo asientos disponibles del grupo con ID: {}", groupId);
        
        try {
            int availableSeats = groupService.getAvailableSeats(groupId);
            
            log.debug("Asientos disponibles en grupo {}: {}", groupId, availableSeats);
            return availableSeats;
            
        } catch (Exception e) {
            log.error("Error inesperado al obtener asientos disponibles del grupo {}: {}", 
                    groupId, e.getMessage(), e);
            throw new RuntimeException("Error interno al obtener asientos disponibles", e);
        }
    }
    @Transactional
    @Override
    public List<Group> deleteGroupsBySubjectName(String subjectName) {
        log.info("Eliminando todos los grupos asociados a la materia: {}", subjectName);
        try {
            if (subjectName == null || subjectName.isEmpty()) {
                log.error("Error: El nombre de la materia no puede ser null o vacío");
                throw new IllegalArgumentException("El nombre de la materia no puede ser null o vacío");
            }
            Subject subject = findByName(subjectName);
            if (subject == null) {
                log.warn("No se encontró la materia con nombre: {}", subjectName);
                throw new RuntimeException("Materia no encontrada: " + subjectName);
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
        } catch (Exception e) {
            log.error("Error al eliminar grupos asociados a la materia '{}': {}", subjectName, e.getMessage());
            throw new RuntimeException("Error interno al eliminar grupos por materia", e);
        }
    }
}
