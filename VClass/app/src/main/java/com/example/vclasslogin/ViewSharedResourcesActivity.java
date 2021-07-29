package com.example.vclasslogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// this activity displays list of all shared resources of a specific course to a student
public class ViewSharedResourcesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_view_shared_resources);

        // action bar
        getSupportActionBar().setTitle("Resources");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        rv = findViewById(R.id.rvList_s_resources);
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
        adapter = new MyRvResourceListAdapter(getApplicationContext(), resources, "view", "");
        rv.setAdapter(adapter);

    }

    // method used to go to previous activity when back button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LiveStudentClassActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("courseName", courseName);
        startActivityForResult(myIntent, 0);
        return true;
    }
}