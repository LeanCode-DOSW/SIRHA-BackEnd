package edu.dosw.sirha.sirha_backend.dto;

public class RequestApprovalRateDTO {
    private int totalRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private int pendingRequests;
    private int inReviewRequests;

    private double approvalRatePercentage;
    private double rejectionRatePercentage;
    private double pendingRatePercentage;
    private double inReviewRatePercentage;

    public RequestApprovalRateDTO(int totalRequests, int approvedRequests, int rejectedRequests, int pendingRequests, int inReviewRequests) {
        this.totalRequests = totalRequests;
        this.approvedRequests = approvedRequests;
        this.rejectedRequests = rejectedRequests;
        this.pendingRequests = pendingRequests;
        this.inReviewRequests = inReviewRequests;

        if (totalRequests > 0) {
            this.approvalRatePercentage = (approvedRequests / (double) totalRequests) * 100;
            this.rejectionRatePercentage = (rejectedRequests / (double) totalRequests) * 100;
            this.pendingRatePercentage = (pendingRequests / (double) totalRequests) * 100;
            this.inReviewRatePercentage = (inReviewRequests / (double) totalRequests) * 100;
        } else {
            this.approvalRatePercentage = 0.0;
            this.rejectionRatePercentage = 0.0;
            this.pendingRatePercentage = 0.0;
            this.inReviewRatePercentage = 0.0;
        }
    }

    public int getTotalRequests() {
        return totalRequests;
    }
    public int getApprovedRequests() {
        return approvedRequests;
    }
    public int getRejectedRequests() {
        return rejectedRequests;
    }
    public int getPendingRequests() {
        return pendingRequests;
    }
    public int getInReviewRequests() {
        return inReviewRequests;
    }
    public double getApprovalRatePercentage() {
        return approvalRatePercentage;
    }
    public double getRejectionRatePercentage() {
        return rejectionRatePercentage;
    }
    public double getPendingRatePercentage() {
        return pendingRatePercentage;
    }
    public double getInReviewRatePercentage() {
        return inReviewRatePercentage;
    }
    
}
