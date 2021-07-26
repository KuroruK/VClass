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

    ArrayList<String> basicFields;
    GridAdapterStudent adapter;
    GridView gridView;
    public static databaseHandler handler;

    // actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mai_menu, menu);
        handler = new databaseHandler(this);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        getSupportActionBar().setTitle("Sign out");

        basicFields = new ArrayList<>();

        getSupportActionBar().show();
        gridView = (GridView) findViewById(R.id.grid);
        basicFields.add("Timetable");
        basicFields.add("Schedule");
        basicFields.add("Classes");
        basicFields.add("Chat");

        //  basicFields.add("Manage Courses");
        String name = getIntent().getStringExtra("student-username");
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText("Student");

        adapter = new GridAdapterStudent(this, basicFields, name);
        gridView.setAdapter(adapter);
    }

    // actionbar menu - logout function
    public void logout(MenuItem item) {
        Intent launchIntent = new Intent(this, LoginActivity.class);
        startActivity(launchIntent);
    }
}