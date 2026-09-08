package com.example.recruit.DTO;


import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;

public class CandidateCreateDTO {
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 5, max = 50, message = "Tên phải từ 5 đến 50 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message="Tuổi không được để trống")
    @Min(value = 18, message = "Không được dưới 18 tuổi")
    private int age;

    @PositiveOrZero(message = "Số năm kinh nghiệm phải >=0")
    private int yearsOfExperience;



    @Pattern(regexp = "^0(3|5|7|8|9)[0-9]{8}$",message = "Số điện thoại không hợp lệ")
    private String phone;


    public CandidateCreateDTO(String fullName, String email, int age, int yearsOfExperience, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.yearsOfExperience = yearsOfExperience;
        this.phone = phone;
    }

    public CandidateCreateDTO() {}

    public int getYearsOfExperience() {return yearsOfExperience;}
    public void setYearsOfExperience(int yearsOfExperience) {this.yearsOfExperience = yearsOfExperience;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
}
