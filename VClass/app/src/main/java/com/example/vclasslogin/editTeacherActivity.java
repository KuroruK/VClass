package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;

public class editTeacherActivity extends AppCompatActivity {

    EditText name, mobileNo, email, username, password, specialization;
    int id;
    Button save;
    DBHelper DB;
    Spinner c1, c2, c3;
    String course1,course2,course3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);

        getSupportActionBar().setTitle("Go Back");

        name = (EditText) findViewById(R.id.edit_t_name2);
        mobileNo = (EditText) findViewById(R.id.edit_t_contact2);
        email = (EditText) findViewById(R.id.edit_t_email2);
        username = (EditText) findViewById(R.id.edit_t_username2);
        specialization = (EditText) findViewById(R.id.edit_t_qualification2);
        password = (EditText) findViewById(R.id.edit_t_password2);
        c1 = (Spinner) findViewById((R.id.edit_t_course11));
        c2 = (Spinner) findViewById((R.id.edit_t_course21));
        c3 = (Spinner) findViewById((R.id.edit_t_course31));
        DB = new DBHelper(this);
        init();
        save = (Button) findViewById(R.id.btn_save_edit_t);
        course1=getIntent().getStringExtra("course1");
        course2=getIntent().getStringExtra("course2");
        course3=getIntent().getStringExtra("course3");

        final ArrayList<String> c1List=new ArrayList<String>();
        final ArrayList<String> c2List=new ArrayList<String>();
        final ArrayList<String> c3List=new ArrayList<String>();
        String qu = "SELECT * FROM COURSES";
        DBHelper db= new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No Courses Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.v("Courses: "," list Courses cursor");
                c1List.add(cursor.getString(2));
                c2List.add(cursor.getString(2));
                c3List.add(cursor.getString(2));
                cursor.moveToNext();
            }
        }
ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c1List);
ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c2List);
ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, c3List);
       c1.setAdapter(adapter1);
       c2.setAdapter(adapter2);
       c3.setAdapter(adapter3);
       c1.setSelection(c1List.indexOf(getIntent().getStringExtra("course1")));
       c2.setSelection(c2List.indexOf(getIntent().getStringExtra("course2")));
       c3.setSelection(c3List.indexOf(getIntent().getStringExtra("course3")));


        c1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course1= c1List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("Course selected1","Nothing selected");


            }
        });
        c2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course2= c2List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        c3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course3= c3List.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("Course selected3","Nothing 3 selected");

            }
        });







    //listener for Sign-up button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tname = name.getText().toString();
                String mobile = mobileNo.getText().toString();
                String mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String spec= specialization.getText().toString();

                if (tname.isEmpty() || mobile.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty()  || spec.isEmpty())
                    Toast.makeText(editTeacherActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                        Boolean checkUser = false, checkEmail = false, checkMobile = false;
                        if(!(user.equals(getIntent().getStringExtra("username")))){
                            DB.doesTeacherUserNameExist(user);
                        }
                    if(!(mail.equals(getIntent().getStringExtra("email")))){
                        checkEmail = DB.doesTeacherEmailExist(mail);
                    }
                    if(!(mobile.equals(getIntent().getStringExtra("mobileNo")))){
                        checkMobile = DB.doesTeacherMobileNumberExist(mobile);
                    }

                        if (!(checkUser || checkEmail || checkMobile)) {

                            Boolean update = DB.updateTeacherData(id,tname, mobile, mail, user, pass,spec);
                            if (update) {
                                Toast.makeText(editTeacherActivity.this, "Teacher Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                                DB.deleteTeacherCourseData(id);
                                DB.insertTeacherCourseData(id,DB.getCourseIDFromCourseName(course1));
                                DB.insertTeacherCourseData(id,DB.getCourseIDFromCourseName(course2));
                                DB.insertTeacherCourseData(id,DB.getCourseIDFromCourseName(course3));
                                Log.v("selected courses",course1+" "+course2+" "+course3);
                                Intent intent = new Intent(getApplicationContext(), ListTeacherActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(editTeacherActivity.this, "Updation failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (checkUser)
                                Toast.makeText(editTeacherActivity.this, "Updation failed!\nUsername already exists!\n", Toast.LENGTH_SHORT).show();
                            else if (checkEmail)
                                Toast.makeText(editTeacherActivity.this, "Updation failed!\nEmail already exists!\n", Toast.LENGTH_SHORT).show();
                            else if (checkMobile)
                                Toast.makeText(editTeacherActivity.this, "Updation failed!\nMobile number already exists!\n", Toast.LENGTH_SHORT).show();
                        }

                }
            }
        });


    }
    void init(){
        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobileNo.setText(getIntent().getStringExtra("mobileNo"));
        String tem=getIntent().getStringExtra("username");
        username.setText(tem);
        specialization.setText(getIntent().getStringExtra("specialization"));
        password.setText(getIntent().getStringExtra("password"));
        id=DB.getTeacherID(tem);


        return;
    }

    // return checked radio button
}