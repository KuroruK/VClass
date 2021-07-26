package com.example.vclasslogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;

public class ShareFileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText course, title;
    Button attachFileBtn, shareBtn;
    String fileType = "", username, courseName;
    Uri selectedImage = null;
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");
        username = getIntent().getStringExtra("username");
        courseName = getIntent().getStringExtra("courseName");

        // displaying activity in dialog form window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        // getting ids
        //course = findViewById(R.id.file_course_2);
        title = findViewById(R.id.file_title_2);
        attachFileBtn = findViewById(R.id.file_attach_btn);
        shareBtn = findViewById(R.id.file_share_btn);

        // attaching file
        attachFileBtn.setOnClickListener(new View.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ShareFileActivity.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            fileType = "Images";

                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,100);

                        }
                        if (i == 1) {
                            fileType = "PDF";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            //intent.setType("*/*");
                            startActivityForResult(intent, 100);
                        }if (i == 2) {
                            fileType = "PPT";
                            String[] mimeTypes =
                                    {"application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx

                                    };
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
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
                            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                            startActivityForResult(intent, 100);
                        }
                    }
                });
                builder.show();
            }
        });
        // sharing file
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "File successfully shared!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            if (fileType.equals("Images")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/images" + count + ".jpg");
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
                                        reference.push().setValue(new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "image",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                ShareFileActivity.this,
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
                                        ShareFileActivity.this,
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
                                        reference.push().setValue(new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "pdf",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                ShareFileActivity.this,
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
                                        ShareFileActivity.this,
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
                                        reference.push().setValue(new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "ppt",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                ShareFileActivity.this,
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
                                        ShareFileActivity.this,
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
                                        reference.push().setValue(new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "msword",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                ShareFileActivity.this,
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
                                        ShareFileActivity.this,
                                        "Failed to upload msword neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            } else if (fileType.equals("zip")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference = storageReference.child("Resources/zip" + count+".zip");
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
                                        reference.push().setValue(new Resource(
                                                courseName,
                                                title.getText().toString(),
                                                "zip",
                                                uri1,
                                                LocalDateTime.now().toString(),
                                                username
                                        ));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                ShareFileActivity.this,
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
                                        ShareFileActivity.this,
                                        "Failed to upload other neechay",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        })
                ;
            }
        }
    }
}