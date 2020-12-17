package com.example.vclasslogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentClassesActivity extends AppCompatActivity {

    String username;
    int studentID;
    ArrayList<String> courses;
    ArrayList<Integer> coursesIDs;
    DBHelper dbHelper;
    MyRvStudentCoursesListAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classes);

        getSupportActionBar().setTitle("View Classes");

        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("name");
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        rv = findViewById(R.id.rvListStudentClasses);

        studentID = dbHelper.getStudentID(username);
        coursesIDs = dbHelper.getStudentCourseIDs(studentID);
        for (int i = 0; i < coursesIDs.size(); ++i) {
            courses.add(dbHelper.getCourseName(coursesIDs.get(i)));
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(StudentClassesActivity.this);
        rv.setLayoutManager(manager);
        adapter = new MyRvStudentCoursesListAdapter(StudentClassesActivity.this, courses);
        rv.setAdapter(adapter);
    }
}