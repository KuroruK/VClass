package com.example.vclasslogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class manageTeacherActivity extends AppCompatActivity {

    ArrayList<String> basicFields;
    GridAdapterAdmin adapter;
    public static ArrayList<String> divisions ;
    public static ArrayList<String> timeSlots;
    GridView gridView;
    public static databaseHandler handler;
    public static Activity activity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mai_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        basicFields = new ArrayList<>();
        handler = new databaseHandler(this);
        activity = this;

        getSupportActionBar().show();
        divisions = new ArrayList();
        divisions.add("Artificial Intelligence");
        divisions.add("Software Engineering");
        divisions.add("Human Computer Interaction");
        divisions.add("Software for Mobile Devices");
        divisions.add("Computer Networks");

        timeSlots=new ArrayList();
        timeSlots.add("8:00 - 9:30");
        timeSlots.add("9:30 - 11:00");
        timeSlots.add("11:00 - 12:30");
        timeSlots.add("12:30 - 14:00");
        timeSlots.add("14:00 - 15:30");
        timeSlots.add("15:30 - 17:00");

        gridView = (GridView)findViewById(R.id.grid);
        basicFields.add("View Teacher");
        basicFields.add("Register Teacher");
        basicFields.add("Edit Teacher");
        basicFields.add("Remove Teacher");
        //adapter = new gridAdapterAdmin(this,basicFields);
        //gridView.setAdapter(adapter);
    }

    public void loadSettings(MenuItem item) {
        //    Intent launchIntent = new Intent(this,SettingsActivity.class);
        //    startActivity(launchIntent);
    }

    public void loadAbout(MenuItem item) {
        //     Intent launchIntent = new Intent(this,About.class);
        //     startActivity(launchIntent);
    }
}