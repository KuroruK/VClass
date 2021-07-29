package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// this activity is used by admin to add new courses
public class RegisterCourseActivity extends AppCompatActivity {

    EditText courseName, courseCode, creditHrs, description;
    Button signUp;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        getSupportActionBar().setTitle("Add Course Details");

        courseName = (EditText) findViewById(R.id.courseName);
        courseCode = (EditText) findViewById(R.id.courseCode);
        creditHrs = (EditText) findViewById(R.id.creditHrs);
        description = (EditText) findViewById(R.id.description);

        signUp = (Button) findViewById(R.id.btnSignUpC);

        DB = new DBHelper(this);
        //listener for Sign-up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = courseName.getText().toString();
                String cCode = courseCode.getText().toString();
                String cHrs = creditHrs.getText().toString();
                String cDes = description.getText().toString();

                // following code makes sure all fields are filled correctly.
                if (cName.isEmpty() || cCode.isEmpty() || cHrs.isEmpty() || cDes.isEmpty())
                    Toast.makeText(RegisterCourseActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                    Boolean checkCName = DB.doesCourseNameExist(cName), checkCCode = DB.doesCourseCodeExist(cCode);
                    if (!(checkCName || checkCCode)) {
                        Boolean insert = DB.insertCourseData(cCode, cName, cHrs, cDes);
                        if (insert) {
                            DB.addWhiteboardForCourse(cName);
                            Toast.makeText(RegisterCourseActivity.this, "Course Registration Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ListCourseActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterCourseActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (checkCCode)
                            Toast.makeText(RegisterCourseActivity.this, "Registration failed!\nCourse Code already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                        if (checkCName)
                            Toast.makeText(RegisterCourseActivity.this, "Registration failed!\nCourse Name already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}