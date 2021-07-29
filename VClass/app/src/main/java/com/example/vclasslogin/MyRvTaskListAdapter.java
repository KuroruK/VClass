package com.example.vclasslogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyRvTaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    List<ClassTask> tasks;
    String userName, userType;
    ArrayList<String> ids; // list of task ids/keys from firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> studentTaskTitles;
    DBHelper dbHelper;

    public MyRvTaskListAdapter(Context c, List<ClassTask> tasks, String userType, ArrayList<String> ids, String userName, ArrayList<String> studentTaskTitles) {
        this.c = c;
        this.tasks = tasks;
        this.userType = userType;
        this.ids = ids;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tasks");
        this.userName = userName;
        this.studentTaskTitles = studentTaskTitles;
        this.dbHelper = new DBHelper(c);
    }

    //returns a value depending on userType -- used to decide which view to create and bind to
    @Override
    public int getItemViewType(int position) {
        switch (userType) {
            case "teacher0": //ongoing teacher - main
                return 0;
            case "teacher1": //submitted teacher - main
                return 1;
            case "student0": //ongoing student - main
                return 2;
            case "student1": //submitted student - main
                return 3;
            case "ongoingTeacherView": //ongoing teacher view - individual task
                return 4;
            case "ongoingStudent": //ongoing student view - individual task
                return 5;
            case "teacherSideStudentNames": //submitted students from teacher side
                return 6;
            case "ongoingTeacherEdit": //ongoing teacher edit - individual task
                return 7;
            case "submittedStudentIndividualFiles": //student submitted view - individual task
                return 8;
            default:
                return -1;
        }

    }

    //setting row layout depending on which activity called it.
    // Variable "userType" used to determine this
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        // viewType is integer that is returned from getItemViewType method.
        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.row_ongoing_task, viewGroup, false);
                viewHolder = new MyViewHolder1(v1);
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.row_ongoing_task, viewGroup, false);
                viewHolder = new MyViewHolder2(v2);
                break;
            case 2:
                View v3 = inflater.inflate(R.layout.row_submitted_task_std, viewGroup, false);
                viewHolder = new MyViewHolder3(v3);
                break;
            case 3:
                View v4 = inflater.inflate(R.layout.row_submitted_task_std, viewGroup, false);
                viewHolder = new MyViewHolder4(v4);
                break;

            case 4:
                View v6 = inflater.inflate(R.layout.row_attached_file, viewGroup, false);
                viewHolder = new MyViewHolder6(v6);
                break;
            case 5:
                View v5 = inflater.inflate(R.layout.row_student_tasks, viewGroup, false);
                viewHolder = new MyViewHolder5(v5);
                break;
            case 6:
                View v7 = inflater.inflate(R.layout.row_std_submission, viewGroup, false);
                viewHolder = new MyViewHolder7(v7);
                break;
            case 7:
                View v8 = inflater.inflate(R.layout.row_t_attached_file, viewGroup, false);
                viewHolder = new MyViewHolder8(v8);
                break;
            case 8:
                View v9 = inflater.inflate(R.layout.row_attached_file, viewGroup, false);
                viewHolder = new MyViewHolder9(v9);
                break;
            default:

                break;
        }
        assert viewHolder != null;
        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (this.getItemViewType(position)) {
            case 0://ongoing teacher
                MyViewHolder1 vh1 = (MyViewHolder1) viewHolder;
                vh1.dueDate.setText(tasks.get(position).getDeadline());
                vh1.taskName.setText(tasks.get(position).getTaskTitle());
                vh1.teacher.setText(tasks.get(position).getSubmittedBy());
                // if task clicked, will view the selected uploaded task in detail
                vh1.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), TeacherViewUploadedTaskActivity.class);
                        intent.putExtra("taskTitle", tasks.get(position).getTaskTitle());
                        intent.putExtra("courseName", tasks.get(position).getCourseName());
                        intent.putExtra("description", tasks.get(position).getDescription());
                        intent.putExtra("deadline", tasks.get(position).getDeadline());
                        intent.putExtra("obtainedMarks", tasks.get(position).getObtainedMarks());
                        intent.putExtra("totalMarks", tasks.get(position).getTotalMarks());
                        intent.putExtra("submittedBy", tasks.get(position).getSubmittedBy());
                        intent.putExtra("file", tasks.get(position).getFile());
                        intent.putExtra("date", tasks.get(position).getDate());
                        intent.putExtra("id", ids.get(position));
                        c.startActivity(intent);


                    }
                });
                break;
            case 1://submitted teacher
                MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                vh2.dueDate.setText(tasks.get(position).getDeadline());
                vh2.taskName.setText(tasks.get(position).getTaskTitle());
                vh2.teacher.setText(tasks.get(position).getSubmittedBy());
                vh2.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), TeacherViewPreviousTaskActivity.class);
                        intent.putExtra("taskTitle", tasks.get(position).getTaskTitle());
                        intent.putExtra("courseName", tasks.get(position).getCourseName());
                        intent.putExtra("description", tasks.get(position).getDescription());
                        intent.putExtra("deadline", tasks.get(position).getDeadline());
                        intent.putExtra("obtainedMarks", tasks.get(position).getObtainedMarks());
                        intent.putExtra("totalMarks", tasks.get(position).getTotalMarks());
                        intent.putExtra("submittedBy", tasks.get(position).getSubmittedBy());
                        intent.putExtra("file", tasks.get(position).getFile());
                        intent.putExtra("date", tasks.get(position).getDate());
                        intent.putExtra("id", ids.get(position));
                        intent.putExtra("userName", userName);
                        c.startActivity(intent);

                    }
                });
                break;
            case 2:// ongoing student
                MyViewHolder3 vh3 = (MyViewHolder3) viewHolder;
                if (tasks.size() != 0) {
                    vh3.taskName.setText(tasks.get(position).getTaskTitle());
                    vh3.teacherName.setText(tasks.get(position).getSubmittedBy());
                    vh3.due.setText(tasks.get(position).getDeadline());
                    String alreadySubmitted = "0";

                    if (studentTaskTitles.size() != 0) {
                        if (studentTaskTitles.contains(tasks.get(position).getTaskTitle())) {
                            vh3.status.setText("Submitted!"); // completed
                            vh3.status.setTextColor(Color.parseColor("#808080"));
                            alreadySubmitted = "1";
                        } else {
                            vh3.status.setText("Assigned!"); // incomplete
                        }
                    } else {
                        vh3.status.setText("Assigned!"); // incomplete
                    }


                    final String finalAlreadySubmitted = alreadySubmitted;
                    vh3.rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), StudentViewOngoingTaskActivity.class);
                            intent.putExtra("taskTitle", tasks.get(position).getTaskTitle());
                            intent.putExtra("courseName", tasks.get(position).getCourseName());
                            intent.putExtra("description", tasks.get(position).getDescription());
                            intent.putExtra("deadline", tasks.get(position).getDeadline());
                            intent.putExtra("obtainedMarks", tasks.get(position).getObtainedMarks());
                            intent.putExtra("totalMarks", tasks.get(position).getTotalMarks());
                            intent.putExtra("submittedBy", tasks.get(position).getSubmittedBy());
                            intent.putExtra("file", tasks.get(position).getFile());
                            intent.putExtra("date", tasks.get(position).getDate());
                            intent.putExtra("id", ids.get(position));
                            intent.putExtra("userName", userName);
                            intent.putExtra("alreadySubmitted", finalAlreadySubmitted);
                            c.startActivity(intent);


                        }
                    });
                }
                break;
            case 3://submitted student
                MyViewHolder4 vh4 = (MyViewHolder4) viewHolder;
                vh4.taskName.setText(tasks.get(position).getTaskTitle());
                vh4.teacherName.setText(tasks.get(position).getSubmittedBy());
                vh4.due.setText(tasks.get(position).getDeadline());

                if (studentTaskTitles.size() != 0) {
                    if (studentTaskTitles.contains(tasks.get(position).getTaskTitle())) {
                        vh4.status.setText("Submitted!");
                        vh4.status.setTextColor(Color.parseColor("#808080"));
                    } else {
                        vh4.status.setText("Missing!"); //incomplete tasks
                        vh4.status.setTextColor(Color.parseColor("#B60520"));
                    }
                } else {
                    vh4.status.setText("Missing!");
                    vh4.status.setTextColor(Color.parseColor("#B60520"));
                }

                vh4.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), StudentViewPreviousTaskActivity.class);
                        intent.putExtra("taskTitle", tasks.get(position).getTaskTitle());
                        intent.putExtra("courseName", tasks.get(position).getCourseName());
                        intent.putExtra("description", tasks.get(position).getDescription());
                        intent.putExtra("deadline", tasks.get(position).getDeadline());
                        intent.putExtra("obtainedMarks", tasks.get(position).getObtainedMarks());
                        intent.putExtra("totalMarks", tasks.get(position).getTotalMarks());
                        intent.putExtra("submittedBy", tasks.get(position).getSubmittedBy());
                        intent.putExtra("file", tasks.get(position).getFile());
                        intent.putExtra("date", tasks.get(position).getDate());
                        intent.putExtra("id", ids.get(position));
                        intent.putExtra("userName", userName);
                        c.startActivity(intent);


                    }
                });

                break;

            case 4://ongoing teacher view individual

                final MyViewHolder6 vh6 = (MyViewHolder6) viewHolder;
                if (ids.size() > position + 1) {
                    String file = ids.get(position + 1);
                    String extension = file.substring(file.indexOf("?alt") - 1, file.indexOf("?alt"));
                    if (extension.equals("g")) extension = ".jpg";
                    if (extension.equals("f")) extension = ".pdf";
                    if (extension.equals("t")) extension = ".ppt";
                    if (extension.equals("x")) extension = ".docx";
                    if (extension.equals("p")) extension = ".zip";

                    vh6.taskName.setText(tasks.get(position).getTaskTitle() + extension);
                    vh6.rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ids.get(position + 1)));
                            vh6.itemView.getContext().startActivity(intent);
                        }
                    });
                }
                break;
            case 5://ongoing individual student task
                final MyViewHolder5 vh5 = (MyViewHolder5) viewHolder;
                String file2 = tasks.get(position).getFile();
                String extension2 = file2.substring(file2.indexOf("?alt") - 1, file2.indexOf("?alt"));
                if (extension2.equals("g")) extension2 = ".jpg";
                if (extension2.equals("f")) extension2 = ".pdf";
                if (extension2.equals("t")) extension2 = ".ppt";
                if (extension2.equals("x")) extension2 = ".docx";
                if (extension2.equals("p")) extension2 = ".zip";

                vh5.taskName.setText(tasks.get(position).getTaskTitle() + extension2);
                vh5.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tasks.size() == 1) {
                            tasks.remove(0);
                            reference.child(ids.get(0)).removeValue();
                            ids.remove(0);
                            notifyItemRemoved(0);

                        } else {
                            tasks.remove(position);
                            reference.child(ids.get(position)).removeValue();
                            ids.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                });
                vh5.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tasks.get(position).getFile()));
                        vh5.itemView.getContext().startActivity(intent);
                    }
                });

            default:
                break;
            case 6:

                final MyViewHolder7 vh7 = (MyViewHolder7) viewHolder;
                vh7.name.setText(studentTaskTitles.get(position));//in studentTaskTitles-> studentNames
                vh7.id.setText(String.valueOf(dbHelper.getStudentID(vh7.name.getText().toString().trim())));
                vh7.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), TeacherViewStudentPreviousTaskActivity.class);
                        intent.putExtra("taskTitle", tasks.get(position).getTaskTitle());
                        intent.putExtra("courseName", tasks.get(position).getCourseName());
                        intent.putExtra("description", tasks.get(position).getDescription());
                        intent.putExtra("deadline", tasks.get(position).getDeadline());
                        intent.putExtra("obtainedMarks", tasks.get(position).getObtainedMarks());
                        intent.putExtra("totalMarks", tasks.get(position).getTotalMarks());
                        intent.putExtra("submittedBy", tasks.get(position).getSubmittedBy());
                        intent.putExtra("file", tasks.get(position).getFile());
                        intent.putExtra("date", tasks.get(position).getDate());
                        intent.putExtra("id", String.valueOf(dbHelper.getStudentID(vh7.name.getText().toString().trim())));
                        intent.putExtra("userName", userName);
                        c.startActivity(intent);

                    }
                });
                break;

            case 7://ongoing teacher edit individual
                final MyViewHolder8 vh8 = (MyViewHolder8) viewHolder;
                String file1 = ids.get(position + 1);
                Log.v("checkFile1", file1);
                String extension1 = file1.substring(file1.indexOf("?alt") - 1, file1.indexOf("?alt"));
                if (extension1.equals("g")) extension1 = ".jpg";
                if (extension1.equals("f")) extension1 = ".pdf";
                if (extension1.equals("t")) extension1 = ".ppt";
                if (extension1.equals("x")) extension1 = ".docx";
                if (extension1.equals("p")) extension1 = ".zip";
                vh8.taskName.setText(tasks.get(position).getTaskTitle() + extension1);
                vh8.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ids.get(position + 1)));
                        vh8.itemView.getContext().startActivity(intent);
                    }
                });
                vh8.cancel.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        //code here
                        String taskTitle = ids.get(0).substring(ids.get(0).indexOf("##") + 2);
                        String key = ids.get(0).substring(0, ids.get(0).indexOf("##"));
                        String oldFile = tasks.get(position).getFile();
                        String newFile = oldFile.substring(0, oldFile.indexOf(ids.get(position + 1)) - 2) + oldFile.substring(oldFile.indexOf(ids.get(position + 1)) + ids.get(position + 1).length());
                        Log.v("checkFileNew", newFile);
                        Log.v("checkFileOld", oldFile);
                        Log.v("checkFileKey", key);
                        for (int i = 0; i < tasks.size(); i++) {
                            tasks.get(i).setFile(newFile);
                        }
                        reference.child(key).setValue(new ClassTask(
                                taskTitle,
                                tasks.get(position).getCourseName(),
                                tasks.get(position).getDescription(),
                                tasks.get(position).getDeadline(),
                                "teacher",
                                "-",
                                tasks.get(position).getTotalMarks(),
                                tasks.get(position).getSubmittedBy(),
                                newFile,
                                LocalDateTime.now().toString(),
                                "ongoing"
                        ));
                        tasks.remove(position);
                        ids.remove(position + 1);
                        notifyItemRemoved(position);
                    }
                });
                break;
            case 8://student submitted view individual

                final MyViewHolder6 vh9 = (MyViewHolder6) viewHolder;
                if (ids.size() > position) {
                    String file = ids.get(position);
                    final String file11 = tasks.get(position).getFile();
                    Log.v("checkStdFile", file);
                    Log.v("checkStdFile", file11);
                    String extension = file11.substring(file11.indexOf("?alt") - 1, file11.indexOf("?alt"));
                    if (extension.equals("g")) extension = ".jpg";
                    if (extension.equals("f")) extension = ".pdf";
                    if (extension.equals("t")) extension = ".ppt";
                    if (extension.equals("x")) extension = ".docx";
                    if (extension.equals("p")) extension = ".zip";

                    vh9.taskName.setText(tasks.get(position).getTaskTitle() + extension);
                    //vh6.teacher.setText("here");
                    vh9.rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file11));
                            vh9.itemView.getContext().startActivity(intent);
                        }
                    });
                }
                break;

        }

    }

    @Override
    public int getItemCount() {
        if (tasks == null)
            return 0;
        return tasks.size();
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView taskName, dueDate, teacher;
        RelativeLayout rl;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_task_og_name);
            dueDate = itemView.findViewById(R.id.row_task_og_deadline2);
            teacher = itemView.findViewById(R.id.row_task_og_tname);
            rl = itemView.findViewById(R.id.o_task_row);


        }
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView taskName, dueDate, teacher;
        RelativeLayout rl;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_task_og_name);
            dueDate = itemView.findViewById(R.id.row_task_og_deadline2);
            teacher = itemView.findViewById(R.id.row_task_og_tname);
            rl = itemView.findViewById(R.id.o_task_row);


        }
    }

    public static class MyViewHolder3 extends RecyclerView.ViewHolder {
        TextView taskName, teacherName, status, due;
        RelativeLayout rl;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_task_submitted_name);
            teacherName = itemView.findViewById(R.id.row_task_submitted_tname);
            status = itemView.findViewById(R.id.row_task_submitted_status);
            due = itemView.findViewById(R.id.row_task_submitted_deadline2);

            rl = itemView.findViewById(R.id.student_task_row);


        }
    }

    public static class MyViewHolder4 extends RecyclerView.ViewHolder {
        TextView taskName, teacherName, status, due;
        RelativeLayout rl;

        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_task_submitted_name);
            teacherName = itemView.findViewById(R.id.row_task_submitted_tname);
            status = itemView.findViewById(R.id.row_task_submitted_status);
            due = itemView.findViewById(R.id.row_task_submitted_deadline2);

            rl = itemView.findViewById(R.id.student_task_row);
        }
    }

    public static class MyViewHolder5 extends RecyclerView.ViewHolder {
        TextView taskName;
        ImageView download, cancel;
        RelativeLayout rl;

        public MyViewHolder5(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_ss_fname);
            download = itemView.findViewById(R.id.row_std_s_download);
            cancel = itemView.findViewById(R.id.row_std_s_cancel);

            rl = itemView.findViewById(R.id.s_row);


        }
    }

    public static class MyViewHolder6 extends RecyclerView.ViewHolder {
        TextView taskName;
        RelativeLayout rl;

        public MyViewHolder6(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_vf_fname);
            rl = itemView.findViewById(R.id.s_row);


        }
    }

    public static class MyViewHolder7 extends RecyclerView.ViewHolder {
        TextView name, id;
        RelativeLayout rl;

        public MyViewHolder7(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.row_ss_fname);
            id = itemView.findViewById(R.id.row_ss_id);
            rl = itemView.findViewById(R.id.s_row);


        }
    }

    public static class MyViewHolder8 extends RecyclerView.ViewHolder {
        TextView taskName;
        ImageView cancel;
        RelativeLayout rl;

        public MyViewHolder8(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_tvf_fname);
            cancel = itemView.findViewById(R.id.row_tvf_cancel);
            rl = itemView.findViewById(R.id.s_row);
        }

    }


    public static class MyViewHolder9 extends RecyclerView.ViewHolder {
        TextView taskName;
        RelativeLayout rl;

        public MyViewHolder9(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.row_vf_fname);
            rl = itemView.findViewById(R.id.s_row);


        }
    }
}
