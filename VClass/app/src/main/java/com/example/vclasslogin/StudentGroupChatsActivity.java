package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// this activity shows list of student group chats
public class StudentGroupChatsActivity extends AppCompatActivity {
    RecyclerView rv;
    MyRvListGroupChatsAdapter adapter;
    ArrayList<String> groupLists = new ArrayList<>();
    String username;
    int studentID;
    ArrayList<String> courses;
    ArrayList<Integer> coursesIDs;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_group_chats);
        groupLists = new ArrayList<>();

        // back button - action bar
        getSupportActionBar().setTitle("Chat App");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("name");
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        rv = findViewById(R.id.ac_rcv);

        studentID = dbHelper.getStudentID(username);
        coursesIDs = dbHelper.getStudentCourseIDs(studentID);

        // following loop adds student courses to groupList
        for (int i = 0; i < coursesIDs.size(); ++i) {
            groupLists.add(dbHelper.getCourseName(coursesIDs.get(i)));
        }


        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new MyRvListGroupChatsAdapter(this, groupLists, getIntent().getStringExtra("name"), "student");
        rv.setAdapter(adapter);


    }


    // method used to go to previous activity when back button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), StudentView.class);
        myIntent.putExtra("student-username", username);
        startActivityForResult(myIntent, 0);
        return true;
    }
}