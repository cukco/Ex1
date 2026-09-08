package com.example.recruit.Controller;

import com.example.recruit.DTO.SalaryDTO;
import com.example.recruit.Wrapper.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salaries")
public class SalaryController {
    @PostMapping
    public ApiResponse<SalaryDTO> test(@Valid @RequestBody SalaryDTO dto) {
        if(dto.getSalaryMax()<dto.getSalaryMin()){
            throw new IllegalArgumentException("Lương tối thiểu không được lớn hơn lương tối đa");
        }
        return new ApiResponse<>("SUCCESS",dto, HttpStatus.OK+"");
    }
}
