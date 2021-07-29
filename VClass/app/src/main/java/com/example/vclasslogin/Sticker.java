// CODE REFERENCE : https://github.com/tlaabs/TimetableView
// Code modified to fit our requirements
package com.example.vclasslogin;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Sticker implements Serializable {
    //this class is used for the timetable component
    //each entry is stored as a sticker, i.e sticker holds the schedule which in turn holds the details such as course, timeslot, etc.

    private ArrayList<TextView> view;
    private ArrayList<Schedule> schedules;

    public Sticker() {
        this.view = new ArrayList<TextView>();
        this.schedules = new ArrayList<Schedule>();
    }

    public void addTextView(TextView v){
        view.add(v);
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public ArrayList<TextView> getView() {
        return view;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}
