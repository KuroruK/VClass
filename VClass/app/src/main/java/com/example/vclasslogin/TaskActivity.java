package com.example.vclasslogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity {
    String courseName, userType, username, fragment;
    ArrayList<String> studentTaskTitles = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        courseName = getIntent().getStringExtra("courseName");
        userType = getIntent().getStringExtra("userType");
        username = getIntent().getStringExtra("username");
        fragment = getIntent().getStringExtra("fragment");

        back = findViewById(R.id.back_ta);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent;
                if (userType.equalsIgnoreCase("student"))
                     myIntent = new Intent(getApplicationContext(), LiveStudentClassActivity.class);
                else
                    myIntent = new Intent(getApplicationContext(), LiveTeacherClassActivity.class);

                myIntent.putExtra("username", username);
                myIntent.putExtra("courseName", courseName);
                startActivityForResult(myIntent, 0);
            }
        });

        if (userType.equals("student")) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("Tasks");
            //    if(userType.equals("teacher")){
            reference.addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ClassTask tem = snapshot.getValue(ClassTask.class);

                    if (tem.getCourseName().equals(courseName) && tem.getSubmittedBy().equals(username)) {
                        Log.v("tagidUser", username);
                        studentTaskTitles.add(
                                Objects.requireNonNull(snapshot.getValue(ClassTask.class)).getTaskTitle()
                        );
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                        previousChildName) {
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
                        previousChildName) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            });
            Log.v("tagid1", "student task titles length " + Integer.toString(studentTaskTitles.size()));
        }

        com.example.vclasslogin.ui.main.SectionsPagerAdapter sectionsPagerAdapter = new com.example.vclasslogin.ui.main.SectionsPagerAdapter(this, getSupportFragmentManager(), userType, courseName, username, studentTaskTitles);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        if (fragment.equals("ongoing"))
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(1);

        TabLayout tabs = findViewById(R.id.tabs);


        Log.v("tabid", "0");
        tabs.setupWithViewPager(viewPager);
    }

}