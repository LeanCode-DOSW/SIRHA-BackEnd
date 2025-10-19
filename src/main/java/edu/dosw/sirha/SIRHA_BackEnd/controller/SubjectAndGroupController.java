package edu.dosw.sirha.sirha_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dosw.sirha.sirha_backend.util.GroupMapper;
import edu.dosw.sirha.sirha_backend.util.SubjectMapper;
import edu.dosw.sirha.sirha_backend.domain.model.Professor;
import edu.dosw.sirha.sirha_backend.domain.model.Schedule;
import edu.dosw.sirha.sirha_backend.domain.model.Subject;
import edu.dosw.sirha.sirha_backend.domain.model.stategroup.Group;
import edu.dosw.sirha.sirha_backend.dto.GroupDTO;
import edu.dosw.sirha.sirha_backend.dto.SubjectDTO;
import edu.dosw.sirha.sirha_backend.exception.SirhaException;
import edu.dosw.sirha.sirha_backend.service.SubjectService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectAndGroupController {

    private final SubjectService subjectService;

    public SubjectAndGroupController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // ============ SUBJECT ENDPOINTS ============
    
    @GetMapping
    public ResponseEntity<List<Subject>> findAllSubjects() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping("/{name}")
    public ResponseEntity<Subject> findSubjectByName(@PathVariable String name) throws SirhaException{
        Subject subject = subjectService.findByName(name);
        return ResponseEntity.ok(subject);
    }
    
    @PostMapping
    public ResponseEntity<Subject> saveSubject(@RequestBody SubjectDTO subjectDTO) throws SirhaException{
        Subject subject = SubjectMapper.toEntity(subjectDTO);
        Subject savedSubject = subjectService.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubject);
    }
    
    @DeleteMapping("/{name}")
    public ResponseEntity<Subject> deleteSubjectByName(@PathVariable String name) throws SirhaException{
        Subject deletedSubject = subjectService.deleteByName(name);
        return ResponseEntity.ok(deletedSubject);
    }
    
    @GetMapping("/{name}/exists")
    public ResponseEntity<Boolean> existsSubjectByName(@PathVariable String name) throws SirhaException{
        boolean exists = subjectService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    
    @GetMapping("/groups")
    public ResponseEntity<List<Group>> findAllGroups() throws SirhaException{
        List<Group> groups = subjectService.findAllGroups();
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{subjectName}/groups")
    public ResponseEntity<List<Group>> getGroupsOfSubject(@PathVariable String subjectName) throws SirhaException{
        List<Group> groups = subjectService.getGroupsOfSubject(subjectName);
        return ResponseEntity.ok(groups);
    }
    
    @GetMapping("/{subjectName}/groups/open")
    public ResponseEntity<List<Group>> getOpenGroupsOfSubject(@PathVariable String subjectName) throws SirhaException{
        List<Group> openGroups = subjectService.getOpenGroupsOfSubject(subjectName);
        return ResponseEntity.ok(openGroups);
    }
    
    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable String id) throws SirhaException{
        Group group = subjectService.getGroupById(id);
        return ResponseEntity.ok(group);
    }
    
    @PostMapping("/{subjectName}/groups")
    public ResponseEntity<Group> saveGroup(@PathVariable String subjectName, @RequestBody GroupDTO groupDTO) {
        try {
            Group group = GroupMapper.toEntity(groupDTO);
            Group savedGroup = subjectService.saveGroup(subjectName, group);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Group> deleteGroupById(@PathVariable String id) throws SirhaException{
        Group deletedGroup = subjectService.deleteGroupById(id);
        return ResponseEntity.ok(deletedGroup);
    }
    
    @GetMapping("/groups/{id}/exists")
    public ResponseEntity<Boolean> existsGroupById(@PathVariable String id) throws SirhaException{
        boolean exists = subjectService.existsGroupById(id);
        return ResponseEntity.ok(exists);
    }
    
    @DeleteMapping("/{subjectName}/groups")
    public ResponseEntity<List<Group>> deleteGroupsBySubjectName(@PathVariable String subjectName) throws SirhaException{
        List<Group> deletedGroups = subjectService.deleteGroupsBySubjectName(subjectName);
        return ResponseEntity.ok(deletedGroups);
    }
    
    @PutMapping("/groups/{groupId}/professor")
    public ResponseEntity<Group> assignProfessor(@PathVariable String groupId, @RequestBody Professor professor) throws SirhaException{
        Group updatedGroup = subjectService.assignProfessor(groupId, professor);
        return ResponseEntity.ok(updatedGroup);
    }
    
    @PostMapping("/groups/{groupId}/schedules")
    public ResponseEntity<Group> addSchedule(@PathVariable String groupId, @RequestBody Schedule schedule) throws SirhaException{
        Group updatedGroup = subjectService.addSchedule(groupId, schedule);
        return ResponseEntity.ok(updatedGroup);
    }
    
    @GetMapping("/groups/{groupId}/professor")
    public ResponseEntity<Professor> getProfessor(@PathVariable String groupId) throws SirhaException{
        Professor professor = subjectService.getProfessor(groupId);
        return ResponseEntity.ok(professor);
    }
    
    @GetMapping("/groups/{groupId}/schedules")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable String groupId) throws SirhaException{
        List<Schedule> schedules = subjectService.getSchedules(groupId);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/groups/{groupId}/full")
    public ResponseEntity<Boolean> isGroupFull(@PathVariable String groupId) throws SirhaException{
        boolean isFull = subjectService.isFull(groupId);
        return ResponseEntity.ok(isFull);
    }
    
    @GetMapping("/groups/{groupId}/available-seats")
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable String groupId) throws SirhaException{
        int availableSeats = subjectService.getAvailableSeats(groupId);
        return ResponseEntity.ok(availableSeats);
    }
}
