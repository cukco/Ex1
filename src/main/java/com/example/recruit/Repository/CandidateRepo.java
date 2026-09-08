package com.example.recruit.Repository;

import com.example.recruit.Models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepo extends JpaRepository<Candidate, Integer> {
}
