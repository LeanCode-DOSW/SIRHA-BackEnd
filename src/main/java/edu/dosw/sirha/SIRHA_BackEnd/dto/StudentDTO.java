package edu.dosw.sirha.SIRHA_BackEnd.dto;

import java.util.List;

public class StudentDTO {
    private String id;
    private String username;
    private String email;
    private String codigo;
    private List<String> solicitudesIds;


    public String getId() {
        return id;
    }
    public void setId(String id) {
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
    public List<String> getSolicitudesIds() {
        return solicitudesIds;
    }
    public void setSolicitudesIds(List<String> solicitudesIds) {
        this.solicitudesIds = solicitudesIds;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
