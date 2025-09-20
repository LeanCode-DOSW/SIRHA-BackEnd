package edu.dosw.sirha.sirha_backend.controller;


import edu.dosw.sirha.sirha_backend.domain.AcademicStatus;
import edu.dosw.sirha.sirha_backend.dto.StudentScheduleDTO;
import edu.dosw.sirha.sirha_backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) { this.studentService = studentService; }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<StudentScheduleDTO> getSchedule(@PathVariable Long id) {
        StudentScheduleDTO dto = studentService.getSchedule(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/semaforo")
    public ResponseEntity<AcademicStatus> getAcademicStatus(@PathVariable Long id) {
        AcademicStatus status = studentService.getAcademicStatus(id);
        return ResponseEntity.ok(status);
    }
}
