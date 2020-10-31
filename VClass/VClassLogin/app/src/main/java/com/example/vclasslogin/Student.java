package com.example.vclasslogin;

import androidx.annotation.NonNull;

public class Student {
    int id;
    String name,mobileNo,email,username,password,Class,section;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getClassName() {
        return Class;
    }

    public void setClassName(String aClass) {
        Class = aClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Student(int id, String name, String mobileNo, String email, String username, String password, String classes, String section) {
        this.id=id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email  = email;
        this.username = username;
        this.password = password;
        this.Class = classes;
        this.section=section;

    }

    public String getName() {
        return name;
    }

    public String toString(){
        return name+" "+mobileNo+" "+email+" "+username+" "+password+" "+Class+" "+section;
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


}
