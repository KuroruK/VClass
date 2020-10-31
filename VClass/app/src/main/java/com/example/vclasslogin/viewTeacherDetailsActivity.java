package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;

public class viewTeacherDetailsActivity extends AppCompatActivity {

    TextView name, mobileNo, email, username, password,specialization,id,course1,course2,course3;
    Button edit,delete;
    DBHelper db;
    int teacherID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_detail_view);

        getSupportActionBar().setTitle("Detail View");

        name = findViewById(R.id.t_name2);
        email = findViewById(R.id.t_email2);
        mobileNo = findViewById(R.id.t_contact2);
        username = findViewById(R.id.t_username2);
        specialization = findViewById(R.id.t_qualification2);
        password = findViewById(R.id.t_password2);
        course1 = findViewById(R.id.t_course1);
        course2 = findViewById(R.id.t_course21);
        course3 = findViewById(R.id.t_course31);
        id = findViewById(R.id.t_id2);
        db=new DBHelper(this);
        edit = (Button) findViewById(R.id.t_edit);
        delete = (Button) findViewById(R.id.t_delete);
        init();
        String qu = "SELECT * FROM teacher_course";
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No teacher_course Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            int i=0;
            while (!cursor.isAfterLast()) {
                if(cursor.getInt(0)==teacherID) {
                    if (i == 0) {
                        course1.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 1) {
                        course2.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 2) {
                        course3.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    i++;
                }
                cursor.moveToNext();

            }
        }

        //init();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(),editTeacherActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("email",getIntent().getStringExtra("email"));
                intent.putExtra("mobileNo",getIntent().getStringExtra("mobileNo"));
                intent.putExtra("username",getIntent().getStringExtra("username"));
                intent.putExtra("password",getIntent().getStringExtra("password"));
                intent.putExtra("specialization",getIntent().getStringExtra("specialization"));
                intent.putExtra("course1",course1.getText().toString());
                intent.putExtra("course2",course2.getText().toString());
                intent.putExtra("course3",course3.getText().toString());
                intent.putExtra("id",getIntent().getIntExtra("id",-1));
                startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deletion=db.deleteTeacherData(teacherID);
                if(deletion){
                    Toast.makeText(viewTeacherDetailsActivity.this,"Deletion Successful",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),ListTeacherActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(viewTeacherDetailsActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    void init(){
        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobileNo.setText(getIntent().getStringExtra("mobileNo"));
        username.setText(getIntent().getStringExtra("username"));
        specialization.setText(getIntent().getStringExtra("specialization"));
        password.setText(getIntent().getStringExtra("password"));
        teacherID=db.getTeacherID(getIntent().getStringExtra("username"));
        id.setText(Integer.toString(teacherID));


        course1.setText("None");
        course2.setText("None");
        course3.setText("None");



        return;
    }
}