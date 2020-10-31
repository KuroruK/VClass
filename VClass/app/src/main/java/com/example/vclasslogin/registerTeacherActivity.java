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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

public class registerTeacherActivity extends AppCompatActivity {

    EditText firstName, lastName, mobileNo, email, username, password, cPassword,specialization;
    AppCompatCheckBox showPasswordCheckbox;
    Button signUp;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        mobileNo = (EditText) findViewById(R.id.mobileNo);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        specialization=(EditText) findViewById(R.id.specialization);
        password = (EditText) findViewById(R.id.password);
        cPassword = (EditText) findViewById(R.id.cPassword);
        showPasswordCheckbox = (AppCompatCheckBox) findViewById(R.id.showPassword);

        signUp = (Button) findViewById(R.id.btnSignUp);

        DB = new DBHelper(this);
        //listener for Sign-up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String mobile = mobileNo.getText().toString();
                String mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String cPass = cPassword.getText().toString();
                String spec= specialization.getText().toString();

                if (fName.isEmpty() || mobile.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty() || cPass.isEmpty() || spec.isEmpty())
                    Toast.makeText(registerTeacherActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(cPass)) {

                        Boolean checkUser = DB.doesTeacherUserNameExist(user), checkEmail = DB.doesTeacherEmailExist(mail), checkMobile = DB.doesTeacherMobileNumberExist(mobile);

                        if (!(checkUser || checkEmail || checkMobile)) {
                            Boolean insert = DB.insertTeacherData((fName + " " + lName) , mobile, mail, user, pass,spec);
                            if (insert) {
                                Toast.makeText(registerTeacherActivity.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), manageTeacherActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(registerTeacherActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (checkUser)
                                Toast.makeText(registerTeacherActivity.this, "Registration failed!\nUsername already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                            else if (checkEmail)
                                Toast.makeText(registerTeacherActivity.this, "Registration failed!\nEmail already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                            else if (checkMobile)
                                Toast.makeText(registerTeacherActivity.this, "Registration failed!\nMobile number already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(registerTeacherActivity.this, "Passwords are not matching!", Toast.LENGTH_SHORT).show();
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
                    cPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    // return checked radio button
}