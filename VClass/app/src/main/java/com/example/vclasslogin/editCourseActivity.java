package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// this is used by admin to edit course details
public class EditCourseActivity extends AppCompatActivity {

    EditText courseName,courseCode,creditHrs,description;
    int id;
    Button save;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        getSupportActionBar().setTitle("Go Back");

        courseName = (EditText) findViewById(R.id.edit_crs_name2);
        courseCode = (EditText) findViewById(R.id.edit_crs_id2);
        creditHrs = (EditText) findViewById(R.id.edit_crs_crd_hrs2);
        description = (EditText) findViewById(R.id.edit_crs_desc2);
        DB = new DBHelper(this);
        init();
        save = (Button) findViewById(R.id.btn_save_edit_crs);

        //listener for Save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = courseName.getText().toString();
                String cCode=courseCode.getText().toString();
                String cHrs=creditHrs.getText().toString();
                String cDes=description.getText().toString();


                // following code makes sure all fields are filled correctly and updates changed entries in database
                if (cName.isEmpty() || cCode.isEmpty() || cHrs.isEmpty() || cDes.isEmpty())
                    Toast.makeText(EditCourseActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                    Boolean checkCName = false,checkCCode = false;
                    if(!(cName.equals(getIntent().getStringExtra("courseName")))){
                        DB.doesCourseNameExist(cName);
                    }
                    if(!(cCode.equals(getIntent().getStringExtra("courseCode")))){
                        checkCCode = DB.doesCourseCodeExist(cCode);
                    }

                    if (!(checkCName ||  checkCCode)) {
                        Boolean update = DB.updateCourseData(id,cCode,cName,cHrs,cDes);
                        if (update) {
                            DB.updateWhiteboardForCourse(getIntent().getStringExtra("courseName"), cName);
                            Toast.makeText(EditCourseActivity.this, "Course Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ListCourseActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(EditCourseActivity.this, "Updation failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (checkCCode)
                            Toast.makeText(EditCourseActivity.this, "Updation failed!\nCourse Code already exists!\n", Toast.LENGTH_SHORT).show();
                        else if (checkCName)
                            Toast.makeText(EditCourseActivity.this, "Updation failed!\nCourse Name already exists!\n", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    // the following method places information regarding courses in editable text boxes
    void init(){
        courseName.setText(getIntent().getStringExtra("courseName"));
        courseCode.setText(getIntent().getStringExtra("courseCode"));
        creditHrs.setText(getIntent().getStringExtra("creditHrs"));
        description.setText(getIntent().getStringExtra("description"));
        id=DB.getCourseID(getIntent().getStringExtra("courseCode"));
        return;
    }

}