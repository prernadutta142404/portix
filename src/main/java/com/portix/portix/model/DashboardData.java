package com.portix.portix.model;

public class DashboardData {

    private int resumeScore;
    private int portfolioCompletion;
    private int projects;
    private int certificates;
    private int resumeCompletion;
    private String resumeStrength;
    private String uploadedFile;

    public DashboardData() {
    }

    public DashboardData(int resumeScore,
                         int portfolioCompletion,
                         int projects,
                         int certificates,
                         int resumeCompletion,
                         String resumeStrength,
                         String uploadedFile) {

        this.resumeScore = resumeScore;
        this.portfolioCompletion = portfolioCompletion;
        this.projects = projects;
        this.certificates = certificates;
        this.resumeCompletion = resumeCompletion;
        this.resumeStrength = resumeStrength;
        this.uploadedFile = uploadedFile;
    }

    public int getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(int resumeScore) {
        this.resumeScore = resumeScore;
    }

    public int getPortfolioCompletion() {
        return portfolioCompletion;
    }

    public void setPortfolioCompletion(int portfolioCompletion) {
        this.portfolioCompletion = portfolioCompletion;
    }

    public int getProjects() {
        return projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getCertificates() {
        return certificates;
    }

    public void setCertificates(int certificates) {
        this.certificates = certificates;
    }

    public int getResumeCompletion() {
        return resumeCompletion;
    }

    public void setResumeCompletion(int resumeCompletion) {
        this.resumeCompletion = resumeCompletion;
    }

    public String getResumeStrength() {
        return resumeStrength;
    }

    public void setResumeStrength(String resumeStrength) {
        this.resumeStrength = resumeStrength;
    }

    public String getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(String uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

}