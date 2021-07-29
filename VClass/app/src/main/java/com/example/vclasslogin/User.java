package com.example.vclasslogin;

// User class is parent class of Student and Teacher
public class User {
    int id;
    String name,mobileNo,email,username,password,userType;

//CONSTRUCTORS

    public User(){

    }

    public User(int id, String name, String mobileNo, String email, String username, String password, String userType) {
        this.id=id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email  = email;
        this.username = username;
        this.password = password;
        this.userType = userType;

    }

//GETTERS

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


//SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


// OTHER METHODS

    public String toString(){
        return name+" "+mobileNo+" "+email+" "+username+" "+password+" "+userType;
    }


}