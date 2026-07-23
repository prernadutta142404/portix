package com.portix.portix.controller;

import com.portix.portix.model.CandidateProfile;
import com.portix.portix.service.CandidateService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.portix.portix.entity.User;
import com.portix.portix.service.CandidateProfileService;
import com.portix.portix.service.JobService;
import org.springframework.web.bind.annotation.PathVariable;
import com.portix.portix.service.ApplicationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.portix.portix.entity.Job;
import jakarta.servlet.http.HttpSession;
import com.portix.portix.service.UserService;
import java.util.Arrays;
import java.util.ArrayList;

@Controller
public class CandidateController {
	
	

    // ==========================================================
    // Services
    // ==========================================================

    private final CandidateService candidateService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final CandidateProfileService candidateProfileService;
    private final UserService userService;

    // ==========================================================
    // Constructor
    // ==========================================================

    
    public CandidateController(
            CandidateService candidateService,
            JobService jobService,
            ApplicationService applicationService,
            CandidateProfileService candidateProfileService,
            UserService userService) {

        this.candidateService = candidateService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.candidateProfileService = candidateProfileService;
        this.userService = userService;
    }
    
    // ==========================================================
    // Candidate Dashboard
    // ==========================================================

    @GetMapping("/candidate/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Long candidateId =
                (Long) session.getAttribute("loggedInUserId");

        if (candidateId == null) {
            return "redirect:/login";
        }

        model.addAttribute("profile", candidateService.getLastProfile());

        model.addAttribute("analysis", candidateService.getLastAnalysis());

        model.addAttribute("resumeUploaded",
                candidateService.isResumeUploaded());

        model.addAttribute("uploadedResumeName",
                candidateService.getUploadedResumeName());

        model.addAttribute("resumeScore",
                candidateService.getResumeScore());

        model.addAttribute("atsScore",
                candidateService.getAtsScore());

        model.addAttribute("profileCompletion",
                candidateService.getProfileCompletion());

        model.addAttribute("portfolioCompletion",
                candidateService.getPortfolioCompletion());

        model.addAttribute("skillsCount",
                candidateService.getSkillsCount());

        model.addAttribute("projectCount",
                candidateService.getProjectCount());

        model.addAttribute("certificateCount",
                candidateService.getCertificateCount());

        model.addAttribute("educationCount",
                candidateService.getEducationCount());

        model.addAttribute("experienceCount",
                candidateService.getExperienceCount());

        return "candidate/dashboard";
    }
    // ==========================================================
    // Candidate Pages
    // ==========================================================

    @GetMapping("/candidate/resume")
    public String resume() {
        return "candidate/resume";
    }

    @GetMapping("/candidate/profile")
    public String profile(Model model) {

        model.addAttribute(
                "profile",
                candidateService.getLastProfile());

        model.addAttribute(
                "analysis",
                candidateService.getLastAnalysis());

        return "candidate/profile";
    }

    @GetMapping("/candidate/portfolio")
    public String portfolio(Model model, HttpSession session) {

        model.addAttribute(
                "profile",
                candidateService.getLastProfile());

        model.addAttribute(
                "analysis",
                candidateService.getLastAnalysis());

        Long candidateId =
                (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("candidateId", candidateId);
        return "candidate/portfolio";
    }
    
    @GetMapping("/portfolio/share")
    public String sharedPortfolio(Model model) {

        model.addAttribute(
                "profile",
                candidateService.getLastProfile()
        );

        model.addAttribute(
                "analysis",
                candidateService.getLastAnalysis()
        );

        return "candidate/portfolio";
    }
    
    @GetMapping("/portfolio/share/{candidateId}")
    public String sharedCandidatePortfolio(
            @PathVariable Long candidateId,
            Model model) {

        var dbProfileOptional =
                candidateProfileService.findByUserId(candidateId);

        if (dbProfileOptional.isEmpty()) {
            return "redirect:/";
        }

        var dbProfile = dbProfileOptional.get();

        CandidateProfile profile = new CandidateProfile();

        profile.setName(dbProfile.getUser().getFullName());
        profile.setEmail(dbProfile.getUser().getEmail());
        profile.setPhone(dbProfile.getPhone());
        profile.setRecommendedRole(dbProfile.getRecommendedRole());
        profile.setSummary(dbProfile.getSummary());
        profile.setGithub(dbProfile.getGithub());
        profile.setLinkedin(dbProfile.getLinkedin());
        profile.setResumeScore(dbProfile.getResumeScore());

        profile.setSkills(
                dbProfile.getSkills() != null
                        ? Arrays.asList(dbProfile.getSkills().split("\\|\\|\\|"))
                        : new ArrayList<>()
        );

        profile.setProjects(
                dbProfile.getProjects() != null
                        ? Arrays.asList(dbProfile.getProjects().split("\\|\\|\\|"))
                        : new ArrayList<>()
        );

        profile.setEducation(
                dbProfile.getEducation() != null
                        ? Arrays.asList(dbProfile.getEducation().split("\\|\\|\\|"))
                        : new ArrayList<>()
        );

        profile.setExperience(
                dbProfile.getExperience() != null
                        ? Arrays.asList(dbProfile.getExperience().split("\\|\\|\\|"))
                        : new ArrayList<>()
        );

        profile.setCertificates(
                dbProfile.getCertificates() != null
                        ? Arrays.asList(dbProfile.getCertificates().split("\\|\\|\\|"))
                        : new ArrayList<>()
        );

        model.addAttribute("profile", profile);
        model.addAttribute("analysis", null);
        model.addAttribute("isPublicPortfolio", true);

        return "candidate/portfolio";
    }
    @GetMapping("/candidate/ats")
    public String ats() {
        return "candidate/ats";
    }

    // ==========================================================
    // Resume Upload
    // ==========================================================

    @PostMapping("/candidate/uploadResume")
    public String uploadResume(
            @RequestParam("resume") MultipartFile file,
            Model model,
            HttpSession session) {

        try {
        	
        	Long candidateId =
        	        (Long) session.getAttribute("loggedInUserId");

        	if (candidateId == null) {
        	    return "redirect:/login";
        	}
        	
        	User loggedInUser = userService.findById(candidateId);

        	if (loggedInUser == null) {
        	    return "redirect:/login";
        	}

            // -----------------------------
            // Create Upload Directory
            // -----------------------------

            String uploadDir = "uploads/resumes/";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            

            // -----------------------------
            // Save Resume
            // -----------------------------

            String fileName = file.getOriginalFilename();

            Path path = Paths.get(uploadDir, fileName);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // -----------------------------
            // Process Resume
            // -----------------------------

            CandidateProfile profile =
                    candidateService.uploadResume(path.toString());

            com.portix.portix.entity.CandidateProfile dbProfile =
                    candidateProfileService.findByUser(loggedInUser)
                            .orElse(new com.portix.portix.entity.CandidateProfile());

            dbProfile.setUser(loggedInUser);

            dbProfile.setPhone(profile.getPhone());
            dbProfile.setSummary(profile.getSummary());
            dbProfile.setGithub(profile.getGithub());
            dbProfile.setLinkedin(profile.getLinkedin());

            dbProfile.setRecommendedRole(
                    candidateService.getLastAnalysis() != null
                            ? candidateService.getLastAnalysis().getRecommendedRole()
                            : null
            );

            dbProfile.setResumeScore(
                    candidateService.getResumeScore()
            );
            
            dbProfile.setSkills(
                    profile.getSkills() != null
                            ? String.join("|||", profile.getSkills())
                            : null
            );

            dbProfile.setProjects(
                    profile.getProjects() != null
                            ? String.join("|||", profile.getProjects())
                            : null
            );

            dbProfile.setEducation(
                    profile.getEducation() != null
                            ? String.join("|||", profile.getEducation())
                            : null
            );

            dbProfile.setExperience(
                    profile.getExperience() != null
                            ? String.join("|||", profile.getExperience())
                            : null
            );

            dbProfile.setCertificates(
                    profile.getCertificates() != null
                            ? String.join("|||", profile.getCertificates())
                            : null
            );
            candidateProfileService.save(dbProfile);
            // -----------------------------
            // Dashboard Data
            // -----------------------------

            model.addAttribute("profile", profile);

            model.addAttribute("analysis",
                    candidateService.getLastAnalysis());

            model.addAttribute("resumeUploaded",
                    candidateService.isResumeUploaded());

            model.addAttribute("uploadedResumeName",
                    candidateService.getUploadedResumeName());

            model.addAttribute("resumeScore",
                    candidateService.getResumeScore());

            model.addAttribute("atsScore",
                    candidateService.getAtsScore());

            model.addAttribute("profileCompletion",
                    candidateService.getProfileCompletion());

            model.addAttribute("portfolioCompletion",
                    candidateService.getPortfolioCompletion());

            model.addAttribute("skillsCount",
                    candidateService.getSkillsCount());

            model.addAttribute("projectCount",
                    candidateService.getProjectCount());

            model.addAttribute("certificateCount",
                    candidateService.getCertificateCount());

            model.addAttribute("educationCount",
                    candidateService.getEducationCount());
            
            model.addAttribute("experienceCount",
                    candidateService.getExperienceCount());

            model.addAttribute("success",
                    "Resume uploaded successfully!");

            return "candidate/dashboard";

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error",
                    "Resume upload failed!");

            return "candidate/dashboard";
        }
        }
        // ==========================================================
        // Analysis Page
        // ==========================================================

        @GetMapping("/candidate/analysis")
        public String analysis(Model model) {

            model.addAttribute("analysis",
                    candidateService.getLastAnalysis());

            model.addAttribute("resumeUploaded",
                    candidateService.isResumeUploaded());

            model.addAttribute("uploadedResumeName",
                    candidateService.getUploadedResumeName());
            model.addAttribute("projectCount",
                    candidateService.getProjectCount());
            return "candidate/analysis";
        }
        
        
        @GetMapping("/candidate/jobs")
        public String jobs(Model model, HttpSession session) {

            List<Job> jobs = jobService.getAllActiveJobs();

            Long candidateId =
                    (Long) session.getAttribute("loggedInUserId");

            if (candidateId == null) {
                return "redirect:/login";
            }

            Map<Long, Boolean> appliedJobs = new HashMap<>();

            for (Job job : jobs) {
                appliedJobs.put(
                    job.getId(),
                    applicationService.hasApplied(
                        job.getId(),
                        candidateId
                    )
                );
            }

            model.addAttribute("jobs", jobs);
            model.addAttribute("appliedJobs", appliedJobs);

            return "candidate/jobs";
        }
        
        @GetMapping("/candidate/apply/{id}")
        public String apply(@PathVariable Long id, HttpSession session) {

            Long candidateId =
                    (Long) session.getAttribute("loggedInUserId");

            if (candidateId == null) {
                return "redirect:/login";
            }

            applicationService.apply(id, candidateId);

            return "redirect:/candidate/jobs";
        }
        
        @GetMapping("/candidate/applications")
        public String myApplications(Model model, HttpSession session) {        

        	Long candidateId =
        	        (Long) session.getAttribute("loggedInUserId");

        	if (candidateId == null) {
        	    return "redirect:/login";
        	}

            var applications =
                    applicationService.getApplicationsByCandidateId(candidateId);

            Map<Long, Job> applicationJobs = new HashMap<>();

            for (var app : applications) {
                applicationJobs.put(
                        app.getId(),
                        jobService.getJobById(app.getJobId())
                );
            }

            model.addAttribute("applications", applications);
            model.addAttribute("applicationJobs", applicationJobs);

            return "candidate/applications";
        }
        
        // ==========================================================
        // Resume Text Preview (Testing)
        // ==========================================================

        @GetMapping("/candidate/test")
        public String testResume(Model model) {

            try {

                if (!candidateService.isResumeUploaded()) {

                    model.addAttribute(
                            "text",
                            "No resume uploaded."
                    );

                } else {

                    String text = candidateService.extractResumeText(
                            "uploads/resumes/"
                            + candidateService.getUploadedResumeName()
                    );

                    model.addAttribute("text", text);
                }

            } catch (Exception e) {

                model.addAttribute("text", e.getMessage());

            }

            return "candidate/test";
        

    }
    }