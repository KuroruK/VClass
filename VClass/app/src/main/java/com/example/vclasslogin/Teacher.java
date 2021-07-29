package com.example.vclasslogin;

public class Teacher extends User{
    String specialization;

// CONSTRUCTORS

    public Teacher(int id, String name, String mobileNo, String email, String username, String password, String specialization) {
        super(id,name,mobileNo,email,username,password,"Teacher");
        this.specialization = specialization;
    }

    public Teacher(){

    }


// GETTERS

    public String getSpecialization() {
        return specialization;
    }


// SETTERS

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


// OTHER FUNCTIONS

    public String toString(){
        return name+" "+mobileNo+" "+email+" "+username+" "+password+" "+specialization;
    }



}