package edu.dosw.sirha.sirha_backend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.sirha.sirha_backend.domain.model.*;
import edu.dosw.sirha.sirha_backend.domain.model.stateGroup.Group;
import edu.dosw.sirha.sirha_backend.repository.mongo.GroupMongoRepository;
import edu.dosw.sirha.sirha_backend.service.GroupService;

import java.util.List;

/**
 * Implementación de {@link GroupService} enfocada en la gestión de materias y grupos.
 */
@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupMongoRepository groupRepository;

    public GroupServiceImpl(GroupMongoRepository groupRepository) {
        this.groupRepository = groupRepository;
        log.info("GroupServiceImpl inicializado correctamente");
    }


    /**
     * Registra un grupo en una materia
     */
    @Transactional
    @Override
    public Group saveGroup(Subject subject, Group group) {
        
        try{
            if (subject == null) {
                log.error("Error: La materia no puede ser null");
                throw new IllegalArgumentException("La materia no puede ser null");
            }
            
            if (group == null) {
                log.error("Error: El grupo no puede ser null");
                throw new IllegalArgumentException("El grupo no puede ser null");
            }
            log.info("Guardando grupo {} para la materia: {}", group.getId(), subject.getName());
            Group savedGroup = groupRepository.save(group);
            subject.addGroup(savedGroup);
            log.info("Grupo guardado exitosamente con ID: {}", savedGroup.getId());
            return savedGroup;
        } catch (Exception e) {
            log.error("Error al guardar el grupo {} para la materia {}: {}", group.getId(), subject.getName(), e.getMessage());
            throw new RuntimeException("Error interno al guardar grupo", e);
        }
    }

    @Transactional
    @Override
    public Group deleteGroupById(String id) { 
        log.info("Eliminando grupo con ID: {}", id);

        try {
            if (id == null) {
                log.error("Error: ID del grupo no puede ser null");
                throw new IllegalArgumentException("El ID del grupo no puede ser null");    
            }

            if (!groupRepository.existsById(id)) {
                log.warn("Grupo con ID {} no existe", id);
                throw new RuntimeException("Grupo con id " + id + " no existe");
            }

            Group group = groupRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Grupo con id " + id + " no encontrado"));
            log.info("Grupo encontrado - Código: {}, Materia: {}, Inscritos: {}", 
                    group.getCode(), 
                    group.getCurso() != null ? group.getCurso().getName() : "null",
                    group.getInscritos());

            Subject subject = group.getCurso();
            subject.removeGroup(group);
            log.info("Grupo removido de la materia: {}", subject.getName());
            groupRepository.deleteById(id);
            log.info("Grupo con ID {} eliminado exitosamente", id);
            return group;
        } catch (Exception e) {
            log.error("Error al eliminar el grupo con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al eliminar grupo", e);
        }
        
    }
    @Transactional
    @Override
    public Group assignProfessor(String groupId, Professor professor) {
        
        try{
            if (professor == null) {
                log.error("Error: El profesor no puede ser null");
                throw new IllegalArgumentException("El profesor no puede ser null");
            }
            log.info("Asignando profesor {} al grupo con ID: {}", professor.getUsername(), groupId);
            Group group = findById(groupId);
            group.setProfesor(professor);
            Group updatedGroup = groupRepository.save(group);
            log.info("Profesor {} asignado exitosamente al grupo con ID: {}", professor.getUsername(), groupId);

            return updatedGroup;
        }catch (Exception e) {
            log.error("Error al asignar el profesor {} al grupo con ID {}: {}", professor.getUsername(), groupId, e.getMessage());
            throw new RuntimeException("Error interno al asignar profesor", e);
        }
    }
    @Transactional
    @Override
    public Group addSchedule(String groupId, Schedule schedule) {
        log.info("Agregando horario al grupo con ID: {}", groupId);

        try {
            if (schedule == null) {
                log.error("Error: El horario no puede ser null");
                throw new IllegalArgumentException("El horario no puede ser null");
            }
            Group group = findById(groupId);
            group.addSchedule(schedule);
            log.info("Horario agregado exitosamente al grupo con ID: {}", groupId);
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} actualizado con nuevo horario", groupId);
            return updatedGroup;
        } catch (Exception e) {
            log.error("Error al agregar horario al grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al agregar horario", e);
        }
    }
    @Transactional
    @Override
    public Group closeGroup(String groupId) {
        log.info("Cerrando grupo con ID: {}", groupId);
        try{
            Group group = findById(groupId);
            log.info("Estado actual del grupo (antes de cerrar): {}", group.getGroupState().getClass().getSimpleName());
            group.closeGroup();
            log.info("Estado actual del grupo (después de cerrar): {}", group.getGroupState().getClass().getSimpleName());
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} cerrado exitosamente", groupId);
            return updatedGroup;
        } catch (Exception e) {
            log.error("Error al cerrar el grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al cerrar grupo", e);
        }
    }
    @Transactional
    @Override
    public Group openGroup(String groupId) {
        log.info("Abriendo grupo con ID: {}", groupId);
        try{
            Group group = findById(groupId);
            log.info("Estado actual del grupo (antes de abrir): {}", group.getGroupState().getClass().getSimpleName());
            group.openGroup();
            log.info("Estado actual del grupo (después de abrir): {}", group.getGroupState().getClass().getSimpleName());
            Group updatedGroup = groupRepository.save(group);
            log.info("Grupo con ID {} abierto exitosamente", groupId);
            return updatedGroup;
        } catch (Exception e) {
            log.error("Error al abrir el grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al abrir grupo", e);
        }
    }
    @Transactional
    @Override
    public List<Schedule> getSchedules(String groupId) {
        log.info("Obteniendo horarios del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            List<Schedule> schedules = group.getSchedules();
            log.info("Horarios obtenidos exitosamente para el grupo con ID: {}", groupId);
            return schedules;
        } catch (Exception e) {
            log.error("Error al obtener horarios del grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al obtener horarios", e);
        }
    }
    @Transactional
    @Override
    public boolean isFull(String groupId) {
        log.info("Verificando si el grupo con ID {} está lleno", groupId);
        try {
            Group group = findById(groupId);
            boolean isFull = group.isFull();
            log.info("El grupo con ID {} está {}", groupId, isFull ? "lleno" : "disponible");
            return isFull;
        } catch (Exception e) {
            log.error("Error al verificar si el grupo con ID {} está lleno: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al verificar estado de grupo", e);
        }
    }
    @Transactional
    @Override
    public int getAvailableSeats(String groupId) {
        log.info("Obteniendo asientos disponibles del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            int availableSeats = group.getCuposDisponibles();
            log.info("El grupo con ID {} tiene {} asientos disponibles", groupId, availableSeats);
            return availableSeats;
        } catch (Exception e) {
            log.error("Error al obtener los asientos disponibles del grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al obtener asientos disponibles", e);
        }
    }

    @Transactional
    @Override
    public Group findById(String id) {
        log.info("Buscando grupo con ID: {}", id);
        try {
            Group group = groupRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Grupo con ID {} no encontrado", id);
                        return new RuntimeException("Grupo con id " + id + " no encontrado");
                    });
            log.info("Grupo encontrado: {}", group);
            return group;
        } catch (Exception e) {
            log.error("Error al buscar grupo con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al buscar grupo", e);
        }
    }

    @Transactional
    @Override
    public Professor getProfessor(String groupId) {
        log.info("Obteniendo profesor del grupo con ID: {}", groupId);
        try {
            Group group = findById(groupId);
            Professor professor = group.getProfesor();
            if (professor == null) {
                log.warn("El grupo con ID {} no tiene un profesor asignado", groupId);
                throw new RuntimeException("El grupo con id " + groupId + " no tiene un profesor asignado");
            }
            log.info("Profesor del grupo con ID {}: {}", groupId, professor.getUsername());
            return professor;
        } catch (Exception e) {
            log.error("Error al obtener el profesor del grupo con ID {}: {}", groupId, e.getMessage());
            throw new RuntimeException("Error interno al obtener profesor", e);
        }
    }
    @Transactional
    @Override
    public List<Group> findAllGroups() {
        log.info("Obteniendo todos los grupos");
        try {
            List<Group> groups = groupRepository.findAll();
            log.info("Total de grupos encontrados: {}", groups.size());
            return groups;
        } catch (Exception e) {
            log.error("Error al obtener todos los grupos: {}", e.getMessage());
            throw new RuntimeException("Error interno al obtener grupos", e);
        }
    }

}