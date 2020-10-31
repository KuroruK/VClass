package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import joinery.DataFrame;

public class view_schedule_activity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    static ArrayList<Sticker> table;
    private ScheduleView schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule);

        getSupportActionBar().setTitle("Schedule");
        init();
    }

    private void init(){
        this.context = this;
        schedule = findViewById(R.id.schedule);
        schedule.setHeaderHighlight(2);
        initView();
        setSchedule(getIntent().getStringExtra("type"),getIntent().getStringExtra("name"));
    }

    public void setSchedule(String type,String username){
        DBHelper db=new DBHelper(context);
        ArrayList<Schedule> arr=new ArrayList<Schedule>();
        if(type.equals("teacher"))
            arr=db.getTeacherSchedules(username);
        else
            arr=db.getStudentSchedules(username);

        for(Schedule s:arr){
            ArrayList<Schedule> temp=new ArrayList<Schedule>();
            temp.add(s);
            schedule.add(temp);
        }
    }

    private void initView(){
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == timeTableEditActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    schedule.add(item);
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == timeTableEditActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    schedule.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == timeTableEditActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    schedule.remove(idx);
                }
                break;
        }
    }

}
