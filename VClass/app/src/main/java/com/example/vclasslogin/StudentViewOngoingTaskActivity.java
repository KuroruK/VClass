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
    int count = 0;
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

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        setContentView(R.layout.activity_view_task_student);

        // back button - action bar
        getSupportActionBar().setTitle(taskTitle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.task_submit_std_title);
        title.setText(taskTitle);
        desc = findViewById(R.id.task_submit_std_desc2);
        desc.setText(description);
        dueDate = findViewById(R.id.task_submit_std_due2);
        dueDate.setText(deadline);
        tMarks = findViewById(R.id.task_submit_std_marks2);
        tMarks.setText(totalMarks);
        uploadFile = findViewById(R.id.task_submit_attach_file);
        reSubmit = findViewById(R.id.task_submit_std_r_btn);
        back = findViewById(R.id.task_submit_std_b_btn);

        if (alreadySubmitted.equals("0"))
            reSubmit.setText("Submit");
        else
            reSubmit.setText("Resubmit");

        rv1 = findViewById(R.id.rvList_files);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ClassTask tem = snapshot.getValue(ClassTask.class);
                Log.v("tagid", tem.getTaskTitle() + ";;;" + taskTitle);
                Log.v("tagid", tem.getSubmittedBy() + ":::" + userName);
                if (tem.getTaskTitle().equals(taskTitle) && tem.getSubmittedBy().equals(userName)) {
                    ids1.add(snapshot.getKey());
                    count++;
                    tem.taskTitle = taskTitle + "_" + userName + "_" + count;
                    Log.v("tagid", "1");
                    tasks1.add(
                            tem
                            //  snapshot.getValue(ClassTask.class)
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


        //teacher files

        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
                Log.v("check updatingNoOfIfles", "yup");
            }
        }
        Log.v("check nooffiles", Integer.toString(noOfFiles));
        String getFiles = null;
        int index = 0;
        ids2.add(id);
        if (noOfFiles > 0) {
            getFiles = file.substring(2, file.length());
        }
        String temp = null;
        for (int i = 0; i < noOfFiles; i++) {

            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            Log.v("check temp", temp);
            ids2.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
            //      Log.v("check getFiles",getFiles);
        }
        for (int i = 0; i < ids2.size(); i++) {
            Log.v("check ids", ids2.get(i));
        }

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


        rv2 = findViewById(R.id.rvList_task_files);
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(manager2);
        Log.v("tagid", "0");
        adapter2 = new MyRvTaskListAdapter(getApplicationContext(), tasks2, "ongoingTeacherView", ids2, submittedBy, null);
        rv2.setAdapter(adapter2);


/////////////////////////////////////////////////////////////////////////////////////////

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(getApplicationContext(), AttachFileActivity.class);

                intent.putExtra("courseName", courseName);
                intent.putExtra("taskTitle", taskTitle);
                intent.putExtra("description", description);
                intent.putExtra("deadline", deadline);
                intent.putExtra("obtainedMarks", obtainedMarks);
                intent.putExtra("totalMarks", totalMarks);
                intent.putExtra("submittedBy", submittedBy);
                intent.putExtra("file", file);
                intent.putExtra("date", date);

                startActivity(intent);*/

                //code here - neeche wala kaam AttachFileActivity.java me kiya hai
                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF",
                                "PowerPoint",
                                "MS Word File",
                                "Zip file"
                        };
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
                            //intent.setType("*/*");
                            startActivityForResult(intent, 110);
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
                            startActivityForResult(intent, 110);
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
                            startActivityForResult(intent, 110);
                        }
                    }
                });
                builder.show();
            }
        });


        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv1.setLayoutManager(manager);
        Log.v("tagid", "0");
        adapter1 = new MyRvTaskListAdapter(getApplicationContext(), tasks1, "ongoingStudent", ids1, userName, null);
        rv1.setAdapter(adapter1);


        reSubmit.setOnClickListener(new View.OnClickListener() {
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


    //code here - neeche wala kaam bhi AttachFileActivity.java me kiya hai
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
