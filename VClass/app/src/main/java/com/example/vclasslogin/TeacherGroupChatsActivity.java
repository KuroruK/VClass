package com.example.vclasslogin;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherGroupChatsActivity extends AppCompatActivity {
//    AutoCompleteAdapter autoCompleteAdapter;
    RecyclerView rv;
    TextView add;
    MyRvListGroupChatsAdapter adapter;
    ArrayList<String> groupLists=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;
    CircleImageView viewProfilePhoto;
    ImageView menu;
    AutoCompleteTextView search;



    String username;
    int teacherID;
    ArrayList<String> courses;
    ArrayList<Integer> coursesIDs;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_group_chats);
        groupLists = new ArrayList<>();
        getSupportActionBar().setTitle("Chat App");
        //menu=(ImageView)findViewById(R.id.ac_menu);


        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("name");
        courses = new ArrayList<String>();
        coursesIDs = new ArrayList<Integer>();
        rv = findViewById(R.id.ac_rcv);

        teacherID = dbHelper.getTeacherID(username);
        coursesIDs = dbHelper.getTeacherCourseIDs(teacherID);
        for (int i = 0; i < coursesIDs.size(); ++i) {
            groupLists.add(dbHelper.getCourseName(coursesIDs.get(i)));
            Log.v("teacher_", dbHelper.getCourseName(coursesIDs.get(i)));
        }


        // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter=new MyRvListGroupChatsAdapter(this,groupLists,getIntent().getStringExtra("name"));
        rv.setAdapter(adapter);



        //----------search

       /* SearchAdapter searchAdapter=new SearchAdapter(this,R.layout.activity_contacts,R.id.rac_name,contactLists);
        search.setAdapter(searchAdapter);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(view.getContext(),messagesActivity.class);
                Profile x=(Profile) adapterView.getItemAtPosition(i);
                intent.putExtra("receiverID",x.getId());
                intent.putExtra("receiverPhoto",x.getDp());

                intent.putExtra("senderID",getIntent().getStringExtra("id"));

                startActivity(intent);

            }
        });

        */

    }
    void searchArray(){
        //search=(AutoCompleteTextView)findViewById(R.id.ac_search2);
 /*       autoCompleteAdapter=new AutoCompleteAdapter(this,contactLists);
        search.setAdapter(autoCompleteAdapter);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(view.getContext(),messagesActivity.class);
                intent.putExtra("receiverID",contactLists.get(i).getId());

                intent.putExtra("receiverPhoto",contactLists.get(i).getDp());

                intent.putExtra("senderID",getIntent().getStringExtra("id"));

                startActivity(intent);

            }
        });
*/
    }

}