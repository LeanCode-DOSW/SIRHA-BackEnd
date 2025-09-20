package edu.dosw.sirha.sirha_backend.controller;

import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.service.GroupService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) { this.groupService = groupService; }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getGroupsBySubject(@RequestParam(name = "materia") @NotBlank String subject) {
        List<GroupDTO> dtos = groupService.findGroupsBySubject(subject);
        return ResponseEntity.ok(dtos);
    }
}

