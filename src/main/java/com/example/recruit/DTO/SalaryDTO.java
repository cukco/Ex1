package com.example.recruit.DTO;

import jakarta.validation.constraints.Min;

public class SalaryDTO {
    @Min(value = 0,message = "Lương phải lớn hơn 0")
    private double salaryMax;

    @Min(value = 0,message = "Lương phải lớn hơn 0")
    private double salaryMin;

    public SalaryDTO(double salaryMax, double salaryMin) {
        this.salaryMax = salaryMax;
        this.salaryMin = salaryMin;
    }
    public void setSalaryMax(double salaryMax) {this.salaryMax = salaryMax;}
    public void setSalaryMin(double salaryMin) {this.salaryMin = salaryMin;}
    public double getSalaryMax() {
        return salaryMax;
    }
    public double getSalaryMin() {
        return salaryMin;
    }
}
