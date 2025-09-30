package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;
import edu.dosw.sirha.SIRHA_BackEnd.domain.port.RequestProcess;

public class ResponseProcess implements RequestProcess {
    @Id
    //@GeneratedValue
    private int id;
    
    private RequestStateEnum estado;
    private LocalDateTime creadoEn;
    private String comentario;

    public ResponseProcess() {
        this.creadoEn = LocalDateTime.now();
    }
    public ResponseProcess(RequestStateEnum estado, String comentario) {
        this.creadoEn = LocalDateTime.now();
        setEstado(estado, comentario);
    }

    public String getComentario() {return comentario;}
    public void setComentario(String comentario) {this.comentario = comentario;}
    public int getId() {return id;}
    public RequestStateEnum getEstado() {return estado;}
    public LocalDateTime getCreadoEn() {return creadoEn;}
    public void setEstado(RequestStateEnum estado) {this.estado = estado;}


    public void setEstado(RequestStateEnum estado, String comentario) {
        this.estado = estado;
        this.comentario = comentario;
    }

}
