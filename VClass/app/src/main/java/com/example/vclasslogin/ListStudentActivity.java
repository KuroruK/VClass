package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListStudentActivity extends AppCompatActivity {
    RecyclerView rv;
    TextView add;
    MyRvStudentListAdapter adapter;
    ArrayList<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);

        getSupportActionBar().setTitle("Students");

        studentList = new ArrayList<Student>();

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.plusButtonStudent);
        assert addButton != null;
        // following button takes admin to RegisterStudentActivity where admin can add a student
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListStudentActivity.this, RegisterStudentActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        // following code is used to get students from database and place them in studentList
        String qu = "SELECT * FROM STUDENTS";
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Students Found", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                studentList.add(new Student(cursor.getInt(0), " ", " ", " ", " ", " ", cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }

            // following code uses user ids from student list to fetch users from database and fill the empty data members of students in studentList
            for (int i = 0; i < studentList.size(); i++) {
                qu = "Select * from all_users where userid=" + studentList.get(i).getId();
                cursor = db.execReadQuery(qu);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    studentList.get(i).setName(cursor.getString(1));
                    studentList.get(i).setMobileNo(cursor.getString(2));
                    studentList.get(i).setEmail(cursor.getString(3));
                    studentList.get(i).setUsername(cursor.getString(4));
                    studentList.get(i).setPassword(cursor.getString(5));

                    cursor.moveToNext();
                }


            }
        }


        rv = findViewById(R.id.rvListStudent);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListStudentActivity.this);
        rv.setLayoutManager(manager);
        adapter = new MyRvStudentListAdapter(ListStudentActivity.this, studentList);
        rv.setAdapter(adapter);

    }

    // following method called when startActivityForResult returns - in this case from registerStudentActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // following code is used to get students from database and place them in studentList

        if (requestCode == 3) {

            String qu = "SELECT * FROM STUDENTS";
            DBHelper db = new DBHelper(this);
            Cursor cursor = db.execReadQuery(qu);
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(getBaseContext(), "No Students Found", Toast.LENGTH_LONG).show();
            } else {
                cursor.moveToFirst();
                for (int i = studentList.size(); i > 0; i--) {
                    studentList.remove(i - 1);
                }
                while (!cursor.isAfterLast()) {
                    studentList.add(new Student(cursor.getInt(0), " ", " ", " ", " ", " ", cursor.getString(1), cursor.getString(2)));
                    cursor.moveToNext();
                }

                for (int i = 0; i < studentList.size(); i++) {
                    qu = "Select * from all_users where userid=" + studentList.get(i).getId();
                    cursor = db.execReadQuery(qu);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        studentList.get(i).setName(cursor.getString(1));
                        studentList.get(i).setMobileNo(cursor.getString(2));
                        studentList.get(i).setEmail(cursor.getString(3));
                        studentList.get(i).setUsername(cursor.getString(4));
                        studentList.get(i).setPassword(cursor.getString(5));

                        cursor.moveToNext();
                    }


                }
            }


            rv = findViewById(R.id.rvListStudent);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(ListStudentActivity.this);
            rv.setLayoutManager(manager);
            adapter = new MyRvStudentListAdapter(ListStudentActivity.this, studentList);
            rv.setAdapter(adapter);

        }

    }
}