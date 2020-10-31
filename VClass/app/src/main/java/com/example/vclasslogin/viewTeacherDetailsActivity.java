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
    Button edit,delete,editCourses;
    DBHelper db;
    int teacherID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_detail_view);

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
        editCourses=(Button) findViewById(R.id.t_edit_t_courses);

        init();
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
                intent.putExtra("id",getIntent().getIntExtra("name",-1));
                startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deletion=db.deleteTeacherData(teacherID);
                if(deletion==true){
                    Toast.makeText(viewTeacherDetailsActivity.this,"Deletion Successful",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),ListTeacherActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(viewTeacherDetailsActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
                }


            }
        });

        editCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        ArrayList<Integer> x=new ArrayList<>();

        course1.setText("Artificial Intelligence");
        course2.setText("Software Engineering");
        course3.setText("Object Oriented Analysis And Design");
        String qu = "SELECT * FROM teacher_course";
        DBHelper db= new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No teacher_course Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.v("teacher-Courses: "," list teacherCourses cursor");
                x.add(cursor.getInt(0));

            }
        }


        return;
    }
}