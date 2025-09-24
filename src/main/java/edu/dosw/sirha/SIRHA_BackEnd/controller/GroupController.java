package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Schedule;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Professor;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // ==================== CRUD BÁSICO ====================

    @GetMapping
    public List<Group> getAll() {
        return groupService.findAll();
    }

    @GetMapping("/{groupId}")
    public Group getById(@PathVariable String groupId) {
        return groupService.findById(groupId);
    }

    @PostMapping
    public Group create(@RequestBody Group group) {
        return groupService.save(group);
    }

    @DeleteMapping("/{groupId}")
    public void delete(@PathVariable String groupId) {
        groupService.delete(groupId);
    }

    // ==================== FUNCIONES DE NEGOCIO ====================

    // Consultar cupos disponibles
    @GetMapping("/{groupId}/cupos")
    public int getCuposDisponibles(@PathVariable String groupId) {
        return groupService.getCuposDisponibles(groupId);
    }

    // Asignar profesor
    @PostMapping("/{groupId}/profesor")
    public void asignarProfesor(@PathVariable String groupId, @RequestBody Professor professor) {
        groupService.asignarProfesor(groupId, professor);
    }

    // Consultar profesor asignado
    @GetMapping("/{groupId}/profesor")
    public Professor getProfesor(@PathVariable String groupId) {
        return groupService.getProfesor(groupId);
    }

    // Agregar horario al grupo
    @PostMapping("/{groupId}/horarios")
    public void agregarHorario(@PathVariable String groupId, @RequestBody Schedule schedule) {
        groupService.agregarHorario(groupId, schedule);
    }

    // Consultar horarios de un grupo
    @GetMapping("/{groupId}/horarios")
    public List<Schedule> getHorarios(@PathVariable String groupId) {
        return groupService.getHorarios(groupId);
    }
}