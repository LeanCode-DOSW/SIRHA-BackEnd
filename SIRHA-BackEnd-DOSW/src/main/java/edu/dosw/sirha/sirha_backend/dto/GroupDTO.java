package edu.dosw.sirha.sirha_backend.dto;

public class GroupDTO {
    private Long id;
    private String subject;
    private String professor;
    private int availableSeats;

    public GroupDTO() {}

    public GroupDTO(Long id, String subject, String professor, int availableSeats) {
        this.id = id;
        this.subject = subject;
        this.professor = professor;
        this.availableSeats = availableSeats;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
