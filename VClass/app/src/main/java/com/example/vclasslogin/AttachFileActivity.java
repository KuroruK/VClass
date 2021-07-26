package com.example.vclasslogin;

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

public class AttachFileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText title;
    RecyclerView rvFile;
    Button attachFileBtn, confirmBtn;
    String fileType = "";
    String username, courseName, taskTitle, description, deadline, obtainedMarks, totalMarks, submittedBy, file, date;
    Uri selectedImage = null;
    MyRvResourceListAdapter adapter;
    ArrayList<Resource> resources;
    Boolean attached = false;
    String id;
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);

        // action bar
        getSupportActionBar().setTitle("File Details");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // code here
        username = getIntent().getStringExtra("username");
        courseName = getIntent().getStringExtra("courseName");
        resources = new ArrayList<>();
        //taskTitle = getIntent().getStringExtra("taskTitle");
        //description = getIntent().getStringExtra("description");
        //deadline = getIntent().getStringExtra("deadline");
        //obtainedMarks = getIntent().getStringExtra("obtainedMarks");
        //totalMarks = getIntent().getStringExtra("totalMarks");
        //submittedBy = getIntent().getStringExtra("submittedBy");
        //file = getIntent().getStringExtra("file");
        //date = getIntent().getStringExtra("date");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");

        // displaying activity in dialog form window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        // getting ids
        title = findViewById(R.id.at_file_title_2);
        attachFileBtn = findViewById(R.id.af_attach_btn);
        confirmBtn = findViewById(R.id.af_confirm_btn);
        rvFile = findViewById(R.id.af_rv);  // code here to show file in this rv

        // attaching file
        attachFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resources.isEmpty())
                    attached = false;
                if (attached == false) {
                    CharSequence options[] = new CharSequence[]
                            {
                                    "Images",
                                    "PDF",
                                    "PowerPoint",
                                    "MS Word File",
                                    "Zip file"
                            };
                    AlertDialog.Builder builder = new AlertDialog.Builder(AttachFileActivity.this);
                    builder.setTitle("Select File");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                fileType = "Images";

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 300);

                            }
                            if (i == 1) {
                                fileType = "PDF";

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/pdf");
                                //intent.setType("*/*");
                                startActivityForResult(intent, 300);
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
                                startActivityForResult(intent, 300);
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
                                startActivityForResult(intent, 300);
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
                                startActivityForResult(intent, 300);
                            }
                        }
                    });
                    builder.show();


                } else {
                    Toast.makeText(getApplicationContext(), "Only one file attachment is allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // sharing file
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "File successfully attached!", Toast.LENGTH_SHORT).show();
                //code here to finish activity and do some action
                finish();
            }
        });
    }

    void fileAdapter(String id) {
        Log.v("check id", id);
        Log.v("check resource", String.valueOf(resources.size()));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rvFile.setLayoutManager(manager);
        adapter = new MyRvResourceListAdapter(getApplicationContext(), resources, "attach", id);
        rvFile.setAdapter(adapter);
    }

    //code here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            if (fileType.equals("Images")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/images" + count + ".jpg");
                count++;
                storageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri1 = uri.toString();
                                        Resource res = new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "image",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        );
                                        id = reference.push().getKey();
                                        reference.child(id).setValue(res);
                                        resources.add(res);
                                        fileAdapter(id);
                                        attached = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
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
                                        getApplicationContext(),
                                        "Failed to upload picture neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;


            } else if (fileType.equals("PDF")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/Pdf" + count + ".pdf");
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
                                        Resource res = new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "pdf",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        );
                                        id = reference.push().getKey();
                                        reference.child(id).setValue(res);
                                        resources.add(res);
                                        fileAdapter(id);
                                        attached = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
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
                                        getApplicationContext(),
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("PPT")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/PowerPoint" + count + ".ppt");
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
                                        Resource res = new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "ppt",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        );
                                        id = reference.push().getKey();
                                        reference.child(id).setValue(res);
                                        resources.add(res);
                                        fileAdapter(id);
                                        attached = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
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
                                        getApplicationContext(),
                                        "Failed to upload pdf neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;

            } else if (fileType.equals("Word")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/msWord" + count + ".docx");
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
                                        Resource res = new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "msword",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        );
                                        id = reference.push().getKey();
                                        reference.child(id).setValue(res);
                                        resources.add(res);
                                        fileAdapter(id);
                                        attached = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
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
                                        getApplicationContext(),
                                        "Failed to upload msword neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/zip" + count + ".zip");
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
                                        Resource res = new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "zip",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        );
                                        id = reference.push().getKey();
                                        reference.child(id).setValue(res);
                                        resources.add(res);
                                        fileAdapter(id);
                                        attached = true;
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                getApplicationContext(),
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
                                        getApplicationContext(),
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
        Intent myIntent = new Intent(getApplicationContext(), LiveTeacherClassActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("courseName", courseName);
        startActivityForResult(myIntent, 0);
        return true;
    }
}