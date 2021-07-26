package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherGroupChatsActivity extends AppCompatActivity {
    //    AutoCompleteAdapter autoCompleteAdapter;
    RecyclerView rv;
    TextView add;
    MyRvListGroupChatsAdapter adapter;
    ArrayList<String> groupLists = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    CircleImageView viewProfilePhoto;
    ImageView menu;
    AutoCompleteTextView search;


    String username;
    int teacherID;
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

        teacherID = dbHelper.getTeacherID(username);
        coursesIDs = dbHelper.getTeacherCourseIDs(teacherID);
        for (int i = 0; i < coursesIDs.size(); ++i) {
            groupLists.add(dbHelper.getCourseName(coursesIDs.get(i)));
            Log.v("teacher_", dbHelper.getCourseName(coursesIDs.get(i)));
        }


        // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new MyRvListGroupChatsAdapter(this, groupLists, getIntent().getStringExtra("name"), "teacher");
        rv.setAdapter(adapter);
    }

    void searchArray() {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TeacherView.class);
        myIntent.putExtra("teacher-username", username);
        startActivityForResult(myIntent, 0);
        return true;
    }

}