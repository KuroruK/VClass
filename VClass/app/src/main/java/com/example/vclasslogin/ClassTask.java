package com.example.vclasslogin;

public class ClassTask {
    String taskTitle;
    String courseName;
    String description;
    String deadline;
    String userType;
    String obtainedMarks;
    String totalMarks;
    String submittedBy;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String file;
    String date;
    String status;

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ClassTask() {
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

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

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }


    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

}
