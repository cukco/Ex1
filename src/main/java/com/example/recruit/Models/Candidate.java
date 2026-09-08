package com.example.recruit.Models;


import jakarta.persistence.*;

@Entity
@Table(name="candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100,name ="full_name")
    private String fullName;

    @Column(nullable = false, length = 50,unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false,name = "years_of_experience")
    private int yearsOfExperience;

    public Candidate(int id, String fullName, String email, int age, int yearsOfExperience) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Candidate() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public int getYearsOfExperience() {return yearsOfExperience;}
    public void setYearsOfExperience(int yearsOfExperience) {this.yearsOfExperience = yearsOfExperience;}
}
