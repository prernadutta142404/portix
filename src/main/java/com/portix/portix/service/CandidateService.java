package com.portix.portix.service;

import com.portix.portix.model.CandidateProfile;
import com.portix.portix.model.ResumeAnalysis;
import org.springframework.stereotype.Service;

/**
 * ==========================================================
 * PORTIX - Candidate Service
 * ==========================================================
 *
 * Responsibilities
 * ----------------
 * 1. Resume Upload Processing
 * 2. ATS Analysis
 * 3. Resume Parsing
 * 4. Dashboard Data Management
 * 5. Profile Completion Calculation
 * 6. Portfolio Completion Calculation
 *
 * NOTE
 * ----
 * This service contains only business logic.
 * No HTML or Thymeleaf code should be written here.
 *
 * Current Version
 * ---------------
 * Dynamic Candidate Dashboard
 *
 * Future Versions
 * ---------------
 * Database Integration
 * Multi-user Dashboard
 * Resume History
 * Portfolio Builder
 * ==========================================================
 */

@Service
public class CandidateService {

    // ==========================================================
    // Services
    // ==========================================================

    private final ResumeService resumeService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final ResumeParserService resumeParserService;

    // ==========================================================
    // Dashboard Data
    // ==========================================================

    private CandidateProfile lastProfile;
    private ResumeAnalysis lastAnalysis;

    private boolean resumeUploaded = false;

    private String uploadedResumeName = "Not Available";

    private int resumeScore = 0;

    private int atsScore = 0;

    private int profileCompletion = 0;

    private int portfolioCompletion = 0;

    private int projectCount = 0;

    private int skillsCount = 0;

    private int certificateCount = 0;

    private int educationCount = 0;

    private int experienceCount = 0;
    // ==========================================================
    // Constructor
    // ==========================================================

    public CandidateService(
            ResumeService resumeService,
            ResumeAnalysisService resumeAnalysisService,
            ResumeParserService resumeParserService) {

        this.resumeService = resumeService;
        this.resumeAnalysisService = resumeAnalysisService;
        this.resumeParserService = resumeParserService;

        initializeDashboard();
    }

    // ==========================================================
    // Dashboard Initialization
    // ==========================================================

    /**
     * Default dashboard for a new user.
     * No fake data.
     * Everything starts from zero.
     */

    private void initializeDashboard() {

        lastProfile = new CandidateProfile();

        lastAnalysis = null;

        resumeUploaded = false;

        uploadedResumeName = "Not Available";

        resumeScore = 0;

        atsScore = 0;

        profileCompletion = 0;

        portfolioCompletion = 0;

        projectCount = 0;

        skillsCount = 0;

        certificateCount = 0;

        educationCount = 0;
        
        experienceCount = 0;
    }
    // ==========================================================
    // Resume Upload
    // ==========================================================

    /**
     * Uploads a resume and updates the complete dashboard.
     *
     * Flow:
     * Resume PDF
     *      ↓
     * Extract Text
     *      ↓
     * ATS Analysis
     *      ↓
     * Resume Parsing
     *      ↓
     * Update Dashboard
     */

    public CandidateProfile uploadResume(String filePath) throws Exception {

        // -----------------------------
        // Extract Resume Text
        // -----------------------------

        String resumeText = resumeService.extractText(filePath);

        // -----------------------------
        // ATS Analysis
        // -----------------------------

        ResumeAnalysis analysis =
                resumeAnalysisService.analyze(resumeText);

        // -----------------------------
        // Parse Candidate Profile
        // -----------------------------

        CandidateProfile profile =
                resumeParserService.parse(resumeText, analysis);

        // -----------------------------
        // Store Latest Objects
        // -----------------------------

        lastProfile = profile;
        lastAnalysis = analysis;

        // -----------------------------
        // Resume Information
        // -----------------------------

        resumeUploaded = true;

        uploadedResumeName =
                new java.io.File(filePath).getName();

        // -----------------------------
        // Dashboard Scores
        // -----------------------------

        atsScore = analysis.getResumeScore();

        resumeScore = analysis.getResumeScore();

        // -----------------------------
        // Dashboard Counts
        // -----------------------------

        skillsCount = profile.getSkills() == null
                ? 0
                : profile.getSkills().size();

        projectCount = profile.getProjects() == null
                ? 0
                : profile.getProjects().size();

        certificateCount = profile.getCertificates() == null
                ? 0
                : profile.getCertificates().size();
        educationCount = profile.getEducation() == null
                ? 0
                : profile.getEducation().size();
        
        experienceCount = profile.getExperience() == null
                ? 0
                : profile.getExperience().size();
        
        // -----------------------------
        // Dynamic Completion
        // -----------------------------

        profileCompletion =
                calculateProfileCompletion(profile);

        portfolioCompletion =
                calculatePortfolioCompletion(profile);

        return profile;
    }
    // ==========================================================
    // Dashboard Getters
    // ==========================================================

    public CandidateProfile getLastProfile() {
        return lastProfile;
    }

    public ResumeAnalysis getLastAnalysis() {
        return lastAnalysis;
    }

    public boolean isResumeUploaded() {
        return resumeUploaded;
    }

    public String getUploadedResumeName() {
        return uploadedResumeName;
    }

    public int getResumeScore() {
        return resumeScore;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public int getProfileCompletion() {
        return profileCompletion;
    }

    public int getPortfolioCompletion() {
        return portfolioCompletion;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public int getSkillsCount() {
        return skillsCount;
    }

    public int getCertificateCount() {
        return certificateCount;
    }

    public int getEducationCount() {
        return educationCount;
    }
    
    public int getExperienceCount() {
        return experienceCount;
    }

    // ==========================================================
    // Resume Utilities
    // ==========================================================

    public boolean hasResume(String filePath) {

        return resumeService.resumeExists(filePath);

    }

    public String extractResumeText(String filePath) throws Exception {

        return resumeService.extractText(filePath);

    }

    public String getResumePreview(String filePath) throws Exception {

        String resumeText = resumeService.extractText(filePath);

        return resumeService.getResumePreview(resumeText);

    }

    public int getResumeWordCount(String filePath) throws Exception {

        String resumeText = resumeService.extractText(filePath);

        return resumeService.getWordCount(resumeText);

    }

    public int getResumeLineCount(String filePath) throws Exception {

        String resumeText = resumeService.extractText(filePath);

        return resumeService.getLineCount(resumeText);

    }

    // ==========================================================
    // Empty Dashboard
    // ==========================================================

    public void resetDashboard() {

        initializeDashboard();

    }
    // ==========================================================
    // Profile Completion
    // ==========================================================

    private int calculateProfileCompletion(CandidateProfile profile) {

        int completed = 0;
        int total = 10;

        if (profile.getName() != null && !profile.getName().isBlank())
            completed++;

        if (profile.getEmail() != null && !profile.getEmail().isBlank())
            completed++;

        if (profile.getPhone() != null && !profile.getPhone().isBlank())
            completed++;

        if (profile.getSummary() != null && !profile.getSummary().isBlank())
            completed++;

        if (profile.getSkills() != null && !profile.getSkills().isEmpty())
            completed++;

        if (profile.getEducation() != null && !profile.getEducation().isEmpty())
            completed++;

        if (profile.getProjects() != null && !profile.getProjects().isEmpty())
            completed++;

        if (profile.getExperience() != null && !profile.getExperience().isEmpty())
            completed++;

        if (profile.getCertificates() != null && !profile.getCertificates().isEmpty())
            completed++;

        return (completed * 100) / total;
    }

    // ==========================================================
    // Portfolio Completion
    // ==========================================================
    private int calculatePortfolioCompletion(CandidateProfile profile) {

        int completed = 0;
        int total = 8;

        if (profile.getSummary() != null && !profile.getSummary().isBlank())
            completed++;

        if (profile.getProjects() != null && !profile.getProjects().isEmpty())
            completed++;

        if (profile.getGithub() != null && !profile.getGithub().isBlank())
            completed++;

        if (profile.getLinkedin() != null && !profile.getLinkedin().isBlank())
            completed++;

        if (profile.getCertificates() != null && !profile.getCertificates().isEmpty())
            completed++;

        if (profile.getEducation() != null && !profile.getEducation().isEmpty())
            completed++;

        if (profile.getExperience() != null && !profile.getExperience().isEmpty())
            completed++;

        if (resumeUploaded)
            completed++;

        return (completed * 100) / total;
    }
    
    // ==========================================================
    // Future Features
    // ==========================================================

    public void updateProfile(CandidateProfile profile) {

        this.lastProfile = profile;

        profileCompletion = calculateProfileCompletion(profile);

        portfolioCompletion = calculatePortfolioCompletion(profile);
    }

    public boolean deleteResume(String filePath) {

        initializeDashboard();

        return true;
    }

}
