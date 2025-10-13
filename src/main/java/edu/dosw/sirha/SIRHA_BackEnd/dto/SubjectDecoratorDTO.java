package edu.dosw.sirha.sirha_backend.dto;

import edu.dosw.sirha.sirha_backend.domain.model.enums.SemaforoColores;

public class SubjectDecoratorDTO {
    
    private String id;
    private String name;    
    private int credits;
    private int semester;
    private SemaforoColores semaforoColor;
    private int grade; 

    public SubjectDecoratorDTO() {}
    
    public SubjectDecoratorDTO(String id, String name, int credits, 
                              int semester, SemaforoColores semaforoColor, 
                              int grade) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.semaforoColor = semaforoColor;
        this.grade = grade;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public SemaforoColores getSemaforoColor() {
        return semaforoColor;
    }
    
    public void setSemaforoColor(SemaforoColores semaforoColor) {
        this.semaforoColor = semaforoColor;
    }

    public int getGrade() {
        return grade;
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
    }

    
    @Override
    public String toString() {
        return "SubjectDecoratorDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", semester=" + semester +
                ", semaforoColor=" + semaforoColor +
                ", grade=" + grade +
                '}';
    }
}