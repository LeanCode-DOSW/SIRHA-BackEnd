package edu.dosw.sirha.sirha_backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO simplificado para Schedule.
 */
public class ScheduleDTO {
    @NotBlank
    private String day;

    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;

    public ScheduleDTO() {}

    public ScheduleDTO(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}