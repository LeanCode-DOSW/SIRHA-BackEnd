package edu.dosw.sirha.sirha_backend;

import java.util.List;

public class StudentScheduleDTO {
    private String studentsId;
    private List<String> subjects;

    public String getStudentsId(){
        return studentsId;
    }
    public List<String> getSubjects(){
        return subjects;
    }
}
