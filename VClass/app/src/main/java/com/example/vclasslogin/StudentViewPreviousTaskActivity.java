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
    Button uploadFile;//, reSubmit, back;
    String fileType = "";
    Uri selectedImage = null;
    int count = 0;
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
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");

        // action bar
        getSupportActionBar().setTitle("View Task");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        oMarks = findViewById(R.id.task_submit_std_ob_marks2);
        feedback = findViewById(R.id.task_submit_std_fb2);
        title = findViewById(R.id.task_submit_std_title);
        title.setText(taskTitle);
        desc = findViewById(R.id.task_submit_std_desc2);
        desc.setText(description);
        dueDate = findViewById(R.id.task_submit_std_due2);
        dueDate.setText(deadline);
        tMarks = findViewById(R.id.task_submit_std_marks2);
        tMarks.setText(totalMarks);
        uploadFile = findViewById(R.id.task_submit_attach_file);
        //back = findViewById(R.id.task_submit_std_b_btn);

        rv1 = findViewById(R.id.rvList_files);
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


        //teacher files

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
        ids2.add(id);
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
            ids2.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
            //      Log.v("check getFiles",getFiles);
        }
        for (int i = 0; i < ids2.size(); i++) {
            Log.v("check ids", ids2.get(i));
        }

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

        rv2 = findViewById(R.id.rvList_task_files);
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(manager2);
        Log.v("tagid", "0");
        adapter2 = new MyRvTaskListAdapter(getApplicationContext(), tasks2, "ongoingTeacherView", ids2, submittedBy, null);
        rv2.setAdapter(adapter2);

/////////////////////////////////////////////////////////////////////////////////////////

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv1.setLayoutManager(manager);
        Log.v("tagid", "0");
        adapter1 = new MyRvTaskListAdapter(getApplicationContext(), tasks1, "submittedStudentIndividualFiles", ids1, userName, null);
        rv1.setAdapter(adapter1);

        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentViewPreviousTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "student");
                intent.putExtra("username", userName);
                intent.putExtra("fragment", "previous");
                startActivity(intent);
            }
        });*/
    }

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
