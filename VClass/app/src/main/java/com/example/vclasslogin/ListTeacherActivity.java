package com.example.vclasslogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
                startActivityForResult(intent,2);
            }
        });

        FloatingActionButton msgButton=(FloatingActionButton) findViewById(R.id.messageButton);
        assert msgButton!=null;
        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       //         Intent intent=new Intent(MainActivity.this,sendMessage.class);
       //         String[] names=new String[contactLists.size()];
        //        String[] phoneNumbers=new String[contactLists.size()];
        //        for(int i=0;i<contactLists.size();i++){
        //            names[i]=contactLists.get(i).getName();
         //           phoneNumbers[i]=contactLists.get(i).getPhone();
           //     }
         //       intent.putExtra("names",names);
          //      intent.putExtra("phoneNumbers",phoneNumbers);
          //      startActivity(intent);
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
                teacherList.add(new Teacher(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
                cursor.moveToNext();
            }
        }

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
    //    contactLists.add(new Contact("ahmed","1234","a@gmail.dom","islamabad"));

        if (requestCode == 2) {
            String newcontact = data.getStringExtra("newContact");
            if (newcontact != null) {
                int n = 0, e = 0, p = 0, a = 0;
                int j = 0;
             /*   for (int i = 0; i < newcontact.length(); i++) {
                    if (newcontact.charAt(i) == ',') {
                        if (j == 0) {
                            n = i;
                            j++;
                        } else if (j == 1) {
                            e = i;
                            j++;
                        } else if (j == 2) {
                            p = i;
                            j++;
                        } else {
                            a = i;
                            j++;
                        }
                    }
                }
*/
  //              String name = newcontact.substring(0, n);
  //              String email = newcontact.substring(n + 1, e);
  //              String phone = newcontact.substring(e + 1, p);
  //              String address = newcontact.substring(p + 1, newcontact.length());
   //             if(!(name.equals("")) && !(phone.equals(""))) {
            //        contactLists.add(new Contact(name, phone, email, address));
     //           }
                String qu = "SELECT * FROM TEACHERS";
                DBHelper db= new DBHelper(this);
                Cursor cursor = db.execReadQuery(qu);
                if(cursor==null||cursor.getCount()==0)
                {
                    Toast.makeText(getBaseContext(),"No Notes Found",Toast.LENGTH_LONG).show();
                }else {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        teacherList.add(new Teacher(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
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
}