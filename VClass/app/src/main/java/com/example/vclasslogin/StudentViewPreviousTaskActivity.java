package com.example.vclasslogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StudentViewPreviousTaskActivity extends AppCompatActivity {
    String courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date, id;
    Button uploadFile;
    int count = 0;    // add a number at the end of attached file names by increasing 1-by-1
    String userName;
    TextView title, desc, dueDate, tMarks, oMarks, feedback;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView rv1, rv2;
    MyRvTaskListAdapter adapter1, adapter2;
    ArrayList<ClassTask> tasks1 = new ArrayList<ClassTask>();
    ArrayList<ClassTask> tasks2 = new ArrayList<ClassTask>();
    ArrayList<String> ids1 = new ArrayList<String>();
    ArrayList<String> ids2 = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_task_student);

        // actionbar - back button and title
        getSupportActionBar().setTitle("View Task");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // getting require data from previous activity
        userName = getIntent().getStringExtra("userName");
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

        // getting firebase instance and reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");

        // getting fields from layout
        oMarks = findViewById(R.id.task_submit_std_ob_marks2);
        feedback = findViewById(R.id.task_submit_std_fb2);
        title = findViewById(R.id.task_submit_std_title);
        desc = findViewById(R.id.task_submit_std_desc2);
        dueDate = findViewById(R.id.task_submit_std_due2);
        tMarks = findViewById(R.id.task_submit_std_marks2);
        uploadFile = findViewById(R.id.task_submit_attach_file);
        rv1 = findViewById(R.id.rvList_files);
        rv2 = findViewById(R.id.rvList_task_files);

        // putting text data in layout fields
        title.setText(taskTitle);
        desc.setText(description);
        dueDate.setText(deadline);
        tMarks.setText(totalMarks);

        //  getting particular task details from firebase
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);
                Log.v("tagid", tem.getTaskTitle() + ";;;" + taskTitle);
                Log.v("tagid", tem.getSubmittedBy() + ":::" + userName);
                if (tem.getTaskTitle().equals(taskTitle) && tem.getSubmittedBy().equals(userName)) {
                    if (tem.status.equals("feedback")) {
                        oMarks.setText(tem.obtainedMarks);
                        feedback.setText(tem.file);
                    } else {
                        ids1.add(snapshot.getKey());
                        count++;
                        tem.taskTitle = taskTitle + "_" + userName + "_" + count;
                        Log.v("tagid", "1");
                        tasks1.add(
                                tem
                                //  snapshot.getValue(ClassTask.class)
                        );
                        adapter1.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // no of files upload by teacher as a helping material for task
        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
            }
        }

        // getting file names attached with a particular task
        String getFiles = null;
        int index = 0;
        ids2.add(id);
        if (noOfFiles > 0) {
            getFiles = file.substring(2);
        }
        String temp;
        for (int i = 0; i < noOfFiles; i++) {

            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            ids2.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
        }

        // getting those files and display them in this activity
        for (int i = 0; i < noOfFiles; i++) {
            tasks2.add(new ClassTask(
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

        // setting layout manager and adapter for rv for viewing attached helping files with the task
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(manager2);
        adapter2 = new MyRvTaskListAdapter(getApplicationContext(), tasks2, "ongoingTeacherView", ids2, submittedBy, null);
        rv2.setAdapter(adapter2);

        // setting layout manager and adapter for rv for viewing student submitted files
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv1.setLayoutManager(manager);
        adapter1 = new MyRvTaskListAdapter(getApplicationContext(), tasks1, "submittedStudentIndividualFiles", ids1, userName, null);
        rv1.setAdapter(adapter1);
    }

    // method use to go to previous activity when back button is pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TaskActivity.class);
        myIntent.putExtra("username", userName);
        myIntent.putExtra("courseName", courseName);
        myIntent.putExtra("userType", "student");
        myIntent.putExtra("fragment", "previous");
        startActivityForResult(myIntent, 0);
        return true;
    }
}
