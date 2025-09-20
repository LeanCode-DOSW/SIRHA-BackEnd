package edu.dosw.sirha.sirha_backend.domain;


public class Group {
    private Long id;
    private String subject;
    private String professor;
    private int capacity;
    private String schedule; // ejemplo: "Lun 8-10"
    private int enrolled;

    public Group() {}

    public Group(Long id, String subject, String professor, int capacity, String schedule, int enrolled) {
        this.id = id;
        this.subject = subject;
        this.professor = professor;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolled = enrolled;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public int getEnrolled() { return enrolled; }
    public void setEnrolled(int enrolled) { this.enrolled = enrolled; }
}
