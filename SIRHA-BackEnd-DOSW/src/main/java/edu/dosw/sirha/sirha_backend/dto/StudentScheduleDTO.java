package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;

public class StudentScheduleDTO {
    private Long studentId;
    private List<String> subjects;

    public StudentScheduleDTO() {}

    public StudentScheduleDTO(Long studentId, List<String> subjects) {
        this.studentId = studentId;
        this.subjects = subjects;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }
}
