package edu.dosw.sirha.SIRHA_BackEnd.controller;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
import edu.dosw.sirha.SIRHA_BackEnd.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de grupos académicos en el sistema SIRHA.
 *
 * Proporciona endpoints para:
 * - Consultar todos los grupos registrados
 * - Crear nuevos grupos
 * - Inscribir estudiantes en grupos específicos
 *
 * Todos los endpoints están mapeados bajo la ruta base "/api/groups".
 *
 * @see Group
 * @see Student
 * @see GroupService
 */
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Obtiene la lista de todos los grupos registrados en el sistema.
     *
     * @return lista de objetos {@link Group}.
     */
    @GetMapping
    public List<Group> getAll() {
        return groupService.findAll();
    }

    /**
     * Crea un nuevo grupo académico.
     *
     * @param group objeto {@link Group} con la información del grupo a registrar.
     * @return el grupo creado.
     */
    @PostMapping
    public Group create(@RequestBody Group group) {
        return groupService.save(group);
    }

    /**
     * Inscribe un estudiante en un grupo académico específico.
     *
     * @param groupId identificador del grupo en el cual se inscribirá al estudiante.
     * @param student objeto {@link Student} con la información del estudiante.
     */
    @PostMapping("/{groupId}/enroll")
    public void enrollStudent(@PathVariable String groupId, @RequestBody Student student) {
        groupService.enrollStudent(groupId, student);
    }
}
