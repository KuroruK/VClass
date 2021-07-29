package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    static ArrayList<Sticker> table;
    private ScheduleView schedule;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule);

        // back button - action bar
        getSupportActionBar().setTitle("Schedule");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        this.context = this;
        schedule = findViewById(R.id.schedule);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        schedule.setHeaderHighlight(day);
        //Log.v("day123", String.valueOf(day));

        initView();
        setSchedule(getIntent().getStringExtra("type"), getIntent().getStringExtra("name"));
    }

    public void setSchedule(String type, String username) {
        DBHelper db = new DBHelper(context);
        ArrayList<Schedule> arr = new ArrayList<Schedule>();
        if (type.equals("teacher"))
            arr = db.getTeacherSchedules(username);
        else
            arr = db.getStudentSchedules(username);

        for (Schedule s : arr) {
            ArrayList<Schedule> temp = new ArrayList<Schedule>();
            temp.add(s);
            schedule.add(temp);
        }
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        String userType = getIntent().getStringExtra("type");
        String username = getIntent().getStringExtra("name");

        Intent intent = null;
        if (userType.equals("student")) {
            intent = new Intent(getApplicationContext(), StudentView.class);
            intent.putExtra("student-username", username);
        } else if (userType.equals("teacher")) {
            intent = new Intent(getApplicationContext(), TeacherView.class);
            intent.putExtra("teacher-username", username);
        }

        startActivityForResult(intent, 0);
        return true;
    }
}
