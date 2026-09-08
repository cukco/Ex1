package com.example.recruit.Service;

import com.example.recruit.DTO.CandidateCreateDTO;
import com.example.recruit.Models.Candidate;
import com.example.recruit.Repository.CandidateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepo candidateRepo;

    public Candidate save(CandidateCreateDTO candidateCreateDTO) {
        Candidate candidate = new Candidate();
        candidate.setFullName(candidateCreateDTO.getFullName());
        candidate.setEmail(candidateCreateDTO.getEmail());
        candidate.setAge(candidateCreateDTO.getAge());
        candidate.setYearsOfExperience(candidateCreateDTO.getYearsOfExperience());

        candidateRepo.save(candidate);
        return candidate;
    }
}
