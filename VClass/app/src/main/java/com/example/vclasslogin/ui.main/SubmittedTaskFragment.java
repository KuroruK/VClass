package com.example.vclasslogin.ui.main;

import android.os.Build;
import android.os.Bundle;
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

public class SubmittedTaskFragment extends Fragment {
    String userType;
    String courseName, userName;
    RecyclerView rv;
    MyRvTaskListAdapter adapter;
    ArrayList<ClassTask> tasks = new ArrayList<ClassTask>();
    FirebaseDatabase database;
    DatabaseReference reference;
    View v;
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> studentTaskTitles;

    // constructor which gets and updates its data members with required data
    public SubmittedTaskFragment(String user, String course, String name, ArrayList<String> studentTaskTitles) {
        userType = user;
        courseName = course;
        userName = name;
        this.studentTaskTitles = studentTaskTitles;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_submitted_task, container, false);   // setting layout
        rv = v.findViewById(R.id.rvList_submitted_task);    // recycler view in layout

        // setting layout manager for rv
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);

        // condition to open particular activity when an item is selected from recycler view
        if (userType.equals("teacher"))
            adapter = new MyRvTaskListAdapter(getContext(), tasks, "teacher1", ids, userName, studentTaskTitles);
        else
            adapter = new MyRvTaskListAdapter(getContext(), tasks, "student1", ids, userName, studentTaskTitles);

        // setting adapter for rv
        rv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting firebase instance, then reference and then getting all the tasks whose dates have been passed
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        reference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);

                // setting task date in tempDate
                String tempDate = tem.getDeadline();
                String currentDate = "";
                String cDate = Integer.toString(LocalDateTime.now().getDayOfMonth());
                cDate += "/";
                cDate += Integer.toString(LocalDateTime.now().getMonthValue());
                cDate += "/";
                cDate += Integer.toString(LocalDateTime.now().getYear());

                // conditions to convert date into proper format i.e, dd/mm/yyyy
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

                //  getting current date and compare with the tasks date to check if task's status is ongoing or previous
                Date d1 = null, d2 = null;
                DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                try {
                    d1 = format1.parse(tempDate);
                    d2 = format1.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // to display only teacher's uploaded task
                if (d2.after(d1)) {
                    if (tem.getCourseName().equals(courseName) && tem.getUserType().equals("teacher")) {
                        ids.add(snapshot.getKey());
                        tasks.add(
                                snapshot.getValue(ClassTask.class)
                        );
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

