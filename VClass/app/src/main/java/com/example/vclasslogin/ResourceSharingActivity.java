package com.example.vclasslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// this activity displays list of all shared resources of a specific course to a teacher
public class ResourceSharingActivity extends AppCompatActivity {
    FloatingActionButton shareBtn;
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";
    RecyclerView rv;
    MyRvResourceListAdapter adapter;
    ArrayList<Resource> resources = new ArrayList<Resource>();
    FirebaseDatabase database;
    DatabaseReference reference;
    String username, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_sharing);

        // action bar
        getSupportActionBar().setTitle("Resources");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        shareBtn = findViewById(R.id.share_resource);
        resources = new ArrayList<>();
        username = getIntent().getStringExtra("username");
        courseName = getIntent().getStringExtra("courseName");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");

        // following code used to get resources from firebase and adding them to resources list
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Resource x = snapshot.getValue(Resource.class);
                if (x.getCourse().equalsIgnoreCase(courseName)) {
                    resources.add(
                            snapshot.getValue(Resource.class)
                    );

                    adapter.notifyDataSetChanged();
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
        rv = findViewById(R.id.rvList_t_resources);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);

        //following code sorts resources in ascending order based on time
        Collections.sort(resources, new Comparator<Resource>() {
            @Override
            public int compare(Resource res, Resource t1) {
                if (res.getTime() == null || t1.getTime() == null)
                    return 0;
                return res.getTime().compareTo(t1.getTime());
            }
        });
        adapter = new MyRvResourceListAdapter(getApplicationContext(), resources, "view", null);
        rv.setAdapter(adapter);


        // following button takes teacher to AttachFileActivity
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttachFileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("courseName", courseName);
                startActivityForResult(intent, 200);
            }
        });
    }

    // following method called when startActivityForResult returns
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // following code adds new resources that have been added by teacher to resource list so it can be displayed as well.
        if (requestCode == 200 && resultCode == RESULT_OK) {
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Resource x = snapshot.getValue(Resource.class);
                    if (x.getCourse().equalsIgnoreCase(courseName)) {
                        resources.add(
                                snapshot.getValue(Resource.class)
                        );
                        adapter.notifyDataSetChanged();
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

    // method used to go to previous activity when back button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LiveTeacherClassActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("courseName", courseName);
        startActivityForResult(myIntent, 0);
        return true;
    }
}