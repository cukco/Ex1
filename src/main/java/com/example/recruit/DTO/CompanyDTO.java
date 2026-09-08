package com.example.recruit.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CompanyDTO {
    @NotBlank
    private String name;

    @Size(min=10, max=13)
    private String taxCode;

    public CompanyDTO() {}

    public CompanyDTO(String name, String taxCode) {
        this.name = name;
        this.taxCode = taxCode;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getTaxCode() {return taxCode;}
    public void setTaxCode(String taxCode) {this.taxCode = taxCode;}


}
