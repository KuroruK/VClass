package com.example.vclasslogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    AppCompatCheckBox showPasswordCheckbox;
    Button btnLogin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("VClass");

        DB = new DBHelper(this);
        //DB.deleteTables();
        DB.createTables();
        if(!DB.doesUserNameExist("admin")) {
            Boolean c = DB.insertData("Admin", "090078601", "manager.vclass@gmail.com", "admin", "pass", "admin");
            if (c)
                Log.v("insertinsert", "hogaya");
        }
        else
            Log.v("insertinsert","nai hogaya");

        if(!DB.doesStudentUserNameExist("student1")) {
            DB.initTeachersAndStudents();
            this.initCourses();
            DB.initStudentCourse();
            DB.initTeacherCourse();
        }
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        showPasswordCheckbox = (AppCompatCheckBox) findViewById(R.id.showPassword1);
        btnLogin = (Button) findViewById((R.id.btnSignIn));


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                    Boolean checkCredentials = DB.checkUsernamePassword(user, pass);
                    //Log.v("UserType", "ye lo");

                    if (checkCredentials) {
                        String type = DB.getUserType(user, pass);

                        //Log.v("UserType", type);

                        if (type.equalsIgnoreCase("student"))
                        {
                            Intent intent = new Intent(getApplicationContext(), StudentView.class);
                            intent.putExtra("student-username",user);
                            startActivity(intent);
                        }
                        else if (type.equalsIgnoreCase("teacher"))
                        {
                            Intent intent = new Intent(getApplicationContext(), TeacherView.class);
                            intent.putExtra("teacher-username",user);
                            startActivity(intent);
                        }

                        else if (type.equalsIgnoreCase("admin"))
                        {
                            Intent intent = new Intent(getApplicationContext(), AdminView.class);
                            intent.putExtra("admin username",user);
                            startActivity(intent);
                        }

                        Toast.makeText(LoginActivity.this, "Welcome to VClass!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //listener for show password checkbox
        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void initCourses(){
        csvLoader loader=new csvLoader();

        ArrayList<course> crs=loader.getCourses(this,"crs.csv");
        for(course c:crs){
            if(!(DB.doesCourseCodeExist(c.code))){
                DB.insertCourseData(c.code,c.name,c.CHs,c.coordinator);
                Log.v("course add",Boolean.toString(DB.doesCourseCodeExist(c.code)));
            }
        }
        crs=loader.getCourses(this,"crs2.csv");
        for(course c:crs){
            if(!(DB.doesCourseCodeExist(c.code))){
                DB.insertCourseData(c.code,c.name,c.CHs,c.coordinator);
            }
        }
/*        crs=loader.getCourses(this,"crs3.csv");
        for(course c:crs){
            if(!(DB.doesCourseCodeExist(c.code))){
                DB.insertCourseData(c.code,c.name,c.CHs,c.coordinator);
            }
        }*/
    }

}