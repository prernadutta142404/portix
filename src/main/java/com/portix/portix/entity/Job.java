package com.portix.portix.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String companyName;

    private String location;

    private String employmentType;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(String requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public LocalDate getPostedDate() {
		return postedDate;
		
	}

	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	private String experience;

    private String salary;

    @Lob
    private String description;
    
    private LocalDate postedDate;

    private Long recruiterId;

    private boolean active = false;

    @Column(nullable = false)
    private String status = "PENDING_REVIEW";

    @Lob
    private String requiredSkills;

    
    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }
    
}