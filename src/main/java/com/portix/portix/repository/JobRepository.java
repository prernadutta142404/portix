package com.portix.portix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portix.portix.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    
    List<Job> findByRecruiterId(Long recruiterId);
    List<Job> findByActiveTrue();
    List<Job> findByStatus(String status);

}