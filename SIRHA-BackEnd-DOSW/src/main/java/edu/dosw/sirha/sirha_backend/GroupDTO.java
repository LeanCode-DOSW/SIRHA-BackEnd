package edu.dosw.sirha.sirha_backend;

public class GroupDTO {
    private String id;
    private String subject;
    private String professor;
    private int availableSeats;

    public String getId(){
        return id;
    }
    public String getSubject(){
        return subject;
    }
    public String getProfessor(){
        return professor;
    }
    public int getAvailableSeats(){
        return availableSeats;
    }
}
