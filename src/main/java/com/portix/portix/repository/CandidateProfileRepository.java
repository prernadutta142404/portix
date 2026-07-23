package com.portix.portix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portix.portix.entity.CandidateProfile;
import com.portix.portix.entity.User;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(User user);
    Optional<CandidateProfile> findByUserId(Long userId);

}