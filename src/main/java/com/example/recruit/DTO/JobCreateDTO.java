package com.example.recruit.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JobCreateDTO {
    @NotBlank
    private String title;

    @NotNull
    @Valid
    private CompanyDTO company;



    public JobCreateDTO() {}
    public JobCreateDTO(String title, CompanyDTO company) {
        this.title = title;
        this.company = company;
    }



    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public CompanyDTO getCompany() {return company;}
    public void setCompany(CompanyDTO company) {this.company = company;}

}
