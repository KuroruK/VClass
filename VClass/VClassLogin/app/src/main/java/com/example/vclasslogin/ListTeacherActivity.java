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

public class ListTeacherActivity extends AppCompatActivity {
    RecyclerView rv;
    TextView add;
    MyRvTeacherListAdapter adapter;
    ArrayList<Teacher> teacherList=new ArrayList<Teacher>();
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_list);
        if(counter==0) {
            teacherList = new ArrayList<Teacher>();
            counter++;
        }

        FloatingActionButton addButton=(FloatingActionButton) findViewById(R.id.plusButton);
        assert addButton!=null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListTeacherActivity.this,registerTeacherActivity.class);
                startActivityForResult(intent,3);
            }
        });

        String qu = "SELECT * FROM TEACHERS";
        DBHelper db= new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No Notes Found",Toast.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                teacherList.add(new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6)));
                cursor.moveToNext();
            }
        }
            for(int i=0;i<teacherList.size();i++)
                Log.v("teacherInfo ",teacherList.get(i).toString());
            rv=findViewById(R.id.rvList);
       // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(ListTeacherActivity.this);
        rv.setLayoutManager(manager);
        adapter=new MyRvTeacherListAdapter(ListTeacherActivity.this,teacherList);
        rv.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
                String qu = "SELECT * FROM TEACHERS";
                DBHelper db= new DBHelper(this);
                Cursor cursor = db.execReadQuery(qu);
                if(cursor==null||cursor.getCount()==0)
                {
                    Toast.makeText(getBaseContext(),"No Notes Found",Toast.LENGTH_LONG).show();
                }else {
                    cursor.moveToFirst();
                    for(int i=teacherList.size();i>0;i--){
                        teacherList.remove(i-1);

                    }
                    while (!cursor.isAfterLast()) {
                        teacherList.add(new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6)));
                        cursor.moveToNext();
                    }
                }

                rv=findViewById(R.id.rvList);
          //s      add=findViewById(R.id.address);
                RecyclerView.LayoutManager manager=new LinearLayoutManager(ListTeacherActivity.this);
               rv.setLayoutManager(manager);
               adapter=new MyRvTeacherListAdapter(ListTeacherActivity.this,teacherList);
                rv.setAdapter(adapter);

            }

    }
}