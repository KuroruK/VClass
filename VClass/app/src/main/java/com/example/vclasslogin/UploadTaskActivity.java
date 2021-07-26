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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UploadTaskActivity extends AppCompatActivity {
    EditText title, description, deadline, totalMarks;
    String courseName, teacherName;
    Button attachTask, uploadTask;
    String fileType = "";
    Uri selectedImage = null;
    static int count = 0;
    FirebaseDatabase database;
    DatabaseReference reference;

    ArrayList<ClassTask> refFiles;
    String id;
    ArrayList<String> ids = new ArrayList<String>();
    RecyclerView rv;
    MyRvTaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        courseName = getIntent().getStringExtra("courseName");
        teacherName = getIntent().getStringExtra("username");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        title = findViewById(R.id.task_name);
        description = findViewById(R.id.task_details);
        deadline = findViewById(R.id.task_due_date);
        totalMarks = findViewById(R.id.task_marks);
        attachTask = findViewById(R.id.task_attach_file);

        // back button - action bar
        getSupportActionBar().setTitle("New Task");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        attachTask.setOnClickListener(new View.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadTaskActivity.this);
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

        uploadTask = findViewById(R.id.btn_upload_task);
        uploadTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void fileAdapter(String id) {
        //Log.v("check id", id);
        //Log.v("check resource", String.valueOf(resources.size()));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        ArrayList<String> temp = new ArrayList<String>();
        adapter = new MyRvTaskListAdapter(getApplicationContext(), refFiles, "ongoingStudent", ids, teacherName, temp);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110 && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            if (fileType.equals("Images")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/images" + count + ".jpg");
                count++;
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
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));

                                        /*ClassTask ct = new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        );

                                        id = reference.push().getKey();
                                        reference.child(id).setValue(ct);
                                        refFiles.add(ct);
                                        fileAdapter(id);

                                        adapter.notifyDataSetChanged();*/
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                UploadTaskActivity.this,
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
                                        UploadTaskActivity.this,
                                        "Failed to upload picture neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;


            } else if (fileType.equals("PDF")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/Pdf" + count + ".pdf");
                count++;
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
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                UploadTaskActivity.this,
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
                                        UploadTaskActivity.this,
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/PowerPoint" + count + ".ppt");
                count++;
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
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                UploadTaskActivity.this,
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
                                        UploadTaskActivity.this,
                                        "Failed to upload ppt neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("Word")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/msWord" + count + ".docx");
                count++;
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
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                UploadTaskActivity.this,
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
                                        UploadTaskActivity.this,
                                        "Failed to upload msword neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Tasks/zip" + count + ".zip");
                count++;
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
                                        reference.push().setValue(new ClassTask(
                                                title.getText().toString(),
                                                courseName,
                                                description.getText().toString(),
                                                deadline.getText().toString(),
                                                "teacher",
                                                "-",
                                                totalMarks.getText().toString(),
                                                teacherName,
                                                "##" + uri1,
                                                LocalDateTime.now().toString(),
                                                "ongoing"
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                UploadTaskActivity.this,
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
                                        UploadTaskActivity.this,
                                        "Failed to upload zip neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), TaskActivity.class);
        myIntent.putExtra("username", teacherName);
        myIntent.putExtra("courseName", courseName);
        myIntent.putExtra("userType", "teacher");
        myIntent.putExtra("fragment", "ongoing");
        startActivityForResult(myIntent, 0);
        return true;
    }
}