package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        // actionbar - title
        getSupportActionBar().setTitle("VClass");

        // getting fields from layout
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        showPasswordCheckbox = (AppCompatCheckBox) findViewById(R.id.showPassword1);
        btnLogin = (Button) findViewById((R.id.btnSignIn));

        // initializing DB and creating necessary tables to run
        DB = new DBHelper(this);
        DB.createTables();
        if (!DB.doesUserNameExist("admin")) {
            Boolean c = DB.insertData("Admin", "090078601", "manager.vclass@gmail.com", "admin", "pass", "admin");
        }

        // adding dummy data into DB to run application
        if (!DB.doesStudentUserNameExist("student1")) {
            DB.initTeachersAndStudents();
            this.initCourses();
            DB.initStudentCourse();
            DB.initTeacherCourse();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                //  condition to check if username and password is entered or not
                if (user.isEmpty() || pass.isEmpty())
                    Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkCredentials = DB.checkUsernamePassword(user, pass);    // checking credentials for DB

                    // if credentials are correct, load a particular view for a particular user i.e, AdminView for Admin and so on
                    if (checkCredentials) {
                        String type = DB.getUserType(user, pass);

                        if (type.equalsIgnoreCase("student")) {
                            Intent intent = new Intent(getApplicationContext(), StudentView.class);
                            intent.putExtra("student-username", user);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase("teacher")) {
                            Intent intent = new Intent(getApplicationContext(), TeacherView.class);
                            intent.putExtra("teacher-username", user);
                            startActivity(intent);
                        } else if (type.equalsIgnoreCase("admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminView.class);
                            intent.putExtra("admin-username", user);
                            startActivity(intent);
                        }

                        Toast.makeText(LoginActivity.this, "Welcome to VClass!", Toast.LENGTH_SHORT).show();

                    } else {
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

    // function to get Courses from csv and load into the DB
    private void initCourses() {
        CsvLoader loader = new CsvLoader();

        // loading programming Courses
        ArrayList<Course> crs = loader.getCourses(this, "crs.csv");
        for (Course c : crs) {
            if (!(DB.doesCourseCodeExist(c.code))) {
                DB.insertCourseData(c.code, c.name, c.CHs, c.coordinator);
            }
        }

        // loading other Courses
        crs = loader.getCourses(this, "crs2.csv");
        for (Course c : crs) {
            if (!(DB.doesCourseCodeExist(c.code))) {
                DB.insertCourseData(c.code, c.name, c.CHs, c.coordinator);
            }
        }

        DB.setClassDetailsTable();
    }
}