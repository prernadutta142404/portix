package com.portix.portix.model;

import java.util.List;

public class ResumeAnalysis {

    private int resumeScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> suggestions;
    private String resumeStrength;
    private int matchedSkillCount;
    private int totalSkillCount;
    public ResumeAnalysis() {
    }

    public ResumeAnalysis(int resumeScore,
                          List<String> matchedSkills,
                          List<String> missingSkills,
                          List<String> suggestions) {
        this.resumeScore = resumeScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.suggestions = suggestions;
    }

    public int getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(int resumeScore) {
        this.resumeScore = resumeScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
    public String getResumeStrength() {
        return resumeStrength;
    }

    public void setResumeStrength(String resumeStrength) {
        this.resumeStrength = resumeStrength;
    }

    public int getMatchedSkillCount() {
        return matchedSkillCount;
    }

    public void setMatchedSkillCount(int matchedSkillCount) {
        this.matchedSkillCount = matchedSkillCount;
    }

    public int getTotalSkillCount() {
        return totalSkillCount;
    }

    public void setTotalSkillCount(int totalSkillCount) {
        this.totalSkillCount = totalSkillCount;
    }
    private String recommendedRole;
    public String getRecommendedRole() {
        return recommendedRole;
    }

    public void setRecommendedRole(String recommendedRole) {
        this.recommendedRole = recommendedRole;
    }
}