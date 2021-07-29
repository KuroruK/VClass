package com.example.vclasslogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminView extends AppCompatActivity {

    ArrayList<String> basicFields; // to store main menu item names
    AdminGridAdapter adapter;   // to set adapter for main menu
    GridView gridView;  // grid view for main menu items

    // actionbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mai_menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        // actionbar - setting title
        getSupportActionBar().setTitle("Main Menu");

        gridView = (GridView) findViewById(R.id.grid);

        // setting profile name as Admin
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText("Admin");

        basicFields = new ArrayList<>();
        // adding main menu item names into the list
        basicFields.add("Manage Teachers");
        basicFields.add("Manage Students");
        basicFields.add("Manage Timetable");
        basicFields.add("Manage Courses");

        // setting adapter for admin menu by passing this activity, menu items names and admin username
        adapter = new AdminGridAdapter(this, basicFields, getIntent().getStringExtra("admin-username"));
        gridView.setAdapter(adapter);
    }

    // actionbar menu - logout function
    public void logout(MenuItem item) {
        Intent launchIntent = new Intent(this, LoginActivity.class);
        startActivity(launchIntent);
    }
}