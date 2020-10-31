package com.example.vclasslogin;


import androidx.appcompat.app.AppCompatActivity;

public class Courses extends AppCompatActivity {
    int id;
    String courseCode,courseName,creditHrs,description;


    public Courses(int id, String courseCode, String courseName, String creditHrs, String description) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.creditHrs = creditHrs;
        this.description = description;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCreditHrs() {
        return creditHrs;
    }

    public void setCreditHrs(String creditHrs) {
        this.creditHrs = creditHrs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
