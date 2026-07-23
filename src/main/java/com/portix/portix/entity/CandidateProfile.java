package com.portix.portix.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 150)
    private String phone;

    @Column(length = 100)
    private String recommendedRole;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column
    private Integer resumeScore;

    @Lob
    private String skills;

    @Lob
    private String projects;

    @Lob
    private String education;

    @Lob
    private String experience;

    @Lob
    private String certificates;

    @Column(length = 255)
    private String github;

    @Column(length = 255)
    private String linkedin;

    @Column(length = 255)
    private String profilePhoto;

    @Column(length = 255)
    private String portfolioUrl;

    @Column(nullable = false)
    private boolean profileCompleted = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getResumeScore() {
		return resumeScore;
	}

	public void setResumeScore(Integer resumeScore) {
		this.resumeScore = resumeScore;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getProjects() {
		return projects;
	}

	public void setProjects(String projects) {
		this.projects = projects;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCertificates() {
		return certificates;
	}

	public void setCertificates(String certificates) {
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

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getPortfolioUrl() {
		return portfolioUrl;
	}

	public void setPortfolioUrl(String portfolioUrl) {
		this.portfolioUrl = portfolioUrl;
	}

	public boolean isProfileCompleted() {
		return profileCompleted;
	}

	public void setProfileCompleted(boolean profileCompleted) {
		this.profileCompleted = profileCompleted;
	}
    
    
}