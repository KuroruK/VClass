package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

public class editTeacherActivity extends AppCompatActivity {

    EditText name, mobileNo, email, username, password,specialization;
    int id;
    Button save;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);

        name = (EditText) findViewById(R.id.t_edit_Name);
        mobileNo = (EditText) findViewById(R.id.t_edit_mobile);
        email = (EditText) findViewById(R.id.t_edit_email);
        username = (EditText) findViewById(R.id.t_edit_username);
        specialization=(EditText) findViewById(R.id.t_edit_spec);
        password = (EditText) findViewById(R.id.t_edit_password);

        DB = new DBHelper(this);
        init();
        save = (Button) findViewById(R.id.t_save_edit);

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