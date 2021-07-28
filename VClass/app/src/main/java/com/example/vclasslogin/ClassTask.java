package com.example.vclasslogin;

public class ClassTask {
    // data members
    String taskTitle;
    String courseName;
    String description;
    String deadline;
    String userType;
    String obtainedMarks;
    String totalMarks;
    String submittedBy;
    String file;
    String date;
    String status;

    // default constructor - needed when task is to get from firebase
    public ClassTask() {
    }

    // parameterized constructor
    public ClassTask(String taskTitle, String courseName, String description, String deadline, String userType, String obtainedMarks, String totalMarks, String submittedBy, String file, String date, String status) {
        this.taskTitle = taskTitle;
        this.courseName = courseName;
        this.description = description;
        this.deadline = deadline;
        this.userType = userType;
        this.obtainedMarks = obtainedMarks;
        this.totalMarks = totalMarks;
        this.submittedBy = submittedBy;
        this.file = file;
        this.date = date;
        this.status = status;
    }

    // getters
    public String getStatus() {
        return status;
    }

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public String getDate() {
        return date;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getUserType() {
        return userType;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getFile() {
        return file;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public String getDeadline() {
        return deadline;
    }

    // setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
