package com.portix.portix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.portix.portix.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);
	boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
	
	List<Application> findByJobId(Long jobId);
	List<Application> findByCandidateId(Long candidateId);
	
	
}