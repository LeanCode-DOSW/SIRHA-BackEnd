package edu.dosw.sirha.sirha_backend.dto;

import java.util.List;
import edu.dosw.sirha.sirha_backend.domain.model.enums.Careers;

public class StudentDTO {
    private String id;
    private String username;
    private String email;
    private String code;
    private List<String> requestIds;
    private Careers career;



    public StudentDTO(String id, String username, String email, String code, Careers career) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.code = code;
        this.career = career;
    }
    
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCode() {
        return code;
    }

    public List<String> getRequestIds() {
        return requestIds;
    }
    public void setSolicitudesIds(List<String> solicitudesIds) {
        this.requestIds = solicitudesIds;
    }

    public String getEmail() {
        return email;
    }

    public Careers getCareer() {
        return career;
    }

}
