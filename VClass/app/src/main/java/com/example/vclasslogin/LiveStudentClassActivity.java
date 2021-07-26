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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class LiveStudentClassActivity extends AppCompatActivity {
    //ToggleButton micToggle, voiceToggle;
    AppCompatButton taskBtn, resourcesBtn, attendanceBtn;
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
    ImageView sendMessageButton, stdShareBtn;
    String receiverPhoto;
    Uri selectedImage = null;
    static int count = 1;
    TextView className;
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

        ////message
        messages = new ArrayList<>();
        //className = findViewById(R.id.live_std_name);
        //className.setText(getIntent().getStringExtra("courseName"));
        username = getIntent().getStringExtra("username");


        //  senderID=getIntent().getStringExtra("senderID");
        //      senderID="1";
        //  receiverID=getIntent().getStringExtra("receiverID");
        //      receiverID="2";
        sendMessage = (EditText) findViewById(R.id.c_msg);
        sendMessageButton = (ImageView) findViewById(R.id.c_send);
        stdShareBtn = findViewById(R.id.c_share);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message x = snapshot.getValue(message.class);
                Log.v("msgmsg", "72");
                if (x.getSenderID().equals(getIntent().getStringExtra("courseName")) || x.getReceiverID().equals(getIntent().getStringExtra("courseName"))) {
                    if (x.getResourceType().equalsIgnoreCase("pdf") || x.getResourceType().equalsIgnoreCase("msword")
                            || x.getResourceType().equalsIgnoreCase("ppt") || x.getResourceType().equalsIgnoreCase("zip")) {
                        //x.message = x.message+ "##" + count;
                        count++;
                    }
                    messages.add(
                            snapshot.getValue(message.class)
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

                        false,
                        "",
                        ""

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
        //voiceToggle = findViewById(R.id.live_std_voice2);
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

        // buttons
        taskBtn = findViewById(R.id.live_std_tasks);
        resourcesBtn = findViewById(R.id.live_std_resources);
        attendanceBtn = findViewById(R.id.live_std_attendance);

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

        stdShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF",
                                "PowerPoint",
                                "MS Word File",
                                "Zip file"
                        };
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
                            //intent.setType("*/*");
                            startActivityForResult(intent, 100);
                        }
                        if (i == 2) {
                            fileType = "PPT";
                            String[] mimeTypes =
                                    {"application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx

                                    };
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            //intent.setType("*/*");
                            startActivityForResult(intent, 100);
                        }
                        if (i == 3) {
                            fileType = "Word";
                            //    Intent intent = new Intent();
                            //    intent.setAction(Intent.ACTION_GET_CONTENT);
                            //    intent.setType("application/msword");
                            //    intent.setType("docx/*");
                            // intent.setType("application/docx");

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            // intent.addCategory(Intent.CATEGORY_);
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
                            /*String[] mimeTypes =
                                    {"application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                            "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                            "text/plain",
                                            "application/zip"};

                             */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.v("HERE", "here");
            selectedImage = data.getData();
            Log.v("HERE", selectedImage.toString());
            Log.v("HERE", selectedImage.getPath());
            Log.v("HERE", selectedImage.getLastPathSegment());
            Log.v("HERE", selectedImage.getPathSegments().toString());
            Log.v("HERE", data.getDataString());


            if (fileType.equals("Images")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://vclass-47776.appspot.com");
                storageReference = storageReference.child("Messages/images" + count + ".jpg");
                //count++;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        message msg = new message("", "", "", "", true, "images", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new message(
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
                                        "Failed to upload picture neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PDF")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/pdf" + count + ".pdf");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        message msg = new message("", "", "", "", true, "pdf", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "pdf", "student"
                                        ));
                                        //count++;
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
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/powerpoint" + count + ".ppt");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        message msg = new message("", "", "", "", true, "ppt", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "ppt", "student"

                                        ));
                                        //count++;
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
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("Word")) {
                Log.v("check word", "1");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/msWord" + count + ".docx");
                Log.v("check word", "2");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.v("check word", "3");
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.v("check word", "4");
                                        message msg = new message("", "", "", "", true, "msword", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);
                                        Log.v("check word", "5 " + msg);
                                        reference.push().setValue(new message(
                                                getIntent().getStringExtra("courseName"),
                                                username,
                                                msg.getMessage(),
                                                LocalDateTime.now().toString(),
                                                true, "msword", "student"

                                        ));
                                        Log.v("check word", "6");
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
                                        "Failed to upload msword neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Messages/zip" + count + ".zip");
                //count++;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        message msg = new message("", "", "", "", true, "zip", "teacher");
                                        msg.setMessage(uri.toString() + "##" + count);

                                        reference.push().setValue(new message(
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
                                        "Failed to upload other neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            }
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), StudentClassesActivity.class);
        myIntent.putExtra("name", username);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
