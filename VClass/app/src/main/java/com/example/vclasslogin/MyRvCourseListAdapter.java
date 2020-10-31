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

public class MyRvCourseListAdapter extends RecyclerView.Adapter<MyRvCourseListAdapter.MyViewHolder> {
    Context c;
    List<Courses> courseList;

    public MyRvCourseListAdapter(Context c, List<Courses> courseList) {
        this.c = c;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(c).inflate(R.layout.course_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.courseName.setText(courseList.get(position).getCourseName());
        holder.courseCode.setText(courseList.get(position).getCourseCode());
        holder.creditHrs.setText(courseList.get(position).getCreditHrs());

        Log.v("t2","yes");
        if(holder.rl!=null) {
            Log.v("t2","no");
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("t1","here");
                    Intent intent=new Intent(view.getContext(),viewCourseDetailsActivity.class);
                    intent.putExtra("courseName",courseList.get(position).getCourseName());
                    intent.putExtra("courseCode",courseList.get(position).getCourseCode());
                    intent.putExtra("creditHrs",courseList.get(position).getCreditHrs());
                    intent.putExtra("description",courseList.get(position).getDescription());
                    intent.putExtra("id",courseList.get(position).getId());


                    c.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(courseList==null)
            return 0;
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseName,courseCode,creditHrs;
        RelativeLayout rl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName=itemView.findViewById(R.id.row_c_name2);
            courseCode=itemView.findViewById(R.id.row_c_courseCode2);
            creditHrs=itemView.findViewById(R.id.row_c_crd_hrs2);

            rl=itemView.findViewById(R.id.c_row);


        }
    }
}
