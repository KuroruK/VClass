package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// this is used by admin to edit student details
public class EditStudentActivity extends AppCompatActivity {

    EditText name, mobileNo, email, username, password, Class, section;
    int id;
    Button save;
    DBHelper DB;
    Spinner c1, c2, c3, c4, c5;
    String course1, course2, course3, course4, course5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        getSupportActionBar().setTitle("Go Back");

        name = (EditText) findViewById(R.id.edit_s_name2);
        mobileNo = (EditText) findViewById(R.id.edit_s_contact2);
        email = (EditText) findViewById(R.id.edit_s_email2);
        username = (EditText) findViewById(R.id.edit_s_username2);
        Class = (EditText) findViewById(R.id.edit_s_class2);
        section = (EditText) findViewById(R.id.edit_s_section2);
        password = (EditText) findViewById(R.id.edit_s_password2);
        c1 = (Spinner) findViewById(R.id.edit_s_course11);
        c2 = (Spinner) findViewById(R.id.edit_s_course21);
        c3 = (Spinner) findViewById(R.id.edit_s_course31);
        c4 = (Spinner) findViewById(R.id.edit_s_course41);
        c5 = (Spinner) findViewById(R.id.edit_s_course51);
        DB = new DBHelper(this);
        init();
        save = (Button) findViewById(R.id.btn_save_edit_s);

        course1 = getIntent().getStringExtra("course1");
        course2 = getIntent().getStringExtra("course2");
        course3 = getIntent().getStringExtra("course3");
        course4 = getIntent().getStringExtra("course4");
        course5 = getIntent().getStringExtra("course5");

        final ArrayList<String> c1List = new ArrayList<String>();
        final ArrayList<String> c2List = new ArrayList<String>();
        final ArrayList<String> c3List = new ArrayList<String>();
        final ArrayList<String> c4List = new ArrayList<String>();
        final ArrayList<String> c5List = new ArrayList<String>();

        // following code adds courses to 5 identical courses lists
        // these lists are attached with spinners and admin can select and update student courses
        String qu = "SELECT * FROM COURSES";
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Courses Found", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                c1List.add(cursor.getString(2));
                c2List.add(cursor.getString(2));
                c3List.add(cursor.getString(2));
                c4List.add(cursor.getString(2));
                c5List.add(cursor.getString(2));
                cursor.moveToNext();
            }
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c1List);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c2List);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c3List);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c4List);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c5List);
        c1.setAdapter(adapter1);
        c2.setAdapter(adapter2);
        c3.setAdapter(adapter3);
        c4.setAdapter(adapter4);
        c5.setAdapter(adapter5);
        c1.setSelection(c1List.indexOf(getIntent().getStringExtra("course1")));
        c2.setSelection(c2List.indexOf(getIntent().getStringExtra("course2")));
        c3.setSelection(c3List.indexOf(getIntent().getStringExtra("course3")));
        c4.setSelection(c4List.indexOf(getIntent().getStringExtra("course4")));
        c5.setSelection(c5List.indexOf(getIntent().getStringExtra("course5")));


        // following listeners are called when a course is selected by admin.
        c1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course1 = c1List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        c2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course2 = c2List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        c3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course3 = c3List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        c4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course4 = c4List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        c5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course5 = c5List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //listener for Save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tname = name.getText().toString();
                String mobile = mobileNo.getText().toString();
                String mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String aclass = Class.getText().toString();
                String sec = section.getText().toString();

                // following code makes sure all fields are filled correctly and updates changed entries in database
                if (tname.isEmpty() || mobile.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty() || aclass.isEmpty() || sec.isEmpty())
                    Toast.makeText(EditStudentActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                    Boolean checkUser = false, checkEmail = false, checkMobile = false;
                    if (!(user.equals(getIntent().getStringExtra("username")))) {
                        DB.doesStudentUserNameExist(user);
                    }
                    if (!(mail.equals(getIntent().getStringExtra("email")))) {
                        checkEmail = DB.doesStudentEmailExist(mail);
                    }
                    if (!(mobile.equals(getIntent().getStringExtra("mobileNo")))) {
                        checkMobile = DB.doesStudentMobileNumberExist(mobile);
                    }

                    if (!(checkUser || checkEmail || checkMobile)) {
                        Boolean update = DB.updateStudentData(id, tname, mobile, mail, user, pass, aclass, sec);
                        if (update) {
                            Toast.makeText(EditStudentActivity.this, "Student Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                            DB.deleteStudentCourseData(id);
                            DB.insertStudentCourseData(id, DB.getCourseIDFromCourseName(course1));
                            DB.insertStudentCourseData(id, DB.getCourseIDFromCourseName(course2));
                            DB.insertStudentCourseData(id, DB.getCourseIDFromCourseName(course3));
                            DB.insertStudentCourseData(id, DB.getCourseIDFromCourseName(course4));
                            DB.insertStudentCourseData(id, DB.getCourseIDFromCourseName(course5));
                            DB.close();
                            Log.v("selected courses", course1 + " " + course2 + " " + course3 + " " + course4 + " " + course5);

                            Intent intent = new Intent(getApplicationContext(), ListStudentActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditStudentActivity.this, "Updation failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (checkUser)
                            Toast.makeText(EditStudentActivity.this, "Updation failed!\nUsername already exists!\n", Toast.LENGTH_SHORT).show();
                        else if (checkEmail)
                            Toast.makeText(EditStudentActivity.this, "Updation failed!\nEmail already exists!\n", Toast.LENGTH_SHORT).show();
                        else if (checkMobile)
                            Toast.makeText(EditStudentActivity.this, "Updation failed!\nMobile number already exists!\n", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    // the following method places information regarding student in editable text boxes
    void init() {
        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobileNo.setText(getIntent().getStringExtra("mobileNo"));
        Class.setText(getIntent().getStringExtra("class"));
        section.setText(getIntent().getStringExtra("section"));
        String tem = getIntent().getStringExtra("username");
        username.setText(tem);
        password.setText(getIntent().getStringExtra("password"));
        id = DB.getStudentID(tem);
        return;
    }

}