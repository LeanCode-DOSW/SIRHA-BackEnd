package edu.dosw.sirha.sirha_backend.domain.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.sirha_backend.domain.port.RequestProcess;
import edu.dosw.sirha.sirha_backend.dto.ResponseRequest;

public class ResponseProcess implements RequestProcess {
    @Id
    private String id;
    private RequestStateEnum estado;
    private LocalDateTime creadoEn;
    private String comentario;

    public ResponseProcess() {
        this.creadoEn = LocalDateTime.now();
    }
    public ResponseProcess(ResponseRequest response) {
        this.creadoEn = LocalDateTime.now();
        setStatus(response.getState(), response.getResponseComment());
        creadoEn = response.getResponseDate();
    }

    public ResponseProcess(RequestStateEnum estado, String comentario) {
        this.creadoEn = LocalDateTime.now();
        setStatus(estado, comentario);
    }

    public String getComment() {return comentario;}
    public void setComment(String comentario) {this.comentario = comentario;}
    public String getId() {return id;}
    public RequestStateEnum getStatus() {return estado;}
    public LocalDateTime getCreatedAt() {return creadoEn;}
    public void setStatus(RequestStateEnum estado) {this.estado = estado;}


    public void setStatus(RequestStateEnum estado, String comentario) {
        this.estado = estado;
        this.comentario = comentario;
    }

}
