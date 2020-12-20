package com.example.vclasslogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class messagesActivity extends AppCompatActivity {
    RecyclerView rv;
    MyRvMessagesListAdapter adapter;
    ArrayList<message> messages=new ArrayList<message>();
    static int counter=0;
    FirebaseDatabase database;
    DatabaseReference reference;
    String senderID,receiverID;
    EditText sendMessage;
    ImageView sendMessageButton,sendImageButton;
    String receiverPhoto;
    Uri selectedImage=null;
    static int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        messages = new ArrayList<>();
        senderID=getIntent().getStringExtra("senderID");
        receiverID=getIntent().getStringExtra("receiverID");
        receiverPhoto=getIntent().getStringExtra("receiverPhoto");
        sendMessage=(EditText)findViewById(R.id.c_msg);
        sendMessageButton=(ImageView) findViewById(R.id.c_send);
        sendImageButton=(ImageView) findViewById(R.id.c_camera);




        database= FirebaseDatabase.getInstance();
        reference=database.getReference("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message x = snapshot.getValue(message.class);

                if (x.getSenderID().equals(senderID) || x.getReceiverID().equals(senderID)) {
                    if (x.getSenderID().equals(receiverID) || x.getReceiverID().equals(receiverID)) {
                        messages.add(
                                snapshot.getValue(message.class)
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

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.O)
             @Override
             public void onClick(View view) {
                 reference.push().setValue(new message(
                         receiverID,
                         senderID,
                         sendMessage.getText().toString().trim(),
                         LocalDateTime.now().toString(),
                         false

                 ));
                 sendMessage.setText("");
//                 adapter.notifyDataSetChanged();

                 reference.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(getApplicationContext(),snapshot.getValue(String.class),Toast.LENGTH_LONG).show();
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });



             };
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,100);
                if(selectedImage!=null){
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                    storageReference=storageReference.child("messagePhotos/photos"+count+".jpg");
                    count++;
                    storageReference.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> task=taskSnapshot.getStorage().getDownloadUrl();
                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            message msg=new message("","","","",true);
                                            msg.setMessage(uri.toString());

                                            reference.push().setValue(new message(
                                                    receiverID,
                                                    senderID,
                                                    msg.getMessage(),
                                                    LocalDateTime.now().toString(),
                                                    true

                                            ));
                                            adapter.notifyDataSetChanged();

                                        }
                                    })               .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(
                                                    messagesActivity.this,
                                                    "Failed to upload picture",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(
                                            messagesActivity.this,
                                            "Failed to upload picture neechay",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            })
                    ;
                                }
                            }






        });

        rv = findViewById(R.id.c_rcv_msg_list);
                 // add=findViewById(R.id.address);
                 RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                 rv.setLayoutManager(manager);
                 Collections.sort(messages, new Comparator<message>() {
                     @Override
                     public int compare(message message, message t1) {
                         if(message.getTime()==null || t1.getTime()==null)
                             return 0;
                         return message.getTime().compareTo(t1.getTime());
                     }
                 });
                 adapter = new MyRvMessagesListAdapter(getApplicationContext(), messages, getIntent().getStringExtra("senderID"),getIntent().getStringExtra("receiverPhoto"));
                 rv.setAdapter(adapter);


         }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            Log.v("HERE","here");
            selectedImage=data.getData();

        }

    }
}