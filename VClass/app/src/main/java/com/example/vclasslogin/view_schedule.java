package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import joinery.DataFrame;

public class view_schedule extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    static ArrayList<Sticker> table;
    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;
    private TimetableView timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timetable);

        init();
    }

    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
        saveBtn = findViewById(R.id.save_btn);
        loadBtn = findViewById(R.id.load_btn);



        timetable = findViewById(R.id.timetable);
        timetable.setHeaderHighlight(2);

        //--------loads from db
        DBHelper obj=new DBHelper(context);
    }

    private void initView(){
        addBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, timeTableEditActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                Intent i = new Intent(this,timeTableEditActivity.class);
                i.putExtra("mode",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == timeTableEditActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == timeTableEditActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == timeTableEditActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }

    /** save timetableView's data to SharedPreferences in json format */
    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        Toast.makeText(this,"saved!", Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
/*
        ArrayList<Schedule> temp=new ArrayList<Schedule>();
        Schedule a=new Schedule();
        a.setClassPlace();
        a.setClassTitle("test class 1");
        a.setDay(2);
        a.setProfessorName();
        a.setStartTime(new Time(10,10));
        a.setEndTime(new Time(13,10));
        Log.v("testing","before addtemp");
        temp.add(a);

        timetable.add(temp);
*/

        String[] days={"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
        int day=0;
        DBHelper obj=new DBHelper(context);

        obj.deleteTimeSlotData();
        csvLoader c=new csvLoader();

        for(int i=0;i<2;i++) {
            ArrayList<timeSlot> s = c.getTable(context, days[i]+".csv");
            List<timeSlot> t = new ArrayList<timeSlot>(s);
            Collections.sort(t);

            int lH = 0, lM = 0;
            int k = -1;
            for (timeSlot slot : t) {
                ArrayList<Schedule> temp = new ArrayList<Schedule>();
                //if(lH==slot.getStartTime().getHour() && lM==slot.getStartTime().getMinute())
                k += 1;
                if (k >= 29)
                    k = 0;

                Schedule a = new Schedule();
                a.setClassPlace();
                a.setClassTitle(slot.name);
                a.setDay(k);
                a.setProfessorName();
                a.setStartTime(slot.getStartTime());
//            a.setStartTime(new Time(10,10));
                a.setEndTime(slot.getEndTime());
//            a.setEndTime(new Time(13,10));

                temp.add(a);
                obj.insertTimeSlot(a, i);
                //timetable.add(temp);
            }
        }

        ArrayList<Schedule> sch=obj.getTimeSlots(0);
        if(sch.size()!=0)
            for(Schedule sched:sch){
                ArrayList<Schedule> temp=new ArrayList<Schedule>();
                temp.add(sched);
                timetable.add(temp);
            }


        Log.v("hello there",Integer.toString(sch.size()));


        Toast.makeText(this,"loaded!", Toast.LENGTH_SHORT).show();
    }

}
