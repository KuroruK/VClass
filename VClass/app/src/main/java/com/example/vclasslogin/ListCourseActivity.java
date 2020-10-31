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

public class ListCourseActivity extends AppCompatActivity {
    RecyclerView rv;
    TextView add;
    MyRvCourseListAdapter adapter;
    ArrayList<Courses> courseList;
    static int ccounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);
        courseList=new ArrayList<Courses>();
        FloatingActionButton addButton=(FloatingActionButton) findViewById(R.id.plusButtonCourse);
        assert addButton!=null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListCourseActivity.this,registerCourseActivity.class);
                startActivityForResult(intent,4);
            }
        });
        String qu = "SELECT * FROM COURSES";
        DBHelper db= new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No Courses Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.v("Courses: "," list Courses cursor");
                courseList.add(new Courses(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                cursor.moveToNext();
            }
        }
            for(int i=0;i<courseList.size();i++)
                Log.v("CourseInfo ",courseList.get(i).toString());
            rv=findViewById(R.id.rvListCourse);
       // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(ListCourseActivity.this);
        rv.setLayoutManager(manager);
        adapter=new MyRvCourseListAdapter(ListCourseActivity.this,courseList);
        rv.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
                String qu = "SELECT * FROM COURSES";
                DBHelper db= new DBHelper(this);
                Cursor cursor = db.execReadQuery(qu);
                if(cursor==null||cursor.getCount()==0)
                {
                    Toast.makeText(getBaseContext(),"No Courses Found",Toast.LENGTH_LONG).show();
                }else {
                    cursor.moveToFirst();
                    for(int i=courseList.size();i>0;i--){
                        courseList.remove(i-1);

                    }
                    while (!cursor.isAfterLast()) {
                        courseList.add(new Courses(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                        cursor.moveToNext();
                    }
                }

                rv=findViewById(R.id.rvListCourse);
                RecyclerView.LayoutManager manager=new LinearLayoutManager(ListCourseActivity.this);
               rv.setLayoutManager(manager);
               adapter=new MyRvCourseListAdapter(ListCourseActivity.this,courseList);
                rv.setAdapter(adapter);

            }

    }
}