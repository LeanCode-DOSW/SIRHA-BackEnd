package edu.dosw.sirha.SIRHA_BackEnd.service;

import java.util.List;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;

public interface GroupService {

    // ===== CRUD de grupos =====
    List<Group> findAll();
    Group findById(Integer id);
    Group save(Group group);
    void delete(Integer id);

    // Gestión de profesores
    Group asignarProfesor(Integer groupId, Professor professor);
    Professor getProfesor(Integer groupId);

    // Gestión de horarios
    Group agregarHorario(Integer groupId, Schedule schedule);
    List<Schedule> getHorarios(Integer groupId);

    // Capacidad
    boolean estaLleno(Integer groupId);
    int getCuposDisponibles(Integer groupId);

    // Estado del grupo
    Group cerrarGrupo(Integer groupId);
    Group abrirGrupo(Integer groupId);
}