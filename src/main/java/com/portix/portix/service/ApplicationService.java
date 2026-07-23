package com.portix.portix.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.portix.portix.entity.Application;
import com.portix.portix.repository.ApplicationRepository;
import java.util.List;
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {

        this.applicationRepository = applicationRepository;

    }

    public void apply(Long jobId, Long candidateId) {

        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId)) {
            return;
        }

        Application application = new Application();

        application.setJobId(jobId);
        application.setCandidateId(candidateId);
        application.setAppliedDate(LocalDate.now());
        application.setStatus("Applied");

        applicationRepository.save(application);
    } 
        public boolean hasApplied(Long jobId, Long candidateId) {
            return applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId);
        
        
    }
    
        public List<Application> getApplicationsByJobId(Long jobId) {

            return applicationRepository.findByJobId(jobId);
            
            

        }
        
        public List<Application> getApplicationsByCandidateId(Long candidateId) {
            return applicationRepository.findByCandidateId(candidateId);
        }
        
        public void updateApplicationStatus(Long applicationId, String status) {

            Application app = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            app.setStatus(status);

            applicationRepository.save(app);
        }
    
    
}