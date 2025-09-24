package edu.dosw.sirha.SIRHA_BackEnd.controller;


import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Group;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.Student;
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

    @GetMapping
    public List<Group> getAll() {
        return groupService.findAll();
    }

    @PostMapping
    public Group create(@RequestBody Group group) {
        return groupService.save(group);
    }

    @PostMapping("/{groupId}/inscribir")
    public void inscribir(@PathVariable String groupId, @RequestBody Student student) {
        groupService.inscribirEstudiante(groupId, student);
    }
}