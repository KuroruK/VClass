package com.example.vclasslogin;

public class Teacher {
    String name,mobileNo,email,username,password,specialization;
    public Teacher(String name, String mobileNo, String email, String username,String password,String specialization) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.email  = email;
        this.username = username;
        this.password = password;
        this.specialization = specialization;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
