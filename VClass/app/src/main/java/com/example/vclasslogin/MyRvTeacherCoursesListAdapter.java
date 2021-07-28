package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRvTeacherCoursesListAdapter extends RecyclerView.Adapter<MyRvTeacherCoursesListAdapter.MyViewHolder> {
    Context c;
    List<String> courses; // contains list of courses of the user - in this case, a teacher
    String username;

    public MyRvTeacherCoursesListAdapter(Context c, List<String> courses, String username) {
        this.c = c;
        this.courses = courses;
        this.username = username;
    }

    //setting row layout for teacher courses
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c).inflate(R.layout.class_row, parent, false);
        return new MyViewHolder(itemView);
    }

    // placing some specific teacher course information in a row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.className.setText(courses.get(position));

        // if a course of a teacher is selected from list, a new Activity that shows the selected teachers' course class hub is opened.
        if (holder.rl != null) {
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), LiveTeacherClassActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("type", "teacher");
                    intent.putExtra("courseName", courses.get(position));

                    c.startActivity(intent);
                }
            });
        }

    }

    // returns number of teachers' courses in list
    @Override
    public int getItemCount() {
        if (courses == null)
            return 0;
        return courses.size();
    }

    // class to define elements of a teachers' course row
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView className, isLive;
        RelativeLayout rl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.row_class_name);
            rl = itemView.findViewById(R.id.class_row);


        }
    }
}
