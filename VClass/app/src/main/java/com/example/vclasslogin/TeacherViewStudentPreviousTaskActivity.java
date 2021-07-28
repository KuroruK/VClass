package com.example.vclasslogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class TeacherViewStudentPreviousTaskActivity extends AppCompatActivity {
    String courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date, id;
    Button save, back;
    int count = 0;  // add a number at the end of attached file names by increasing 1-by-1
    String userName;
    TextView title, studentID, studentName;
    EditText feedback, oMarks;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView rv1;
    MyRvTaskListAdapter adapter1;
    ArrayList<ClassTask> tasks1 = new ArrayList<ClassTask>();
    ArrayList<String> ids1 = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_teacher);

        // actionbar - back button and title
        getSupportActionBar().setTitle(submittedBy);
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
        title = findViewById(R.id.task_v_teacher_title);
        studentID = findViewById(R.id.task_v_std_id2);
        studentName = findViewById(R.id.task_v_std_name2);
        oMarks = findViewById(R.id.task_v_teacher_om3);
        feedback = findViewById(R.id.task_v_teacher_db3);
        save = findViewById(R.id.t_vut_e_btn);
        back = findViewById(R.id.t_vut_b_btn);
        rv1 = findViewById(R.id.rvList_files);

        // putting text data in layout fields
        title.setText(taskTitle);
        studentID.setText(id);
        studentName.setText(submittedBy);

        //  getting particular task details from firebase
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);
                if (tem.getTaskTitle().equals(taskTitle) && tem.getSubmittedBy().equals(submittedBy)) {
                    if (tem.getStatus().equals("feedback")) {
                        oMarks.setText(tem.obtainedMarks);
                        feedback.setText(tem.file);
                    } else {
                        ids1.add(snapshot.getKey());
                        count++;
                        tem.taskTitle = taskTitle + "_" + submittedBy + "_" + count;
                        tasks1.add(
                                tem
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

        // setting layout manager and adapter for rv
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv1.setLayoutManager(manager);
        Log.v("tagid", "0");
        adapter1 = new MyRvTaskListAdapter(getApplicationContext(), tasks1, "submittedStudentIndividualFiles", ids1, userName, null);
        rv1.setAdapter(adapter1);

        // button to go back to the last opened activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherViewStudentPreviousTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "teacher");
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });

        // button to save modification (obtained marks and feedback)
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.push().setValue(new ClassTask(
                        title.getText().toString(),
                        courseName,
                        description,
                        deadline,
                        "student",
                        oMarks.getText().toString(),
                        totalMarks,
                        studentName.getText().toString(),
                        feedback.getText().toString(),
                        LocalDateTime.now().toString(),
                        "feedback"  // will show this is the task which has obtained marks and feedback.
                ));
                finish();
            }
        });
    }

    // method use to go to previous activity when back button is pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), TeacherViewPreviousTaskActivity.class);

        intent.putExtra("userName", userName);
        intent.putExtra("courseName", courseName);
        intent.putExtra("taskTitle", taskTitle);
        intent.putExtra("description", description);
        intent.putExtra("deadline", deadline);
        intent.putExtra("obtainedMarks", obtainedMarks);
        intent.putExtra("totalMarks", totalMarks);
        intent.putExtra("submittedBy", submittedBy);
        intent.putExtra("file", file);
        intent.putExtra("date", date);
        intent.putExtra("id", id);

        startActivityForResult(intent, 0);
        return true;
    }
}
