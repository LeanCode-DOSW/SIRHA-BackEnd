package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.time.LocalDateTime;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;

public interface RequestProcess {
    int getId();
    RequestStateEnum getEstado();
    LocalDateTime getCreadoEn();
    void setEstado(RequestStateEnum estado, String comentario);

}