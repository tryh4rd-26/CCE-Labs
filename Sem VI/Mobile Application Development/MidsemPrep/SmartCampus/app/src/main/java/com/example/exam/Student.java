package com.example.exam;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable {
    private String name;
    private String regNo;
    private String phone;
    private String email;
    private String gender;
    private List<String> skills;
    private boolean isHostelResident;
    private boolean isScholarshipEligible;
    private String department;
    private String dob;
    private String preferredLabTime;

    public Student(String name, String regNo, String phone, String email, String gender, List<String> skills, boolean isHostelResident, boolean isScholarshipEligible, String department, String dob, String preferredLabTime) {
        this.name = name;
        this.regNo = regNo;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.skills = skills;
        this.isHostelResident = isHostelResident;
        this.isScholarshipEligible = isScholarshipEligible;
        this.department = department;
        this.dob = dob;
        this.preferredLabTime = preferredLabTime;
    }

    public String getName() { return name; }
    public String getRegNo() { return regNo; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public List<String> getSkills() { return skills; }
    public boolean isHostelResident() { return isHostelResident; }
    public boolean isScholarshipEligible() { return isScholarshipEligible; }
    public String getDepartment() { return department; }
    public String getDob() { return dob; }
    public String getPreferredLabTime() { return preferredLabTime; }
    
    @Override
    public String toString() {
        return name + " (" + regNo + ") - " + department;
    }
}
