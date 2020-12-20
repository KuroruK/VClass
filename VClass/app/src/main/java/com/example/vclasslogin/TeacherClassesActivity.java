package com.example.vclasslogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeacherClassesActivity extends AppCompatActivity {

    String username;
    int teacherID;
    ArrayList<String> courses;
    ArrayList<Integer> coursesIDs;
    DBHelper dbHelper;
    MyRvTeacherCoursesListAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_classes);

        getSupportActionBar().setTitle("View Classes");

        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("name");
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        rv = findViewById(R.id.rvListTeacherClasses);

        teacherID = dbHelper.getTeacherID(username);
        coursesIDs = dbHelper.getTeacherCourseIDs(teacherID);
        for (int i = 0; i < coursesIDs.size(); ++i) {
            courses.add(dbHelper.getCourseName(coursesIDs.get(i)));
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(TeacherClassesActivity.this);
        rv.setLayoutManager(manager);
        adapter = new MyRvTeacherCoursesListAdapter(TeacherClassesActivity.this, courses,username);
        rv.setAdapter(adapter);
    }
}