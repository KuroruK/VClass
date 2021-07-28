package com.example.vclasslogin;

import androidx.annotation.NonNull;

public class Student extends User{
    String Class,section;


// CONSTRUCTORS

    public Student(int id, String name, String mobileNo, String email, String username, String password, String classes, String section) {
        super(id,name,mobileNo,email,username,password,"Student");
        this.Class = classes;
        this.section=section;

    }


// GETTERS

    @NonNull
    public String getClassName() {
        return Class;
    }

    public String getSection() {
        return section;
    }


// SETTERS

    public void setClassName(String aClass) {
        Class = aClass;
    }

        public void setSection(String section) {
            this.section = section;
        }


// OTHER FUNCTIONS

    public String toString(){
        return name+" "+mobileNo+" "+email+" "+username+" "+password+" "+Class+" "+section;
    }


}