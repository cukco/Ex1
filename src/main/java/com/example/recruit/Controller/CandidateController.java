package com.example.recruit.Controller;

import com.example.recruit.DTO.CandidateCreateDTO;
import com.example.recruit.DTO.CandidateUpdateDTO;
import com.example.recruit.Models.Candidate;
import com.example.recruit.Service.CandidateService;
import com.example.recruit.Wrapper.ApiResponse;
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
    public ApiResponse<?> save(@Valid @RequestBody CandidateCreateDTO candidateCreateDTO){
        Candidate candidate=candidateService.save(candidateCreateDTO);
        return new ApiResponse<>("SUCCESS",candidate,HttpStatus.CREATED+"");
    }

    @PutMapping(value = "/update/{id}",consumes = {"multipart/form-data"})
    public ApiResponse<?> update(@PathVariable int id, @Valid @ModelAttribute CandidateUpdateDTO dto){
        Candidate candidate=candidateService.update(id,dto.getAddress(),dto.getBio());
        return new ApiResponse<>("SUCCESS",candidate,HttpStatus.OK+"");
    }

}
