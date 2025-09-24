package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;

public interface GroupService {

    // ===== CRUD de grupos =====
    List<Group> findAll();
    Group findById(String id);
    Group save(Group group);
    void delete(String id);

    // Gestión de profesores
    Group asignarProfesor(String groupId, Professor professor);
    Professor getProfesor(String groupId);

    // Gestión de horarios
    Group agregarHorario(String groupId, Schedule schedule);
    List<Schedule> getHorarios(String groupId);

    // Capacidad
    boolean estaLleno(String groupId);
    int getCuposDisponibles(String groupId);

    // Estado del grupo
    Group cerrarGrupo(String groupId);
    Group abrirGrupo(String groupId);
}