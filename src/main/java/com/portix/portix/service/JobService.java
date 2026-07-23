package com.portix.portix.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.portix.portix.entity.Job;
import com.portix.portix.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {

        this.jobRepository = jobRepository;

    }

    public Job saveJob(Job job) {

        job.setPostedDate(LocalDate.now());

        // New jobs must be reviewed by admin first
        job.setStatus("PENDING_REVIEW");

        // Job should not be visible to candidates yet
        job.setActive(false);

        return jobRepository.save(job);

    }

    public List<Job> getJobsByRecruiterId(Long recruiterId) {

        return jobRepository.findByRecruiterId(recruiterId);

    }
    
    public List<Job> getAllActiveJobs() {
        return jobRepository.findByActiveTrue();
    }
    
    public void deleteJob(Long id) {

        jobRepository.deleteById(id);

    }
    
    public Job getJobById(Long id) {

        return jobRepository.findById(id).orElse(null);

    }

    public Job updateJob(Job job) {

        return jobRepository.save(job);

    }

    public List<Job> getAllJobs() {

        return jobRepository.findAll();

    }
    
    public List<Job> getJobs() {

        return jobRepository.findAll();

    }
    
    public List<Job> getJobsByStatus(String status) {

        return jobRepository.findByStatus(status);

    }

}