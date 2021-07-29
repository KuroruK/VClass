package com.example.vclasslogin;

// objects of this class are stored in firebase.
public class Resource {
    String course;
    String title;
    String fileType;
    String resource;
    String time;
    String teacherID;


    // CONSTRUCTORS

    // default constructor that takes no values - needed because of firebase
    public Resource(){

    }

    // parameterized constructor
    public Resource(String course, String title, String fileType, String resource, String time, String teacherID) {
        this.course = course;
        this.title = title;
        this.fileType = fileType;
        this.resource = resource;
        this.time = time;
        this.teacherID = teacherID;
    }

    // GETTERS
    public String getCourse() {
        return course;
    }

    public String getTitle() {
        return title;
    }

    public String getFileType() {
        return fileType;
    }

    public String getResource() {
        return resource;
    }

    public String getTime() {
        return time;
    }

    public String getTeacherID() {
        return teacherID;
    }

    // SETTERS
    public void setCourse(String course) {
        this.course = course;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

}
