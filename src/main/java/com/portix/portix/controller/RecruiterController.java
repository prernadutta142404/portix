package com.portix.portix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.portix.portix.entity.Job;
import com.portix.portix.service.JobService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.portix.portix.service.ApplicationService;
import com.portix.portix.repository.UserRepository;
import com.portix.portix.entity.User;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@Controller
public class RecruiterController {
	
	private final UserRepository userRepository;
	
	private final JobService jobService;
	private final ApplicationService applicationService;
	public RecruiterController(
	        JobService jobService,
	        ApplicationService applicationService,
	        UserRepository userRepository) {

	    this.jobService = jobService;
	    this.applicationService = applicationService;
	    this.userRepository = userRepository;
	}
    
	@GetMapping("/recruiter/dashboard")
	public String dashboard(
	        Model model,
	        HttpSession session) {

	    Long recruiterId =
	            (Long) session.getAttribute("loggedInUserId");

	    if (recruiterId == null) {
	        return "redirect:/login";
	    }

	    var recruiterJobs =
	            jobService.getJobsByRecruiterId(recruiterId);

	    long totalJobs = recruiterJobs.size();

	    long pendingJobs = recruiterJobs.stream()
	            .filter(job -> "PENDING_REVIEW".equals(job.getStatus()))
	            .count();

	    long publishedJobs = recruiterJobs.stream()
	            .filter(job -> "PUBLISHED".equals(job.getStatus()))
	            .count();

	    long closedJobs = recruiterJobs.stream()
	            .filter(job -> "CLOSED".equals(job.getStatus()))
	            .count();

	    model.addAttribute("totalJobs", totalJobs);
	    model.addAttribute("pendingJobs", pendingJobs);
	    model.addAttribute("publishedJobs", publishedJobs);
	    model.addAttribute("closedJobs", closedJobs);
	    
	    var recentCandidates = recruiterJobs.stream()
	            .flatMap(job ->
	                    applicationService
	                        .getApplicationsByJobId(job.getId())
	                        .stream()
	            )
	            .limit(5)
	            .map(application ->
	                    userRepository
	                        .findById(application.getCandidateId())
	                        .orElse(null)
	            )
	            .filter(user -> user != null)
	            .toList();

	    model.addAttribute(
	            "recentCandidates",
	            recentCandidates
	    );

	    return "recruiter/dashboard";
	}
    
	@GetMapping("/recruiter/candidates")
	public String candidates(
	        Model model,
	        HttpSession session) {

	    Long recruiterId =
	            (Long) session.getAttribute("loggedInUserId");

	    if (recruiterId == null) {
	        return "redirect:/login";
	    }

	    var recruiterJobs =
	            jobService.getJobsByRecruiterId(recruiterId);

	    var applications = recruiterJobs.stream()
	            .flatMap(job ->
	                    applicationService
	                        .getApplicationsByJobId(job.getId())
	                        .stream()
	            )
	            .toList();

	    model.addAttribute(
	            "applications",
	            applications
	    );
	    var candidates = applications.stream()
	            .map(application ->
	                    userRepository
	                        .findById(application.getCandidateId())
	                        .orElse(null)
	            )
	            .filter(user -> user != null)
	            .toList();

	    model.addAttribute(
	            "candidates",
	            candidates
	    );
	    
	    Map<Long, String> jobTitles = new HashMap<>();

	    for (Job job : recruiterJobs) {
	        jobTitles.put(
	                job.getId(),
	                job.getJobTitle()
	        );
	    }

	    model.addAttribute(
	            "jobTitles",
	            jobTitles
	    );

	    return "recruiter/candidates";
	}
    
    @GetMapping("/recruiter/jobs")
    public String jobs(
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "jobs",
                jobService.getJobsByRecruiterId(recruiterId)
        );

        return "recruiter/jobs";
    }
    
    @GetMapping("/recruiter/jobs/create")
    public String createJobForm(Model model) {

        model.addAttribute("job", new Job());

        return "recruiter/create-job";

    }
    
    @PostMapping("/recruiter/jobs/save")
    public String saveJob(
            @ModelAttribute Job job,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        job.setRecruiterId(recruiterId);

        jobService.saveJob(job);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/recruiter/jobs/delete/{id}")
    public String deleteJob(
            @PathVariable Long id,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        // Recruiter can only manage their own job
        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        // Published or closed jobs must never be permanently deleted
        if ("PUBLISHED".equals(job.getStatus()) ||
            "CLOSED".equals(job.getStatus())) {

            return "redirect:/recruiter/jobs";
        }

        jobService.deleteJob(id);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/recruiter/jobs/close/{id}")
    public String closeJob(
            @PathVariable Long id,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        // Security: recruiter can close only their own job
        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        // Close the job
        job.setStatus("CLOSED");
        job.setActive(false);

        jobService.updateJob(job);

        return "redirect:/recruiter/jobs";
    }    
    @GetMapping("/recruiter/jobs/edit/{id}")
    public String editJob(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        model.addAttribute("job", job);

        return "recruiter/edit-job";
    }
    
    @PostMapping("/recruiter/jobs/update")
    public String updateJob(
            @ModelAttribute Job job,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job existingJob = jobService.getJobById(job.getId());

        if (existingJob == null ||
            existingJob.getRecruiterId() == null ||
            !existingJob.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        job.setRecruiterId(recruiterId);

        jobService.updateJob(job);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/recruiter/job/{id}/applicants")
    public String viewApplicants(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        model.addAttribute(
                "applications",
                applicationService.getApplicationsByJobId(id));

        model.addAttribute("job", job);

        model.addAttribute(
                "candidates",
                applicationService.getApplicationsByJobId(id)
                    .stream()
                    .map(application -> userRepository
                        .findById(application.getCandidateId())
                        .orElse(null))
                    .toList()
        );

        return "recruiter/applicants";
    }
    
    
    @GetMapping("/recruiter/application/{applicationId}/shortlist/{jobId}")
    public String shortlistApplication(
            @PathVariable Long applicationId,
            @PathVariable Long jobId,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(jobId);

        // Recruiter can manage applicants only for their own job
        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        applicationService.updateApplicationStatus(
                applicationId,
                "Shortlisted"
        );

        return "redirect:/recruiter/job/"
                + jobId
                + "/applicants";
    }

    @GetMapping("/recruiter/application/{applicationId}/reject/{jobId}")
    public String rejectApplication(
            @PathVariable Long applicationId,
            @PathVariable Long jobId,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(jobId);

        // Recruiter can manage applicants only for their own job
        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        applicationService.updateApplicationStatus(
                applicationId,
                "Rejected"
        );

        return "redirect:/recruiter/job/"
                + jobId
                + "/applicants";
    }
    
    @GetMapping("/recruiter/jobs/payment/{id}")
    public String paymentPage(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        if (!"PAYMENT_PENDING".equals(job.getStatus())) {
            return "redirect:/recruiter/jobs";
        }

        model.addAttribute("job", job);

        return "recruiter/payment";
    }
    @PostMapping("/recruiter/jobs/payment/{id}/success")
    public String paymentSuccess(
            @PathVariable Long id,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        // Recruiter can publish only their own job
        if (job == null ||
            job.getRecruiterId() == null ||
            !job.getRecruiterId().equals(recruiterId)) {

            return "redirect:/recruiter/jobs";
        }

        // Only approved jobs waiting for payment can be published
        if (!"PAYMENT_PENDING".equals(job.getStatus())) {
            return "redirect:/recruiter/jobs";
        }

        // Mock payment successful
        job.setStatus("PUBLISHED");
        job.setActive(true);

        jobService.updateJob(job);

        return "redirect:/recruiter/jobs";
    }
    
    @GetMapping("/recruiter/analytics")
    public String analytics(
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        var recruiterJobs =
                jobService.getJobsByRecruiterId(recruiterId);

        var applications = recruiterJobs.stream()
                .flatMap(job ->
                        applicationService
                            .getApplicationsByJobId(job.getId())
                            .stream()
                )
                .toList();

        long totalCandidates = applications.size();

        long shortlisted = applications.stream()
                .filter(app -> "SHORTLISTED".equalsIgnoreCase(app.getStatus()))
                .count();

        long underReview = applications.stream()
                .filter(app -> "APPLIED".equalsIgnoreCase(app.getStatus()))
                .count();

        long rejected = applications.stream()
                .filter(app -> "REJECTED".equalsIgnoreCase(app.getStatus()))
                .count();

        model.addAttribute("totalCandidates", totalCandidates);
        model.addAttribute("shortlisted", shortlisted);
        model.addAttribute("underReview", underReview);
        model.addAttribute("rejected", rejected);

        return "recruiter/analytics";
    }
    
    @GetMapping("/recruiter/candidate/{id}")
    public String candidateDetails(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Long recruiterId =
                (Long) session.getAttribute("loggedInUserId");

        if (recruiterId == null) {
            return "redirect:/login";
        }

        User candidate = userRepository
                .findById(id)
                .orElse(null);

        if (candidate == null) {
            return "redirect:/recruiter/candidates";
        }

        model.addAttribute("candidate", candidate);

        return "recruiter/candidate-details";
    }

 
   

}