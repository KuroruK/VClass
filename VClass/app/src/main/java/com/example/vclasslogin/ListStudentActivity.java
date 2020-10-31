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
    static int scounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        studentList=new ArrayList<Student>();

        FloatingActionButton addButton=(FloatingActionButton) findViewById(R.id.plusButtonStudent);
        assert addButton!=null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListStudentActivity.this,registerStudentActivity.class);
                startActivityForResult(intent,3);
            }
        });

        String qu = "SELECT * FROM STUDENTS";
        DBHelper db= new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No Students Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                studentList.add(new Student(cursor.getInt(0), " ", " ", " "," ", " ",cursor.getString(1),cursor.getString(2)));
                cursor.moveToNext();
            }

            for(int i=0;i<studentList.size();i++){
                qu="Select * from all_users where userid="+studentList.get(i).getId();
                cursor=db.execReadQuery(qu);
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



        for(int i=0;i<studentList.size();i++)
                Log.v("StudentInfo ",studentList.get(i).toString());
            rv=findViewById(R.id.rvListStudent);
       // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(ListStudentActivity.this);
        rv.setLayoutManager(manager);
        adapter=new MyRvStudentListAdapter(ListStudentActivity.this,studentList);
        rv.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
    /*            String qu = "SELECT * FROM STUDENTS";
                DBHelper db= new DBHelper(this);
                Cursor cursor = db.execReadQuery(qu);
                if(cursor==null||cursor.getCount()==0)
                {
                    Toast.makeText(getBaseContext(),"No Students Found",Toast.LENGTH_LONG).show();
                }else {
                    cursor.moveToFirst();
                    for(int i=studentList.size();i>0;i--){
                        studentList.remove(i-1);
                    }
                    while (!cursor.isAfterLast()) {
                        studentList.add(new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),cursor.getString(7)));
                        cursor.moveToNext();
                    }
                }

     */
            String qu = "SELECT * FROM STUDENTS";
            DBHelper db= new DBHelper(this);
            Cursor cursor = db.execReadQuery(qu);
            if(cursor==null||cursor.getCount()==0)
            {
                Toast.makeText(getBaseContext(),"No Students Found",Toast.LENGTH_LONG).show();
            }else {
                cursor.moveToFirst();
                for(int i=studentList.size();i>0;i--){
                    studentList.remove(i-1);
                }
                while (!cursor.isAfterLast()) {
                    studentList.add(new Student(cursor.getInt(0), " ", " ", " "," ", " ",cursor.getString(1),cursor.getString(2)));
                    cursor.moveToNext();
                }

                for(int i=0;i<studentList.size();i++){
                    qu="Select * from all_users where userid="+studentList.get(i).getId();
                    cursor=db.execReadQuery(qu);
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



            rv=findViewById(R.id.rvListStudent);
          //s      add=findViewById(R.id.address);
                RecyclerView.LayoutManager manager=new LinearLayoutManager(ListStudentActivity.this);
               rv.setLayoutManager(manager);
               adapter=new MyRvStudentListAdapter(ListStudentActivity.this,studentList);
                rv.setAdapter(adapter);

            }

    }
}