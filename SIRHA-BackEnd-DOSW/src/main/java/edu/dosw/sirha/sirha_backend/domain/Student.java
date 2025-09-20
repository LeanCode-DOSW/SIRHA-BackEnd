package edu.dosw.sirha.sirha_backend.domain;

import java.util.List;
import java.util.Objects;

public class Student {
    private Long id;
    private String name;
    private String career;
    private int semester;
    private List<String> subjects;

    public Student() {}

    public Student(Long id, String name, String career, int semester, List<String> subjects) {
        this.id = id;
        this.name = name;
        this.career = career;
        this.semester = semester;
        this.subjects = subjects;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
