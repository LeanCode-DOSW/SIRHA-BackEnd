package edu.dosw.sirha.sirha_backend.dto;
import java.time.LocalDateTime;

import edu.dosw.sirha.sirha_backend.domain.model.enums.RequestStateEnum;

public class ResponseRequest {
    private LocalDateTime responseDate;
    private String responseComment;
    private RequestStateEnum state;

    public ResponseRequest(String responseComment, RequestStateEnum state) {
        this.responseDate = LocalDateTime.now();
        this.responseComment = responseComment;
        this.state = state;

    }
    
    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public String getResponseComment() {
        return responseComment;
    }

    public RequestStateEnum getState() {
        return state;
    }
}