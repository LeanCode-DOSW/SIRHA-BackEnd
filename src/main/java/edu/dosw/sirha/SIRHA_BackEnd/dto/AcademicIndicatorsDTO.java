package edu.dosw.sirha.SIRHA_BackEnd.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.Careers;
import edu.dosw.sirha.SIRHA_BackEnd.domain.model.enums.SemaforoColores;

public class AcademicIndicatorsDTO {

    private double overallProgressPercentage;
    
    private SemaforoColores globalProgressIndicator;
    
    private double academicSuccessRate;
    
    private double creditsCompletionPercentage;
    
    private boolean academicRisk;
    
    private String academicStatus;
    
    private Map<SemaforoColores, Double> trafficLightSummary;

    public AcademicIndicatorsDTO(double overallProgressPercentage, SemaforoColores globalProgressIndicator,
            double academicSuccessRate, double creditsCompletionPercentage, boolean academicRisk,
            String academicStatus, Map<SemaforoColores, Double> trafficLightSummary) {
        this.overallProgressPercentage = overallProgressPercentage;
        this.globalProgressIndicator = globalProgressIndicator;
        this.academicSuccessRate = academicSuccessRate;
        this.creditsCompletionPercentage = creditsCompletionPercentage;
        this.academicRisk = academicRisk;
        this.academicStatus = academicStatus;
        this.trafficLightSummary = trafficLightSummary;

    }

    public double getOverallProgressPercentage() {
        return overallProgressPercentage;
    }
    public SemaforoColores getGlobalProgressIndicator() {
        return globalProgressIndicator;
    }
    public double getAcademicSuccessRate() {
        return academicSuccessRate;
    }
    public double getCreditsCompletionPercentage() {
        return creditsCompletionPercentage;
    }
    public boolean isAcademicRisk() {
        return academicRisk;
    }
    public String getAcademicStatus() {
        return academicStatus;
    }
    public Map<SemaforoColores, Double> getTrafficLightSummary() {
        return trafficLightSummary;
    }

    
}
