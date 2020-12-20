package com.example.vclasslogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LiveStudentClassActivity extends AppCompatActivity {
    ToggleButton micToggle, voiceToggle;
    ImageView whiteboard;
    public static final String TAG = "AndroidDrawing";
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";
    DBHelper db;

    RecyclerView rv;
    MyRvMessagesListAdapter adapter;
    ArrayList<message> messages = new ArrayList<message>();
    static int counter = 0;
    FirebaseDatabase database;
    DatabaseReference reference;
    //   String senderID,receiverID;
    EditText sendMessage;
    ImageView sendMessageButton, sendImageButton;
    String receiverPhoto;
    Uri selectedImage = null;
    static int count = 0;
    TextView className;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_student_class);
        getSupportActionBar().setTitle("Class");
        ////message
        messages = new ArrayList<>();
        className = findViewById(R.id.live_std_name);
        className.setText(getIntent().getStringExtra("courseName"));
        username = getIntent().getStringExtra("username");

        //  senderID=getIntent().getStringExtra("senderID");
        //      senderID="1";
        //  receiverID=getIntent().getStringExtra("receiverID");
        //      receiverID="2";
        sendMessage = (EditText) findViewById(R.id.c_msg);
        sendMessageButton = (ImageView) findViewById(R.id.c_send);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message x = snapshot.getValue(message.class);
                Log.v("msgmsg", "72");
                if (x.getSenderID().equals(getIntent().getStringExtra("courseName")) || x.getReceiverID().equals(getIntent().getStringExtra("courseName"))) {
                    messages.add(
                            snapshot.getValue(message.class)
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

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.v("msgmsg", "109");
                reference.push().setValue(new message(
                        getIntent().getStringExtra("courseName"),
                        username,
                        sendMessage.getText().toString().trim(),
                        LocalDateTime.now().toString(),
                        false

                ));
                sendMessage.setText("");
                adapter.notifyDataSetChanged();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(getApplicationContext(),snapshot.getValue(String.class),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            ;
        });
        rv = findViewById(R.id.c_rcv_msg_list);
        // add=findViewById(R.id.address);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        Collections.sort(messages, new Comparator<message>() {
            @Override
            public int compare(message message, message t1) {
                if (message.getTime() == null || t1.getTime() == null)
                    return 0;
                return message.getTime().compareTo(t1.getTime());
            }
        });
        adapter = new MyRvMessagesListAdapter(getApplicationContext(), messages, username, " ");
        rv.setAdapter(adapter);

////////////


        db = new DBHelper(getApplicationContext());
        //db.setClassDetailsTable();

        //micToggle = findViewById(R.id.live_std_mic);
        voiceToggle = findViewById(R.id.live_std_voice2);
        whiteboard = findViewById(R.id.live_std_whiteboard);


        whiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = db.getWhiteboardIDFromClassTitle(getIntent().getStringExtra("courseName"));//firebase boardmeta key/id
                Log.i(TAG, "Opening board " + key);
                Toast.makeText(LiveStudentClassActivity.this, "Opening board: " + key, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LiveStudentClassActivity.this, DrawingActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("FIREBASE_URL", FIREBASE_URL);
                intent.putExtra("BOARD_ID", key);
                intent.putExtra("user_type", "student");
                startActivity(intent);
            }
        });
    }

    public void micToggleClick(View view) {
        if (micToggle.isChecked()) {
            //micToggle.setChecked(false);
            Toast.makeText(this, "Mic muted!", Toast.LENGTH_SHORT).show();
        } else {
            //micToggle.setChecked(true);
            Toast.makeText(this, "Mic unmuted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void voiceToggleClick(View view) {
        if (voiceToggle.isChecked()) {
            Toast.makeText(this, "Class volume on!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Class volume off!", Toast.LENGTH_SHORT).show();
        }
    }
}