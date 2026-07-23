package com.portix.portix.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portix.portix.entity.CandidateProfile;
import com.portix.portix.entity.User;
import com.portix.portix.repository.CandidateProfileRepository;


@Service
public class CandidateProfileService {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    public CandidateProfile save(CandidateProfile profile) {

        return candidateProfileRepository.save(profile);

    }

    public Optional<CandidateProfile> findByUser(User user) {

        return candidateProfileRepository.findByUser(user);

    }
    
 // ADD THIS HERE
    public Optional<CandidateProfile> findByUserId(Long userId) {

        return candidateProfileRepository.findByUserId(userId);

    }

}