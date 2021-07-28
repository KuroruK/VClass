// CODE REFERENCE : https://github.com/tlaabs/TimetableView
// Code modified to fit our requirements

package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlinx.coroutines.ObsoleteCoroutinesApi;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    static ArrayList<Sticker> table;
    //private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;
    private Spinner daySpinner;
    private TimetableView timetable;
    String userType;

    private int weekday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timetable);

        //back button action bar
        getSupportActionBar().setTitle("Timetable");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userType = getIntent().getStringExtra("type");

        if (userType == null)
            userType = "admin";
        init();
    }

    //initial function to setup timetable view page
    private void init() {
        this.context = this;

        //options buttons
        clearBtn = findViewById(R.id.clear_btn);
        saveBtn = findViewById(R.id.save_btn);
        loadBtn = findViewById(R.id.load_btn);

        //dropdown menu to select weekday
        daySpinner = (Spinner) findViewById(R.id.timtable_day_spinner);

        weekday = 0;

        //only load option buttons if user type is admin
        if (!(userType.equals("admin"))) {
            clearBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
            loadBtn.setVisibility(View.GONE);
        }

        //find timetable values
        timetable = findViewById(R.id.timetable);
        timetable.setHeaderHighlight(2);

        //loads from db
        DBHelper obj = new DBHelper(context);
        ArrayList<Schedule> sch = obj.getTimeSlots(0);//default = 0 i.e. monday
        if (sch.size() != 0)
            for (Schedule sched : sch) {
                ArrayList<Schedule> temp = new ArrayList<Schedule>();
                temp.add(sched);
                timetable.add(temp);
            }

        initView();

        //setup the spinner/dropdown menu
        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        daySpinner.setAdapter(adapter);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadTableForDay(i);
                weekday = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadTableForDay(0);
                weekday = 0;
            }
        });
    }

    //load listeners for buttons
    private void initView() {
        clearBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
    }

    //managing different button selections
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_btn:
                timetable.removeAll();
                break;
            case R.id.save_btn:
                saveByPreference(timetable.createSaveData());
                break;
            case R.id.load_btn:
                loadSavedData();
                break;
        }
    }


    /**
     * save timetableView's data to SharedPreferences in json format
     */
    private void saveByPreference(String data) {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo", data);
        editor.commit();
        Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show();
    }

    /**
     * get json data from SharedPreferences and then restore the timetable
     */
    private void loadSavedData() {
        timetable.removeAll();

        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        int day = 0;
        DBHelper obj = new DBHelper(context);

        //delete currently loaded data for timetable
        obj.deleteTimeSlotData(weekday);
        CsvLoader c = new CsvLoader();

        //load csv files
        ArrayList<TimeSlot> s = c.getTable(context, days[weekday] + ".csv");
        List<TimeSlot> t = new ArrayList<TimeSlot>(s);
        Collections.sort(t);

        //generate new schedule
        int lH = 0, lM = 0;
        int k = -1;
        for (TimeSlot slot : t) {
            ArrayList<Schedule> temp = new ArrayList<Schedule>();
            k += 1;
            if (k >= 29)
                k = 0;

            Schedule a = new Schedule();
            a.setClassPlace();
            a.setClassTitle(slot.name);
            a.setDay(k);
            a.setProfessorName(" ");
            a.setStartTime(slot.getStartTime());
            a.setEndTime(slot.getEndTime());

            //add schedule to timetable corresponding to column
            temp.add(a);
            obj.insertTimeSlot(a, weekday);
        }

        //load the timetable
        ArrayList<Schedule> sch = obj.getTimeSlots(0);
        if (sch.size() != 0)
            for (Schedule sched : sch) {
                ArrayList<Schedule> temp = new ArrayList<Schedule>();
                temp.add(sched);
                timetable.add(temp);
            }

        Toast.makeText(this, "loaded!", Toast.LENGTH_SHORT).show();
    }

    //loads a specific days timetable
    public void loadTableForDay(int day) {
        timetable.removeAll();
        DBHelper obj = new DBHelper(context);
        //get timeslots list for day
        ArrayList<Schedule> sch = obj.getTimeSlots(day);
        if (sch.size() != 0)
            for (Schedule s : sch) {
                ArrayList<Schedule> temp = new ArrayList<Schedule>();
                temp.add(s);
                //load the timetable with the loaded schedules
                timetable.add(temp);
            }
    }

    //function to manage passed values between activities
    public boolean onOptionsItemSelected(MenuItem item) {
        String userType = getIntent().getStringExtra("type");
        String username = null;
        if (!userType.equals("admin"))
            username = getIntent().getStringExtra("name");

        Intent intent = null;
        if (userType.equals("student")) {
            intent = new Intent(getApplicationContext(), StudentView.class);
            intent.putExtra("student-username", username);
        } else if (userType.equals("teacher")) {
            intent = new Intent(getApplicationContext(), TeacherView.class);
            intent.putExtra("teacher-username", username);
        } else {
            intent = new Intent(getApplicationContext(), AdminView.class);
        }

        startActivityForResult(intent, 0);
        return true;
    }

}
