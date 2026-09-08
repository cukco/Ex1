package com.example.recruit.Controller;

import com.example.recruit.DTO.JobCreateDTO;
import com.example.recruit.Wrapper.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @PostMapping
    public ApiResponse<JobCreateDTO> createJob(@Valid @RequestBody JobCreateDTO dto) {
        return new ApiResponse<>("SUCCESS",dto, HttpStatus.OK+"");
    }
}
