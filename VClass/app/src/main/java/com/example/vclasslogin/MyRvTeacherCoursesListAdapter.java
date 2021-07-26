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
    List<String> courses;
    String username;

    public MyRvTeacherCoursesListAdapter(Context c, List<String> courses, String username) {
        this.c = c;
        this.courses = courses;
        this.username = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c).inflate(R.layout.class_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.className.setText(courses.get(position));

        Log.v("t2", "yes");
        if (holder.rl != null) {
            Log.v("t2", "no");
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("t1", "here");
                    Intent intent = new Intent(view.getContext(), LiveTeacherClassActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("type", "teacher");
                    intent.putExtra("courseName", courses.get(position));

                    c.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (courses == null)
            return 0;
        return courses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView className, isLive;
        RelativeLayout rl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.row_class_name);
            //isLive = itemView.findViewById(R.id.row_class_is_live);

            rl = itemView.findViewById(R.id.class_row);


        }
    }
}
