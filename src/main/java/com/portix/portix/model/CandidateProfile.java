
	package com.portix.portix.model;

	import java.util.List;

	public class CandidateProfile {

	    private String name;

	    private String email;

	    private String phone;

	    private String recommendedRole;

	    private List<String> skills;
	    
	    private String summary;

	    private List<String> projects;

	    private List<String> education;

	    private List<String> experience;

	    private List<String> certificates;
	    
	   

	    private String github;

	    private String linkedin;

	    private int resumeScore;

	    // ================= GETTERS & SETTERS =================

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getRecommendedRole() {
	        return recommendedRole;
	    }

	    public void setRecommendedRole(String recommendedRole) {
	        this.recommendedRole = recommendedRole;
	    }

	    public List<String> getSkills() {
	        return skills;
	    }

	    public void setSkills(List<String> skills) {
	        this.skills = skills;
	    }

	    public List<String> getProjects() {
	        return projects;
	    }

	    public void setProjects(List<String> projects) {
	        this.projects = projects;
	    }

	    public List<String> getEducation() {
	        return education;
	    }

	    public void setEducation(List<String> education) {
	        this.education = education;
	    }

	    public List<String> getExperience() {
	        return experience;
	    }

	    public void setExperience(List<String> experience) {
	        this.experience = experience;
	    }

	    public List<String> getCertificates() {
	        return certificates;
	    }

	    public void setCertificates(List<String> certificates) {
	        this.certificates = certificates;
	    }

	    public String getGithub() {
	        return github;
	    }

	    public void setGithub(String github) {
	        this.github = github;
	    }

	    public String getLinkedin() {
	        return linkedin;
	    }

	    public void setLinkedin(String linkedin) {
	        this.linkedin = linkedin;
	    }

	    public int getResumeScore() {
	        return resumeScore;
	    }

	    public void setResumeScore(int resumeScore) {
	        this.resumeScore = resumeScore;
	    }
	    public String getSummary() {
	        return summary;
	    }

	    public void setSummary(String summary) {
	        this.summary = summary;
	    }
	}
