package com.example.vclasslogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TeacherViewUploadedTaskActivity extends AppCompatActivity {
    TextView title, desc, due, marks;
    Button btnEdit, btnBack;
    RecyclerView rv;

    FirebaseDatabase database;
    DatabaseReference reference;
    MyRvTaskListAdapter adapter;
    ArrayList<ClassTask> tasks = new ArrayList<ClassTask>();
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> files = new ArrayList<>();

    String courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date, id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view_uploaded_task);


        // back button - action bar
        getSupportActionBar().setTitle("View Task");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        courseName = getIntent().getStringExtra("courseName");
        taskTitle = getIntent().getStringExtra("taskTitle");
        description = getIntent().getStringExtra("description");
        deadline = getIntent().getStringExtra("deadline");
        obtainedMarks = getIntent().getStringExtra("obtainedMarks");
        totalMarks = getIntent().getStringExtra("totalMarks");
        submittedBy = getIntent().getStringExtra("submittedBy");
        file = getIntent().getStringExtra("file");
        date = getIntent().getStringExtra("date");
        id = getIntent().getStringExtra("id");

        title = findViewById(R.id.t_vut_title);
        desc = findViewById(R.id.t_vut_desc2);
        due = findViewById(R.id.t_vut_due2);
        marks = findViewById(R.id.t_vut_marks2);
        btnEdit = findViewById(R.id.t_vut_e_btn);
        btnBack = findViewById(R.id.t_vut_b_btn);
        rv = findViewById(R.id.t_vut_rvList_files);
        Log.v("check file", file);

        //no of files in task
        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
                Log.v("check updatingNoOfIfles", "yup");
            }
        }
        Log.v("check nooffiles", Integer.toString(noOfFiles));
        String getFiles = null;
        int index = 0;
        ids.add(id);
        if (noOfFiles > 0) {
            getFiles = file.substring(2, file.length());
        }
        String temp = null;
        for (int i = 0; i < noOfFiles; i++) {

            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            Log.v("check temp", temp);
            ids.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
            //      Log.v("check getFiles",getFiles);
        }
        for (int i = 0; i < ids.size(); i++) {
            Log.v("check ids", ids.get(i));
        }

        for (int i = 0; i < noOfFiles; i++) {
            tasks.add(new ClassTask(
                    "File No. " + Integer.toString(i + 1),
                    courseName,
                    description,
                    deadline,
                    "teacher",
                    "-",
                    totalMarks,
                    submittedBy,
                    file,
                    LocalDateTime.now().toString(),
                    "ongoing"
            ));
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        Log.v("tagid", "0");
        adapter = new MyRvTaskListAdapter(getApplicationContext(), tasks, "ongoingTeacherView", ids, submittedBy, null);
        rv.setAdapter(adapter);

        title.setText(taskTitle);
        desc.setText(description);
        due.setText(deadline);
        marks.setText(totalMarks);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherViewUploadedTaskActivity.this, EditTaskActivity.class);
                intent.putExtra("taskTitle", taskTitle);
                intent.putExtra("courseName", courseName);
                intent.putExtra("description", description);
                intent.putExtra("deadline", deadline);
                intent.putExtra("obtainedMarks", obtainedMarks);
                intent.putExtra("totalMarks", totalMarks);
                intent.putExtra("submittedBy", submittedBy);
                intent.putExtra("file", file);
                intent.putExtra("date", date);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherViewUploadedTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("userType", "teacher");
                intent.putExtra("username", submittedBy);
                intent.putExtra("fragment", "ongoing");
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TaskActivity.class);
        myIntent.putExtra("username", submittedBy);
        myIntent.putExtra("courseName", courseName);
        myIntent.putExtra("userType", "teacher");
        myIntent.putExtra("fragment", "ongoing");
        startActivityForResult(myIntent, 0);
        return true;
    }
}