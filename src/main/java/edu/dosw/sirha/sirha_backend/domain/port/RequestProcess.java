package edu.dosw.sirha.sirha_backend.domain.port;

import java.time.LocalDateTime;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;

public interface RequestProcess {
    String getId();
    RequestStateEnum getStatus();
    LocalDateTime getCreatedAt();
    void setStatus(RequestStateEnum estado, String comentario);
    void setStatus(RequestStateEnum estado);
    String getComment();
}