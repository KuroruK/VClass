package com.example.vclasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    String fileType = "";   // images, ppt, word, pdf, zip
    String username, courseName;
    Uri selectedImage = null;
    MyRvResourceListAdapter adapter;
    ArrayList<Resource> resources;
    Boolean attached = false;
    String id;
    static int count = 0;   // add a number at the end of attached file names by increasing 1-by-1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_file);

        // actionbar - back button
        getSupportActionBar().setTitle("File Details");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");
        courseName = getIntent().getStringExtra("courseName");
        resources = new ArrayList<>();

        // getting firebase instance and reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");

        // displaying activity in dialog form window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // size of window
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // setting size of the window/activity
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        // getting ids
        title = findViewById(R.id.at_file_title_2);
        attachFileBtn = findViewById(R.id.af_attach_btn);
        confirmBtn = findViewById(R.id.af_confirm_btn);
        rvFile = findViewById(R.id.af_rv);

        // attaching file
        attachFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resources.isEmpty())
                    attached = false;
                if (!attached) {
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
                                startActivityForResult(intent, 300);
                            }
                            if (i == 3) {
                                fileType = "Word";

                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
                // display message when action is completed and finish this activity
                Toast.makeText(getApplicationContext(), "File successfully attached!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // function to set rv based on ids - when the file is attached, it is shown in rv
    void fileAdapter(String id) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rvFile.setLayoutManager(manager);
        adapter = new MyRvResourceListAdapter(getApplicationContext(), resources, "attach", id);
        rvFile.setAdapter(adapter);
    }

    /*following code used to store file/resource in firebase storage and then linking a reference to that file
    in a message that is stored in realtime firebase so that it can be shared with others in almost real time*/
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

    // method use to go to previous activity when back button is pressed
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LiveTeacherClassActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("courseName", courseName);
        startActivityForResult(myIntent, 0);
        return true;
    }
}