package com.portix.portix.model;

import java.util.List;

public class RoleMatch {

    private String roleName;

    private int matchPercentage;

    private String category;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private List<String> learningRoadmap;

    public RoleMatch() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public List<String> getLearningRoadmap() {
        return learningRoadmap;
    }

    public void setLearningRoadmap(List<String> learningRoadmap) {
        this.learningRoadmap = learningRoadmap;
    }

}