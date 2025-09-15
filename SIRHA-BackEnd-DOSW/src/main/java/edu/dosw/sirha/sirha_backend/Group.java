package edu.dosw.sirha.sirha_backend;

public class Group {
    private String id;
    private String subject;
    private String professor;
    private int capacity;
    private String schedule;

    public String getId(){
        return id;
    }
    public String getSubject(){
        return subject;
    }
    public String getProfessor(){
        return professor;
    }
    public  String getSchedule(){
        return schedule;
    }
    public int getCapacity(){
        return capacity;
    }
}
