package com.portix.portix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.portix.portix.entity.Job;
import com.portix.portix.service.JobService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final JobService jobService;

    public AdminController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/admin/jobs")
    public String pendingJobs(
            Model model,
            HttpSession session) {

        String role =
                (String) session.getAttribute("loggedInUserRole");

        if (role == null ||
            !"ADMIN".equalsIgnoreCase(role)) {

            return "redirect:/login";
        }

        model.addAttribute(
                "jobs",
                jobService.getJobsByStatus("PENDING_REVIEW")
        );

        return "admin/jobs";
    }
    
    @GetMapping("/admin/jobs/approve/{id}")
    public String approveJob(
            @PathVariable Long id,
            HttpSession session) {

        String role =
                (String) session.getAttribute("loggedInUserRole");

        if (role == null ||
            !"ADMIN".equalsIgnoreCase(role)) {

            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        if (job != null &&
            "PENDING_REVIEW".equals(job.getStatus())) {

            job.setStatus("PAYMENT_PENDING");
            job.setActive(false);

            jobService.updateJob(job);
        }

        return "redirect:/admin/jobs";
    }


    @GetMapping("/admin/jobs/reject/{id}")
    public String rejectJob(
            @PathVariable Long id,
            HttpSession session) {

        String role =
                (String) session.getAttribute("loggedInUserRole");

        if (role == null ||
            !"ADMIN".equalsIgnoreCase(role)) {

            return "redirect:/login";
        }

        Job job = jobService.getJobById(id);

        if (job != null &&
            "PENDING_REVIEW".equals(job.getStatus())) {

            job.setStatus("REJECTED");
            job.setActive(false);

            jobService.updateJob(job);
        }

        return "redirect:/admin/jobs";
    }
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/login";
    }
    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            Model model,
            HttpSession session) {

        String role =
                (String) session.getAttribute("loggedInUserRole");

        if (role == null ||
            !"ADMIN".equalsIgnoreCase(role)) {

            return "redirect:/login";
        }

        // Existing dashboard model.addAttribute code
        // exactly same rahega

        model.addAttribute(
                "totalJobs",
                jobService.getAllJobs().size()
        );

        model.addAttribute(
                "pendingJobs",
                jobService.getJobsByStatus("PENDING_REVIEW").size()
        );

        model.addAttribute(
                "paymentPendingJobs",
                jobService.getJobsByStatus("PAYMENT_PENDING").size()
        );

        model.addAttribute(
                "publishedJobs",
                jobService.getJobsByStatus("PUBLISHED").size()
        );

        model.addAttribute(
                "rejectedJobs",
                jobService.getJobsByStatus("REJECTED").size()
        );

        model.addAttribute(
                "closedJobs",
                jobService.getJobsByStatus("CLOSED").size()
        );

        return "admin/dashboard";
    }
    
    
}