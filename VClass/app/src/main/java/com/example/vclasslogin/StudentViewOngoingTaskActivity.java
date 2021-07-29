package com.example.vclasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class StudentViewOngoingTaskActivity extends AppCompatActivity {
    String courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date, id, alreadySubmitted;
    Button uploadFile, reSubmit, back;
    String fileType = "";
    Uri selectedImage = null;
    int count = 0;  // add a number at the end of attached file names by increasing 1-by-1
    String userName;
    TextView title, desc, dueDate, tMarks;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView rv1, rv2;
    MyRvTaskListAdapter adapter1, adapter2;
    ArrayList<ClassTask> tasks1 = new ArrayList<ClassTask>();
    ArrayList<ClassTask> tasks2 = new ArrayList<ClassTask>();
    ArrayList<String> ids1 = new ArrayList<String>();
    ArrayList<String> ids2 = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_student);

        // actionbar - back button and title
        getSupportActionBar().setTitle(taskTitle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // getting require data from previous activity
        userName = getIntent().getStringExtra("userName");
        courseName = getIntent().getStringExtra("courseName");
        taskTitle = getIntent().getStringExtra("taskTitle");
        description = getIntent().getStringExtra("description");
        deadline = getIntent().getStringExtra("deadline");
        obtainedMarks = getIntent().getStringExtra("obtainedMarks");
        totalMarks = getIntent().getStringExtra("totalMarks");
        submittedBy = getIntent().getStringExtra("submittedBy");
        file = getIntent().getStringExtra("file");
        date = getIntent().getStringExtra("date");
        id = getIntent().getStringExtra("id");
        alreadySubmitted = getIntent().getStringExtra("alreadySubmitted");

        // getting firebase instance and reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");

        // getting fields from layout
        title = findViewById(R.id.task_submit_std_title);
        desc = findViewById(R.id.task_submit_std_desc2);
        dueDate = findViewById(R.id.task_submit_std_due2);
        tMarks = findViewById(R.id.task_submit_std_marks2);
        uploadFile = findViewById(R.id.task_submit_attach_file);
        reSubmit = findViewById(R.id.task_submit_std_r_btn);
        back = findViewById(R.id.task_submit_std_b_btn);
        rv1 = findViewById(R.id.rvList_files);
        rv2 = findViewById(R.id.rvList_task_files);

        // putting text data in layout fields
        title.setText(taskTitle);
        desc.setText(description);
        dueDate.setText(deadline);
        tMarks.setText(totalMarks);

        // change button text if task is submitted before
        if (alreadySubmitted.equals("0"))
            reSubmit.setText("Submit");
        else
            reSubmit.setText("Resubmit");

        //  getting particular task details from firebase
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);
                if (tem.getTaskTitle().equals(taskTitle) && tem.getSubmittedBy().equals(userName)) {
                    ids1.add(snapshot.getKey());
                    count++;
                    tem.taskTitle = taskTitle + "_" + userName + "_" + count;
                    tasks1.add(
                            tem
                    );
                    adapter1.notifyDataSetChanged();
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

        // no of files upload by teacher as a helping material for task
        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
            }
        }

        // getting file names attached with a particular task
        String getFiles = null;
        int index = 0;
        ids2.add(id);
        if (noOfFiles > 0) {
            getFiles = file.substring(2);
        }
        String temp;
        for (int i = 0; i < noOfFiles; i++) {

            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            ids2.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
        }

        // getting those files and display them in this activity
        for (int i = 0; i < noOfFiles; i++) {
            tasks2.add(new ClassTask(
                    "File No. " + Integer.toString(i + 1),
                    courseName,
                    description,
                    deadline,
                    "teacher",
                    "-",
                    totalMarks,
                    submittedBy,
                    file,
                    LocalDateTime.now().toString(),
                    "ongoing"
            ));
        }

        // setting layout manager and adapter for rv
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(manager2);
        adapter2 = new MyRvTaskListAdapter(getApplicationContext(), tasks2, "ongoingTeacherView", ids2, submittedBy, null);
        rv2.setAdapter(adapter2);

        // button to upload files
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentViewOngoingTaskActivity.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            fileType = "Images";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 110);

                        }
                        if (i == 1) {
                            fileType = "PDF";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            //intent.setType("*/*");
                            startActivityForResult(intent, 110);
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
                            startActivityForResult(intent, 110);
                        }
                        if (i == 3) {
                            fileType = "Word";

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                            startActivityForResult(intent, 110);
                        }
                        if (i == 4) {
                            fileType = "zip";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            String[] mimeTypes = {"application/zip"};
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            startActivityForResult(intent, 110);
                        }
                    }
                });
                builder.show();
            }
        });

        // setting layout manager and adapter for rv
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv1.setLayoutManager(manager);
        adapter1 = new MyRvTaskListAdapter(getApplicationContext(), tasks1, "ongoingStudent", ids1, userName, null);
        rv1.setAdapter(adapter1);

        reSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to submit task by uploading files in it
                Intent intent = new Intent(StudentViewOngoingTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "student");
                intent.putExtra("username", userName);
                intent.putExtra("fragment", "ongoing");
                startActivity(intent);
            }
        });

        // to go to the last opened activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StudentViewOngoingTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", getIntent().getStringExtra("courseName"));
                intent.putExtra("userType", "student");
                intent.putExtra("username", userName);
                intent.putExtra("fragment", "ongoing");
                startActivity(intent);
            }
        });


    }

    /*following code used to store file/resource in firebase storage and then linking a reference to that file
    in a message that is stored in realtime firebase so that it can be shared with others in almost real time*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110 && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            if (fileType.equals("Images")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/images" + userName + "/" + taskTitle + ids1.size() + ".jpg");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                com.google.android.gms.tasks.Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),// taskTitle_username_Count.fileType
                                                courseName,
                                                description,
                                                deadline,
                                                "student",
                                                "-",
                                                totalMarks,
                                                userName,
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                StudentViewOngoingTaskActivity.this,
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
                                        StudentViewOngoingTaskActivity.this,
                                        "Failed to upload picture neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("PDF")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/Pdf" + userName + "/" + taskTitle + ids1.size() + ".pdf");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                com.google.android.gms.tasks.Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description,
                                                deadline,
                                                "student",
                                                "-",
                                                totalMarks,
                                                userName,
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                StudentViewOngoingTaskActivity.this,
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
                                        StudentViewOngoingTaskActivity.this,
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/PowerPoint" + userName + "/" + taskTitle + ids1.size() + ".ppt");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                com.google.android.gms.tasks.Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description,
                                                deadline,
                                                "student",
                                                "-",
                                                totalMarks,
                                                userName,
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                StudentViewOngoingTaskActivity.this,
                                                "Failed to ppt",
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
                                        StudentViewOngoingTaskActivity.this,
                                        "Failed to upload ppt neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("Word")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/msWord" + userName + "/" + taskTitle + ids1.size() + ".docx");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                com.google.android.gms.tasks.Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description,
                                                deadline,
                                                "student",
                                                "-",
                                                totalMarks,
                                                userName,
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                StudentViewOngoingTaskActivity.this,
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
                                        StudentViewOngoingTaskActivity.this,
                                        "Failed to upload msword neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/zip" + userName + "/" + taskTitle + ids1.size() + ".zip");
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                com.google.android.gms.tasks.Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description,
                                                deadline,
                                                "student",
                                                "-",
                                                totalMarks,
                                                userName,
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                StudentViewOngoingTaskActivity.this,
                                                "Failed to upload zip",
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
                                        StudentViewOngoingTaskActivity.this,
                                        "Failed to upload zip neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }
        }
    }

    // method use to go to previous activity when back button is pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TaskActivity.class);
        myIntent.putExtra("username", userName);
        myIntent.putExtra("courseName", courseName);
        myIntent.putExtra("userType", "student");
        myIntent.putExtra("fragment", "ongoing");
        startActivityForResult(myIntent, 0);
        return true;
    }
}
