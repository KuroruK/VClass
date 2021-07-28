package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// this is used by admin to view course details
public class ViewCourseDetailsActivity extends AppCompatActivity {

    TextView courseName, courseCode, creditHrs, description;
    Button edit, delete;
    DBHelper db;
    int courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_view);

        getSupportActionBar().setTitle("Detail View");

        courseName = findViewById(R.id.c_name2);
        courseCode = findViewById(R.id.c_id2);
        creditHrs = findViewById(R.id.c_crd_hrs2);
        description = findViewById(R.id.c_desc2);
        db = new DBHelper(this);
        edit = (Button) findViewById(R.id.c_edit);
        delete = (Button) findViewById(R.id.c_delete);
        init();

        //listener for edit button - opens EditCourseActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EditCourseActivity.class);

                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("courseCode", getIntent().getStringExtra("courseCode"));
                intent.putExtra("creditHrs", getIntent().getStringExtra("creditHrs"));
                intent.putExtra("description", getIntent().getStringExtra("description"));
                intent.putExtra("id", getIntent().getIntExtra("id", -1));
                startActivity(intent);

            }
        });

        // listener for delete button -> deletes course from database
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deletion = db.deleteCourseData(courseID);
                if (deletion == true) {
                    db.removeWhiteboardForCourse(courseName.getText().toString());
                    Toast.makeText(ViewCourseDetailsActivity.this, "Deletion Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), ListCourseActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ViewCourseDetailsActivity.this, "Deletion Failed", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    // the following method places information regarding course in text boxes
    void init() {
        courseName.setText(getIntent().getStringExtra("courseName"));
        courseCode.setText(getIntent().getStringExtra("courseCode"));
        creditHrs.setText(getIntent().getStringExtra("creditHrs"));
        description.setText(getIntent().getStringExtra("description"));
        courseID = db.getCourseID(getIntent().getStringExtra("courseCode"));
        return;
    }
}