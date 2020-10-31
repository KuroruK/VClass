package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class viewStudentDetailsActivity extends AppCompatActivity {

    TextView name, mobileNo, email, username, password,Class,section,id,course1,course2,course3,course4,course5;
    Button edit,delete;
    DBHelper db;
    int studentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_detail_view);

        getSupportActionBar().setTitle("Detail View");

        name = findViewById(R.id.s_name2);
        email = findViewById(R.id.s_email2);
        mobileNo = findViewById(R.id.s_contact2);
        username = findViewById(R.id.s_username2);
        password = findViewById(R.id.s_password2);
        Class=findViewById(R.id.s_class2);
        section=findViewById(R.id.s_section2);
        course1=findViewById(R.id.s_course1);
        course2=findViewById(R.id.s_course21);
        course3=findViewById(R.id.s_course31);
        course4=findViewById(R.id.s_course41);
        course5=findViewById(R.id.s_course51);

        id = findViewById(R.id.s_id2);
        db=new DBHelper(this);
        edit = (Button) findViewById(R.id.s_edit);
        delete = (Button) findViewById(R.id.s_delete);
        init();

        String qu = "SELECT * FROM student_course";
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No student_course Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            int i=0;
            while (!cursor.isAfterLast()) {
                if(cursor.getInt(0)==studentID) {
                    if (i == 0) {
                        course1.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 1) {
                        course2.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 2) {
                        course3.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 3) {
                        course4.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    if (i == 4) {
                        course5.setText(db.getCourseName(cursor.getInt(1)));
                    }
                    i++;
                }
                cursor.moveToNext();

            }
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(),editStudentActivity.class);
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("email",getIntent().getStringExtra("email"));
                intent.putExtra("mobileNo",getIntent().getStringExtra("mobileNo"));
                intent.putExtra("username",getIntent().getStringExtra("username"));
                intent.putExtra("password",getIntent().getStringExtra("password"));
                intent.putExtra("class",getIntent().getStringExtra("class"));
                intent.putExtra("section",getIntent().getStringExtra("section"));
                intent.putExtra("course1",course1.getText().toString());
                intent.putExtra("course2",course2.getText().toString());
                intent.putExtra("course3",course3.getText().toString());
                intent.putExtra("course4",course4.getText().toString());
                intent.putExtra("course5",course5.getText().toString());

                intent.putExtra("id",getIntent().getIntExtra("id",-1));
                startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deletion=db.deleteStudentData(studentID);
                if(deletion==true){
                    Toast.makeText(viewStudentDetailsActivity.this,"Deletion Successful",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),ListStudentActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(viewStudentDetailsActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    void init(){
        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobileNo.setText(getIntent().getStringExtra("mobileNo"));
        username.setText(getIntent().getStringExtra("username"));
        Class.setText(getIntent().getStringExtra("class"));
        section.setText(getIntent().getStringExtra("section"));
        password.setText(getIntent().getStringExtra("password"));
        studentID=db.getStudentID(getIntent().getStringExtra("username"));
        id.setText(Integer.toString(studentID));

        course1.setText("None");
        course2.setText("None");
        course3.setText("None");
        course4.setText("None");
        course5.setText("None");


        return;
    }
}