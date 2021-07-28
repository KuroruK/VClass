package com.example.vclasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.Task;
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

public class EditTaskActivity extends AppCompatActivity {
    static int count = 0;   // add a number at the end of attached file names by increasing 1-by-1
    EditText title, descriptionEdit, deadlineEdit, totalMarksEdit;
    String courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date, id;
    Button attachTask, uploadTask, cancelBtn;
    String fileType = "";
    Uri selectedImage = null;
    FirebaseDatabase database;
    DatabaseReference reference;
    boolean fileChange = false;
    RecyclerView rv;
    MyRvTaskListAdapter adapter;
    ArrayList<ClassTask> tasks = new ArrayList<ClassTask>();
    ArrayList<String> ids = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // actionbar - back button and title
        getSupportActionBar().setTitle("Edit Task");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // getting require data from previous activity
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
        uploadTask = findViewById(R.id.t_et_r_btn);

        // getting firebase instance and reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");

        // getting fields from layout
        title = findViewById(R.id.t_et_title2);
        descriptionEdit = findViewById(R.id.t_et_desc2);
        deadlineEdit = findViewById(R.id.t_et_due2);
        totalMarksEdit = findViewById(R.id.t_et_marks2);
        attachTask = findViewById(R.id.t_et_attach_files);
        rv = findViewById(R.id.t_et_rvList_files);

        // putting text data in layout fields
        title.setText(taskTitle);
        descriptionEdit.setText(description);
        deadlineEdit.setText(deadline);
        totalMarksEdit.setText(totalMarks);

        //  getting count of reference files
        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
            }
        }

        // getting file names attached with a particular task
        String getFiles = null;
        int index;
        ids.add(id + "##" + taskTitle);
        if (noOfFiles > 0) {
            getFiles = file.substring(2);
        }
        String temp;
        for (int i = 0; i < noOfFiles; i++) {
            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            ids.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
        }

        // getting those files and display them in this activity
        for (int i = 0; i < noOfFiles; i++) {

            tasks.add(new ClassTask(
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
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        adapter = new MyRvTaskListAdapter(getApplicationContext(), tasks, "ongoingTeacherEdit", ids, submittedBy, null);
        rv.setAdapter(adapter);

        attachTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // options contains types of files that can be sent
                CharSequence[] options = new CharSequence[]
                        {
                                "Images",
                                "PDF",
                                "PowerPoint",
                                "MS Word File",
                                "Zip file"
                        };

                // builder used to select type of file student wants to send
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
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
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            startActivityForResult(intent, 110);
                        }
                    }
                });
                builder.show();
            }
        });


        uploadTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!fileChange) {
                    // get task info from firebase realtime database
                    reference.addChildEventListener(new ChildEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            ClassTask tem = snapshot.getValue(ClassTask.class);

                            if (snapshot.getKey().equals(getIntent().getStringExtra("id"))) {

                                reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                        title.getText().toString(),
                                        courseName,
                                        descriptionEdit.getText().toString(),
                                        deadlineEdit.getText().toString(),
                                        "teacher",
                                        "-",
                                        totalMarksEdit.getText().toString(),
                                        submittedBy,
                                        tem.file,
                                        LocalDateTime.now().toString(),
                                        "ongoing"
                                ));
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                                previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
                                previousChildName) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }

                    });

                }
                Toast.makeText(getApplicationContext(), "Task successfully modified!", Toast.LENGTH_SHORT).show();

                //go back to list of ongoing task
                Intent intent = new Intent(EditTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("userType", "teacher");
                intent.putExtra("username", submittedBy);
                intent.putExtra("fragment", "ongoing");
                startActivity(intent);
            }
        });

        // button to go back to the last activity when is pressed
        cancelBtn = findViewById(R.id.t_et_b_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTaskActivity.this, TaskActivity.class);
                intent.putExtra("courseName", courseName);
                intent.putExtra("userType", "teacher");
                intent.putExtra("username", submittedBy);
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
                storageReference = storageReference.child("Tasks/images" + count + ".jpg");
                count++;
                fileChange = true;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        file = file + "##" + uri1;
                                        reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                descriptionEdit.getText().toString(),
                                                deadlineEdit.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarksEdit.getText().toString(),
                                                submittedBy,
                                                file,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                        updateRV();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                EditTaskActivity.this,
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
                                        EditTaskActivity.this,
                                        "Failed to upload picture",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("PDF")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/Pdf" + count + ".pdf");
                count++;
                fileChange = true;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        file = file + "##" + uri1;
                                        reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                descriptionEdit.getText().toString(),
                                                deadlineEdit.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarksEdit.getText().toString(),
                                                submittedBy,
                                                file,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                        updateRV();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                EditTaskActivity.this,
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
                                        EditTaskActivity.this,
                                        "Failed to upload pdf",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/PowerPoint" + count + ".ppt");
                count++;
                fileChange = true;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        file = file + "##" + uri1;
                                        reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                descriptionEdit.getText().toString(),
                                                deadlineEdit.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarksEdit.getText().toString(),
                                                submittedBy,
                                                file,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                        updateRV();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                EditTaskActivity.this,
                                                "Failed to powerpoint",
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
                                        EditTaskActivity.this,
                                        "Failed to upload powerpoint",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("Word")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/msWord" + count + ".docx");
                count++;
                fileChange = true;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        file = file + "##" + uri1;
                                        reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                descriptionEdit.getText().toString(),
                                                deadlineEdit.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarksEdit.getText().toString(),
                                                submittedBy,
                                                file,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                        updateRV();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                EditTaskActivity.this,
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
                                        EditTaskActivity.this,
                                        "Failed to upload ms word",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/zip" + count + ".zip");
                count++;
                fileChange = true;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        file = file + "##" + uri1;
                                        reference.child(getIntent().getStringExtra("id")).setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                descriptionEdit.getText().toString(),
                                                deadlineEdit.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarksEdit.getText().toString(),
                                                submittedBy,
                                                file,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                        updateRV();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                EditTaskActivity.this,
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
                                        EditTaskActivity.this,
                                        "Failed to upload zip folder",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            }
        }
    }

    // function to update attached files with the task because no of attached files are changing but not the task
    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateRV() {
        // clearing attached files
        tasks.clear();
        ids.clear();

        // no of files uploaded by teacher as a helping material for task
        int noOfFiles = 0;
        for (int i = 0; i < file.length() - 1; i++) {
            if (file.charAt(i) == '#' && file.charAt(i + 1) == '#') {
                noOfFiles++;
            }
        }

        // getting file names attached with a particular task
        String getFiles = null;
        int index = 0;
        ids.add(id + "##" + taskTitle);
        if (noOfFiles > 0) {
            getFiles = file.substring(2);
        }
        String temp = null;
        for (int i = 0; i < noOfFiles; i++) {

            if (getFiles.contains("##"))
                temp = getFiles.substring(0, getFiles.indexOf('#'));
            else
                temp = getFiles;
            ids.add(temp);
            index = getFiles.indexOf('#') + 2;
            getFiles = getFiles.substring(index);
        }

        // getting those files and display them in this activity
        for (int i = 0; i < noOfFiles; i++) {
            tasks.add(new ClassTask(
                    "File No. " + (i + 1),
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
        adapter.notifyDataSetChanged();
    }

    // method use to go to previous activity when back button is pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), TeacherViewUploadedTaskActivity.class);

        intent.putExtra("courseName", courseName);
        intent.putExtra("taskTitle", taskTitle);
        intent.putExtra("description", description);
        intent.putExtra("deadline", deadline);
        intent.putExtra("obtainedMarks", obtainedMarks);
        intent.putExtra("totalMarks", totalMarks);
        intent.putExtra("submittedBy", submittedBy);
        intent.putExtra("file", file);
        intent.putExtra("date", date);
        intent.putExtra("id", id);

        startActivityForResult(intent, 0);
        return true;
    }
}