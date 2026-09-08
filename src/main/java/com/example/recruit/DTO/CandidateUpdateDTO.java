package com.example.recruit.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CandidateUpdateDTO {
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank
    @Size(max = 200,message = "Tối đa 200 ký tự")
    private String bio;

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getBio() {return bio;}
    public void setBio(String bio) {this.bio = bio;}
}
