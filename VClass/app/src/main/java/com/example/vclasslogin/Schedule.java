// CODE REFERENCE : https://github.com/tlaabs/TimetableView
// Code modified to fit our requirements

package com.example.vclasslogin;

import java.io.Serializable;

public class Schedule implements Serializable {
    //class to store a specific entry on the timetable

    static final int MON = 0;
    static final int TUE = 1;
    static final int WED = 2;
    static final int THU = 3;
    static final int FRI = 4;
    static final int SAT = 5;
    static final int SUN = 6;

    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private Time startTime;
    private Time endTime;

    //constructor
    public Schedule() {
        this.startTime = new Time();
        this.endTime = new Time();
    }

    //getter functions
    public String getProfessorName() {
        return professorName;
    }
    public String getClassTitle() {
        return classTitle;
    }
    public Time getStartTime() {
        return startTime;
    }
    public String getClassPlace() {
        return classPlace;
    }
    public int getDay() {
        return day;
    }
    public Time getEndTime() {
        return endTime;
    }

    //setter functions
    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
    public void setProfessorName() {
        this.professorName = "";
    }
    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }
    public void setClassPlace() {
        this.classPlace = "online";
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }




}
