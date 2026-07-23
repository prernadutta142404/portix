package com.portix.portix.service;

import org.springframework.stereotype.Service;

import com.portix.portix.model.DashboardData;
import com.portix.portix.model.ResumeAnalysis;

@Service
public class DashboardService {

    public DashboardData buildDashboard(ResumeAnalysis analysis, String uploadedFile) {

        DashboardData dashboard = new DashboardData();

        // Resume Score
        dashboard.setResumeScore(analysis.getResumeScore());

        // Resume Completion
        dashboard.setResumeCompletion(analysis.getResumeScore());

        // Portfolio Completion (temporary logic)
        dashboard.setPortfolioCompletion(45);

        // Projects (temporary)
        dashboard.setProjects(6);

        // Certificates (temporary)
        dashboard.setCertificates(4);

        // Uploaded File
        dashboard.setUploadedFile(uploadedFile);

        // Resume Strength
        if (analysis.getResumeScore() >= 85) {
            dashboard.setResumeStrength("Excellent ⭐⭐⭐⭐⭐");
        }
        else if (analysis.getResumeScore() >= 70) {
            dashboard.setResumeStrength("Good ⭐⭐⭐⭐");
        }
        else if (analysis.getResumeScore() >= 50) {
            dashboard.setResumeStrength("Average ⭐⭐⭐");
        }
        else {
            dashboard.setResumeStrength("Needs Improvement ⭐⭐");
        }

        return dashboard;
    }
}