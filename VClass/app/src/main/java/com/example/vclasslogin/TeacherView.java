package com.example.vclasslogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class TeacherView extends AppCompatActivity {

    ArrayList<String> basicFields;
    gridAdapterTeacher adapter;
    public static ArrayList<String> divisions ;
    public static ArrayList<String> timeSlots;
    GridView gridView;
    public static databaseHandler handler;
    public static Activity activity;
    String name;

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

        getSupportActionBar().setTitle("Sign out");
        name = getIntent().getStringExtra("teacher-username");

        basicFields = new ArrayList<>();
        handler = new databaseHandler(this);
        activity = this;

        getSupportActionBar().show();
        gridView = (GridView)findViewById(R.id.grid);
        basicFields.add("Schedule");
        basicFields.add("Timetable");
        basicFields.add("Classes");
        basicFields.add("Chat");

       // basicFields.add("Manage Tasks");
        adapter = new gridAdapterTeacher(this,basicFields, name);
        gridView.setAdapter(adapter);
    }

    public void loadSettings(MenuItem item) {
        //    Intent launchIntent = new Intent(this,SettingsActivity.class);
        //    startActivity(launchIntent);
    }

    public void loadAbout(MenuItem item) {
        //     Intent launchIntent = new Intent(this,About.class);
        //     startActivity(launchIntent);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                name = data.getStringExtra("name");
            }
        }
    }*/
}