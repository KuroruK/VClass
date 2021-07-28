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

// this activity shows list of all courses to admin
public class ListCourseActivity extends AppCompatActivity {
    RecyclerView rv;
    MyRvCourseListAdapter adapter;
    ArrayList<Courses> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        getSupportActionBar().setTitle("Courses");

        courseList = new ArrayList<Courses>();
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.plusButtonCourse);
        // following button takes admin to RegisterCourseActivity where admin can add a course
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListCourseActivity.this, RegisterCourseActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        // following code is used to get courses from database and place them in courseList
        String qu = "SELECT * FROM COURSES";
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Courses Found", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                courseList.add(new Courses(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                cursor.moveToNext();
            }
        }
        rv = findViewById(R.id.rvListCourse);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListCourseActivity.this);
        rv.setLayoutManager(manager);
        adapter = new MyRvCourseListAdapter(ListCourseActivity.this, courseList);
        rv.setAdapter(adapter);

    }

    // following method called when startActivityForResult returns - in this case from registerCourseActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // following code is used to get courses from database and place them in courseList

        if (requestCode == 4) {
            String qu = "SELECT * FROM COURSES";
            DBHelper db = new DBHelper(this);
            Cursor cursor = db.execReadQuery(qu);
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(getBaseContext(), "No Courses Found", Toast.LENGTH_LONG).show();
            } else {
                cursor.moveToFirst();
                for (int i = courseList.size(); i > 0; i--) {
                    courseList.remove(i - 1);

                }
                while (!cursor.isAfterLast()) {
                    courseList.add(new Courses(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                    cursor.moveToNext();
                }
            }

            rv = findViewById(R.id.rvListCourse);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(ListCourseActivity.this);
            rv.setLayoutManager(manager);
            adapter = new MyRvCourseListAdapter(ListCourseActivity.this, courseList);
            rv.setAdapter(adapter);

        }

    }
}