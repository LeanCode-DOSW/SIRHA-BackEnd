package edu.dosw.sirha.sirha_backend.dto;

/**
 * DTO para PrerequisiteRule.
 */
public class PrerequisiteDTO {
    private String type; // "APPROVED_SUBJECT", "MIN_CREDITS", etc.
    private String subjectId;
    private Integer minCredits;

    public PrerequisiteDTO() {}

    public PrerequisiteDTO(String type, String subjectId) {
        this.type = type;
        this.subjectId = subjectId;
    }

    public String getType() {
        return type;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public Integer getMinCredits() {
        return minCredits;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setMinCredits(Integer minCredits) {
        this.minCredits = minCredits;
    }
}