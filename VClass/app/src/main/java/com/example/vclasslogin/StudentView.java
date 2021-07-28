package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StudentView extends AppCompatActivity {

    ArrayList<String> basicFields; // to store main menu item names
    StudentGridAdapter adapter;   // to set adapter for main menu
    GridView gridView;  // grid view for main menu items

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

        // action bar - setting title
        getSupportActionBar().setTitle("Main Menu");

        gridView = (GridView) findViewById(R.id.grid);

        // setting profile name as Student
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText("Student");

        basicFields = new ArrayList<>();
        // adding main menu item names into the list
        basicFields.add("Timetable");
        basicFields.add("Schedule");
        basicFields.add("Classes");
        basicFields.add("Chat");

        // setting adapter for student menu by passing this activity, menu items names and student username
        adapter = new StudentGridAdapter(this, basicFields, getIntent().getStringExtra("student-username"));
        gridView.setAdapter(adapter);
    }

    // actionbar menu - logout function
    public void logout(MenuItem item) {
        Intent launchIntent = new Intent(this, LoginActivity.class);
        startActivity(launchIntent);
    }
}