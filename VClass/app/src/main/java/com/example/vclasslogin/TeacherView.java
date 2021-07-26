package com.example.vclasslogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TeacherView extends AppCompatActivity {

    ArrayList<String> basicFields;
    GridAdapterTeacher adapter;
    public static ArrayList<String> divisions;
    public static ArrayList<String> timeSlots;
    GridView gridView;
    public static databaseHandler handler;
    public static Activity activity;
    String name;

    // actionbar menu
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

        getSupportActionBar().setTitle("Main Menu");

        basicFields = new ArrayList<>();

        //handler = new databaseHandler(this);
        //activity = this;

        //getSupportActionBar().show();

        gridView = (GridView) findViewById(R.id.grid);

        name = getIntent().getStringExtra("teacher-username");
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText("Teacher");

        basicFields.add("Timetable");
        basicFields.add("Schedule");
        basicFields.add("Classes");
        basicFields.add("Chat");

        // basicFields.add("Manage Tasks");
        adapter = new GridAdapterTeacher(this, basicFields, name);
        gridView.setAdapter(adapter);
    }

    // actionbar menu - logout function
    public void logout(MenuItem item) {
        Intent launchIntent = new Intent(this, LoginActivity.class);
        startActivity(launchIntent);
    }
}