package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// this activity shows list of all teachers to admin
public class ListTeacherActivity extends AppCompatActivity {
    RecyclerView rv;
    MyRvTeacherListAdapter adapter;
    ArrayList<Teacher> teacherList = new ArrayList<Teacher>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_list);

        // back button - action bar
        getSupportActionBar().setTitle("Teachers");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        teacherList = new ArrayList<Teacher>();


        FloatingActionButton addButton = findViewById(R.id.plusButton);
        assert addButton != null;
        // following button takes admin to RegisterTeacherActivity where admin can add a teacher
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTeacherActivity.this, RegisterTeacherActivity.class);
                startActivityForResult(intent, 3);
            }
        });


        // following code is used to get teachers from database and place them in teacherList
        String qu = "SELECT * FROM TEACHERS";
        DBHelper db = new DBHelper(this);
        Cursor cursor = db.execReadQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Teachers Found", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                teacherList.add(new Teacher(cursor.getInt(0), " ", " ", " ", " ", " ", cursor.getString(1)));
                cursor.moveToNext();
            }

            // following code uses user ids from teacher list to fetch users from database and fill the empty data members of teachers in teachersList
            for (int i = 0; i < teacherList.size(); i++) {
                qu = "Select * from all_users where userid=" + teacherList.get(i).getId();
                cursor = db.execReadQuery(qu);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    teacherList.get(i).setName(cursor.getString(1));
                    teacherList.get(i).setMobileNo(cursor.getString(2));
                    teacherList.get(i).setEmail(cursor.getString(3));
                    teacherList.get(i).setUsername(cursor.getString(4));
                    teacherList.get(i).setPassword(cursor.getString(5));

                    cursor.moveToNext();
                }
            }
        }

        rv = findViewById(R.id.rvList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListTeacherActivity.this);
        rv.setLayoutManager(manager);
        adapter = new MyRvTeacherListAdapter(ListTeacherActivity.this, teacherList);
        rv.setAdapter(adapter);
    }


    // following method called when startActivityForResult returns - in this case from registerTeacherActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // following code is used to get teachers from database and place them in teacherList

        if (requestCode == 3) {

            String qu = "SELECT * FROM TEACHERS";
            DBHelper db = new DBHelper(this);
            Cursor cursor = db.execReadQuery(qu);
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(getBaseContext(), "No Teachers Found", Toast.LENGTH_LONG).show();
            } else {

                cursor.moveToFirst();
                for (int i = teacherList.size(); i > 0; i--) {
                    teacherList.remove(i - 1);
                }

                while (!cursor.isAfterLast()) {
                    teacherList.add(new Teacher(cursor.getInt(0), " ", " ", " ", " ", " ", cursor.getString(1)));
                    cursor.moveToNext();
                }

                for (int i = 0; i < teacherList.size(); i++) {
                    qu = "Select * from all_users where userid=" + teacherList.get(i).getId();
                    cursor = db.execReadQuery(qu);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        teacherList.get(i).setName(cursor.getString(1));
                        teacherList.get(i).setMobileNo(cursor.getString(2));
                        teacherList.get(i).setEmail(cursor.getString(3));
                        teacherList.get(i).setUsername(cursor.getString(4));
                        teacherList.get(i).setPassword(cursor.getString(5));

                        cursor.moveToNext();
                    }
                }
            }

            rv = findViewById(R.id.rvList);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(ListTeacherActivity.this);
            rv.setLayoutManager(manager);
            adapter = new MyRvTeacherListAdapter(ListTeacherActivity.this, teacherList);
            rv.setAdapter(adapter);
        }
    }


    // method used to go to previous activity when back button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), AdminView.class);
        intent.putExtra("admin-username", getIntent().getStringExtra("username"));
        startActivityForResult(intent, 0);
        return true;
    }
}