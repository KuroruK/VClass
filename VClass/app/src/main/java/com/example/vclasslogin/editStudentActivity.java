package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class editStudentActivity extends AppCompatActivity {

    EditText name, mobileNo, email, username, password,Class,section;
    int id;
    Button save;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        name = (EditText) findViewById(R.id.s_edit_Name);
        mobileNo = (EditText) findViewById(R.id.s_edit_mobile);
        email = (EditText) findViewById(R.id.s_edit_email);
        username = (EditText) findViewById(R.id.s_edit_username);
        Class = (EditText) findViewById(R.id.s_edit_class);
        section = (EditText) findViewById(R.id.s_edit_section);
        password = (EditText) findViewById(R.id.s_edit_password);

        DB = new DBHelper(this);
        init();
        save = (Button) findViewById(R.id.s_save_edit);

        //listener for Sign-up button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tname = name.getText().toString();
                String mobile = mobileNo.getText().toString();
                String mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String aclass= Class.getText().toString();
                String sec= section.getText().toString();

                if (tname.isEmpty() || mobile.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty()  || aclass.isEmpty() || sec.isEmpty())
                    Toast.makeText(editStudentActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else {

                        Boolean checkUser = false, checkEmail = false, checkMobile = false;
                        if(!(user.equals(getIntent().getStringExtra("username")))){
                            DB.doesStudentUserNameExist(user);
                        }
                    if(!(mail.equals(getIntent().getStringExtra("email")))){
                        checkEmail = DB.doesStudentEmailExist(mail);
                    }
                    if(!(mobile.equals(getIntent().getStringExtra("mobileNo")))){
                        checkMobile = DB.doesStudentMobileNumberExist(mobile);
                    }

                        if (!(checkUser || checkEmail || checkMobile)) {
                            Boolean update = DB.updateStudentData(id,tname, mobile, mail, user, pass,aclass,sec);
                            if (update) {
                                Toast.makeText(editStudentActivity.this, "Student Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ListStudentActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(editStudentActivity.this, "Updation failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (checkUser)
                                Toast.makeText(editStudentActivity.this, "Updation failed!\nUsername already exists!\n", Toast.LENGTH_SHORT).show();
                            else if (checkEmail)
                                Toast.makeText(editStudentActivity.this, "Updation failed!\nEmail already exists!\n", Toast.LENGTH_SHORT).show();
                            else if (checkMobile)
                                Toast.makeText(editStudentActivity.this, "Updation failed!\nMobile number already exists!\n", Toast.LENGTH_SHORT).show();
                        }

                }
            }
        });


    }
    void init(){
        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobileNo.setText(getIntent().getStringExtra("mobileNo"));
        Class.setText(getIntent().getStringExtra("class"));
        section.setText(getIntent().getStringExtra("section"));
        String tem=getIntent().getStringExtra("username");
        username.setText(tem);
        password.setText(getIntent().getStringExtra("password"));
        id=DB.getStudentID(tem);
        return;
    }

    // return checked radio button
}