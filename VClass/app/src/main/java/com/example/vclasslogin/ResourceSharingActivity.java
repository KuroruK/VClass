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

public class ResourceSharingActivity extends AppCompatActivity {
    //String userType;
    FloatingActionButton shareBtn;
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";
    RecyclerView rv;
    MyRvResourceListAdapter adapter;
    ArrayList<Resource> resources = new ArrayList<Resource>();
    static int counter = 0;
    FirebaseDatabase database;
    DatabaseReference reference;
    String username, courseName;//, userType;

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
        //userType = getIntent().getStringExtra("userType");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Resource x = snapshot.getValue(Resource.class);
                resources.add(
                        snapshot.getValue(Resource.class)
                );

                adapter.notifyDataSetChanged();

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


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttachFileActivity.class);
                Log.v("myLogRSA", username);
                intent.putExtra("username", username);
                intent.putExtra("courseName", courseName);
                startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Resource x = snapshot.getValue(Resource.class);
                    resources.add(
                            snapshot.getValue(Resource.class)
                    );
                    adapter.notifyDataSetChanged();

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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), LiveTeacherClassActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("courseName", courseName);
        startActivityForResult(myIntent, 0);
        return true;
    }
}