package edu.dosw.sirha.SIRHA_BackEnd.domain.port;

import java.time.LocalDateTime;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.RequestStateEnum;

public interface RequestProcess {
    String getId();
    RequestStateEnum getStatus();
    LocalDateTime getCreatedAt();
    void setStatus(RequestStateEnum estado, String comentario);
    void setStatus(RequestStateEnum estado);
    String getComment();
}