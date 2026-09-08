package com.example.recruit.Controller;

import com.example.recruit.DTO.CandidateCreateDTO;
import com.example.recruit.Models.Candidate;
import com.example.recruit.Service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CandidateCreateDTO candidateCreateDTO){
        Candidate candidate=candidateService.save(candidateCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(candidate);
    }
}
