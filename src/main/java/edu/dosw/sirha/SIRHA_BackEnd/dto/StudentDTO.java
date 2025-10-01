package edu.dosw.sirha.SIRHA_BackEnd.dto;

import java.util.List;

public class StudentDTO {
    private int id;
    private String username;
    private String email;
    private String codigo;
    private List<Integer> solicitudesIds;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public List<Integer> getSolicitudesIds() {
        return solicitudesIds;
    }
    public void setSolicitudesIds(List<Integer> solicitudesIds) {
        this.solicitudesIds = solicitudesIds;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
