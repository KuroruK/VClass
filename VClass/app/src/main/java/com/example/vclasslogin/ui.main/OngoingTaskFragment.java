package com.example.vclasslogin.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vclasslogin.ClassTask;
import com.example.vclasslogin.MyRvTaskListAdapter;
import com.example.vclasslogin.R;
import com.example.vclasslogin.UploadTaskActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OngoingTaskFragment extends Fragment {
    FloatingActionButton addTask;
    String userType;// = "student";
    String courseName, userName;
    RecyclerView rv;
    MyRvTaskListAdapter adapter;
    ArrayList<ClassTask> tasks = new ArrayList<ClassTask>();
    FirebaseDatabase database;
    DatabaseReference reference;
    View v;
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> studentTaskTitles = new ArrayList<>();


    int i = 0;

    public OngoingTaskFragment(String user, String course, String name, ArrayList<String> studentTaskTitles) {
        userType = user;
        courseName = course;
        userName = name;
        this.studentTaskTitles = studentTaskTitles;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (userType.equals("teacher")) {
            v = inflater.inflate(R.layout.fragment_ongoing_task_teacher, container, false);
            addTask = v.findViewById(R.id.add_task);
            addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UploadTaskActivity.class);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }
            });

            rv = v.findViewById(R.id.rvList_ongoing_task_teacher);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            rv.setLayoutManager(manager);
            Log.v("tagid", "0");
            adapter = new MyRvTaskListAdapter(getContext(), tasks, "teacher0", ids, userName, studentTaskTitles);
            rv.setAdapter(adapter);
            return v;

        } else {
            v = inflater.inflate(R.layout.fragment_ongoing_task_student, container, false);
            rv = v.findViewById(R.id.rvList_ongoing_task_student);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            rv.setLayoutManager(manager);
            Log.v("tagid", "0");
            adapter = new MyRvTaskListAdapter(getContext(), tasks, "student0", ids, userName, studentTaskTitles);
            rv.setAdapter(adapter);
            return v;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        //    if(userType.equals("teacher")){
        reference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);
                Log.v("tagid", tem.getCourseName() + "......" + courseName);


                String tempDate = tem.getDeadline();
                String currentDate = "";
                String cDate = Integer.toString(LocalDateTime.now().getDayOfMonth());
                cDate += "/";
                cDate += Integer.toString(LocalDateTime.now().getMonthValue());
                cDate += "/";
                cDate += Integer.toString(LocalDateTime.now().getYear());
                if (cDate.charAt(1) == '/') {
                    currentDate = '0' + cDate;
                } else {
                    currentDate = cDate;
                }
                cDate = currentDate;

                if (cDate.charAt(4) == '/') {
                    currentDate = '0' + cDate;
                } else {
                    currentDate = cDate;
                }


                Date d1 = null, d2 = null;
                DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                try {
                    d1 = format1.parse(tempDate);
                    d2 = format1.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!(d2.after(d1))) {


                    if (tem.getCourseName().equals(courseName) && tem.getUserType().equals("teacher")) {

                        ids.add(snapshot.getKey());

                        Log.v("tagid", "1");
                        tasks.add(
                                snapshot.getValue(ClassTask.class)
                        );
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

}

