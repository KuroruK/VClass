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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

public class registerStudentActivity extends AppCompatActivity {

    EditText firstName, lastName, mobileNo, email, username, password, cPassword,Class,section;
    AppCompatCheckBox showPasswordCheckbox;
    Button signUp;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        firstName = (EditText) findViewById(R.id.firstNameS);
        lastName = (EditText) findViewById(R.id.lastNameS);
        mobileNo = (EditText) findViewById(R.id.mobileNoS);
        email = (EditText) findViewById(R.id.emailS);
        username = (EditText) findViewById(R.id.usernameS);
        Class = (EditText) findViewById(R.id.classS);
        section = (EditText) findViewById(R.id.sectionS);
        password = (EditText) findViewById(R.id.passwordS);
        cPassword = (EditText) findViewById(R.id.cPasswordS);
        showPasswordCheckbox = (AppCompatCheckBox) findViewById(R.id.showPasswordS);

        signUp = (Button) findViewById(R.id.btnSignUpS);

        DB = new DBHelper(this);
        //listener for Sign-up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String mobile = mobileNo.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String cPass = cPassword.getText().toString().trim();
                String clas= Class.getText().toString().trim();
                String sect= section.getText().toString().trim();


                if (fName.isEmpty() || mobile.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty() || cPass.isEmpty() || clas.isEmpty() || sect.isEmpty())
                    Toast.makeText(registerStudentActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(cPass)) {

                        Boolean checkUser = DB.doesStudentUserNameExist(user), checkEmail = DB.doesStudentEmailExist(mail), checkMobile = DB.doesStudentMobileNumberExist(mobile);

                        if (!(checkUser || checkEmail || checkMobile)) {
                            Boolean insert = DB.insertStudentData((fName + " " + lName) , mobile, mail, user, pass,clas,sect);
                            if (insert) {
                                sendMail(mail,user,pass);
                                Toast.makeText(registerStudentActivity.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ListStudentActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(registerStudentActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (checkUser)
                                Toast.makeText(registerStudentActivity.this, "Registration failed!\nUsername already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                            else if (checkEmail)
                                Toast.makeText(registerStudentActivity.this, "Registration failed!\nEmail already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                            else if (checkMobile)
                                Toast.makeText(registerStudentActivity.this, "Registration failed!\nMobile number already exists!\nPlease login!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(registerStudentActivity.this, "Passwords are not matching!", Toast.LENGTH_SHORT).show();
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
    private void sendMail(String mail,String username,String password) {
        String rcvEmail = mail;
        String sbjStr = "Welcome to VClass.";
        String msgStr = "Now you can log into VClass by using the following:\nUsername: "
                + username+"\nPassword: "+ password;

        // sending mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, rcvEmail, sbjStr, msgStr);
        javaMailAPI.execute();

        Log.v("temp_mail", rcvEmail + "___" + sbjStr + "___" + msgStr);
    }
    // return checked radio button
}