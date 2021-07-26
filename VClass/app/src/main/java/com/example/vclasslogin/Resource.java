package com.example.vclasslogin;

public class Resource {
    String course;
    String title;
    String fileType;
    String resource;
    String time;
    String teacherID;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public Resource(){

    }
    public Resource(String course, String title, String fileType, String resource, String time, String teacherID) {
        this.course = course;
        this.title = title;
        this.fileType = fileType;
        this.resource = resource;
        this.time = time;
        this.teacherID = teacherID;
    }
}
