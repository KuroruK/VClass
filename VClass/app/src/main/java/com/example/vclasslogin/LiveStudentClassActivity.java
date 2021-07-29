package com.example.vclasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.vclasstestv2.vrVclass.UnityPlayerActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

// class hub - student side
public class LiveStudentClassActivity extends AppCompatActivity {
    AppCompatButton taskBtn, resourcesBtn, VRButton;
    ImageView whiteboard;
    public static final String TAG = "AndroidDrawing";
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";
    DBHelper db;

    RecyclerView rv;
    MyRvMessagesListAdapter adapter;
    ArrayList<Message> messages = new ArrayList<Message>();
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText sendMessage;
    ImageView sendMessageButton, stdShareBtn;
    Uri selectedFile = null;
    static int count = 1;
    String username;
    String fileType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_student_class);

        // action bar
        getSupportActionBar().setTitle(getIntent().getStringExtra("courseName"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        messages = new ArrayList<>();
        username = getIntent().getStringExtra("username");


        VRButton = findViewById(R.id.live_std_VRMode);
        // clicking on this button opens VR Classroom - student side
        VRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LiveStudentClassActivity.this, UnityPlayerActivity.class);
                intent.putExtra("arguments","student");
                intent.putExtra("sessionName ",getIntent().getStringExtra("courseName"));
                startActivity(intent);

            }
        });


        sendMessage = (EditText) findViewById(R.id.c_msg);
        sendMessageButton = (ImageView) findViewById(R.id.c_send);
        stdShareBtn = findViewById(R.id.c_share);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("messages");

        // following code is used to get messages from firebase which are placed in messages list.
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message x = snapshot.getValue(Message.class);
                // checking if message belongs to this class
                if (x.getSenderID().equals(getIntent().getStringExtra("courseName")) || x.getReceiverID().equals(getIntent().getStringExtra("courseName"))) {

                    // incrementing count if message is a resource
                    if (x.getResourceType().equalsIgnoreCase("pdf") || x.getResourceType().equalsIgnoreCase("msword")
                            || x.getResourceType().equalsIgnoreCase("ppt") || x.getResourceType().equalsIgnoreCase("zip")) {
                        count++;
                    }
                    messages.add(
                            snapshot.getValue(Message.class)
                    );
                    rv.smoothScrollToPosition(Objects.requireNonNull(rv.getAdapter()).getItemCount() - 1);
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

        // following code used to send text message by pushing a message object to firebase so others can see in near realtime
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                reference.push().setValue(new Message(
                        getIntent().getStringExtra("courseName"),
                        username,
                        sendMessage.getText().toString().trim(),
                        LocalDateTime.now().toString(),
                        false,
                        "",
                        ""

                ));
                sendMessage.setText("");
                adapter.notifyDataSetChanged();
            };
        });

        rv = findViewById(R.id.c_rcv_msg_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);

        // following code sorts messages according to time - in ascending order
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                if (message.getTime() == null || t1.getTime() == null)
                    return 0;
                return message.getTime().compareTo(t1.getTime());
            }
        });

        adapter = new MyRvMessagesListAdapter(getApplicationContext(), messages, username, " ");
        rv.setAdapter(adapter);

        db = new DBHelper(getApplicationContext());
        whiteboard = findViewById(R.id.live_std_whiteboard);

        // clicking on button opens class whiteboard
        whiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = db.getWhiteboardIDFromClassTitle(getIntent().getStringExtra("courseName"));//firebase boardmeta key/id
                Toast.makeText(LiveStudentClassActivity.this, "Opening board: " + key, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LiveStudentClassActivity.this, DrawingActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("FIREBASE_URL", FIREBASE_URL);
                intent.putExtra("BOARD_ID", key);
                intent.putExtra("user_type", "student");
                startActivity(intent);
            }
        });



        taskBtn = findViewById(R.id.live_std_tasks);
        resourcesBtn = findViewById(R.id.live_std_resources);


        // clicking on button opens activity showing list of tasks - ongoing and finished
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveStudentClassActivity.this, TaskActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "student");
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("fragment", "ongoing");
                startActivity(intent);
            }
        });


        // clicking on resource button opens activity showing list of resources shared by teachers
        resourcesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveStudentClassActivity.this, ViewSharedResourcesActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "student");
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });


        // this button is used to send files
        stdShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // options contains types of files that can be sent
                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF",
                                "PowerPoint",
                                "MS Word File",
                                "Zip file"
                        };

                // builder used to select type of file student wants to send
                AlertDialog.Builder builder = new AlertDialog.Builder(LiveStudentClassActivity.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            fileType = "Images";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 100);

                        }
                        if (i == 1) {
                            fileType = "PDF";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 100);
                        }
                        if (i == 2) {
                            fileType = "PPT";
                            String[] mimeTypes =
                                    {"application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation" // .ppt & .pptx

                                    };
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            startActivityForResult(intent, 100);
                        }
                        if (i == 3) {
                            fileType = "Word";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                            startActivityForResult(intent, 100);
                        }
                        if (i == 4) {
                            fileType = "zip";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            String[] mimeTypes = {"application/zip"};
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            startActivityForResult(intent, 100);
                        }
                    }
                });
                builder.show();
            }
        });
    }


    // following method called when result returns from startActivityForResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //following code used to store file/resource in firebase storage and then linking a reference to that file in a message that is stored in
        // realtime firebase so that it can be shared with others in almost real time
        if (requestCode == 100 && resultCode == RESULT_OK) {
            selectedFile = data.getData();//selectedFile contains resource data that was selected by student to send in chat
            if (fileType.equals("Images")) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://vclass-47776.appspot.com");
                storageReference = storageReference.child("Messages/images" + count + ".jpg");
                storageReference.putFile(selectedFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message msg = new Message("", "", "", "", true, "images", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new Message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "images", "student"

                                        ));
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                LiveStudentClassActivity.this,
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
                                        LiveStudentClassActivity.this,
                                        "Failed to upload picture  ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PDF")) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/pdf" + count + ".pdf");
                storageReference.putFile(selectedFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message msg = new Message("", "", "", "", true, "pdf", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new Message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "pdf", "student"
                                        ));
                                        adapter.notifyDataSetChanged();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                LiveStudentClassActivity.this,
                                                "Failed to pdf picture",
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
                                        LiveStudentClassActivity.this,
                                        "Failed to upload pdf  ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/powerpoint" + count + ".ppt");
                storageReference.putFile(selectedFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message msg = new Message("", "", "", "", true, "ppt", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new Message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "ppt", "student"

                                        ));
                                        adapter.notifyDataSetChanged();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                LiveStudentClassActivity.this,
                                                "Failed to pdf picture",
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
                                        LiveStudentClassActivity.this,
                                        "Failed to upload pdf  ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("Word")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/msWord" + count + ".docx");
                storageReference.putFile(selectedFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message msg = new Message("", "", "", "", true, "msword", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);
                                        reference.push().setValue(new Message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "msword", "student"

                                        ));
                                        adapter.notifyDataSetChanged();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                LiveStudentClassActivity.this,
                                                "Failed to upload msword",
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
                                        LiveStudentClassActivity.this,
                                        "Failed to upload msword  ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/zip" + count + ".zip");
                storageReference.putFile(selectedFile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message msg = new Message("", "", "", "", true, "zip", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new Message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "zip", "student"


                                        ));
                                        adapter.notifyDataSetChanged();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                LiveStudentClassActivity.this,
                                                "Failed to upload other",
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
                                        LiveStudentClassActivity.this,
                                        "Failed to upload other  ",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            }
        }
    }


    // method used to go to previous activity when back button pressed.
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), StudentClassesActivity.class);
        myIntent.putExtra("name", username);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
