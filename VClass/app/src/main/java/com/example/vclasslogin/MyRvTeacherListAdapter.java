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

public class MyRvTeacherListAdapter extends RecyclerView.Adapter<MyRvTeacherListAdapter.MyViewHolder> {
    Context c;
    List<Teacher> teacherList; // contains list of teachers

    public MyRvTeacherListAdapter(Context c, List<Teacher> teacherList) {
        this.c = c;
        this.teacherList = teacherList;
    }

    //setting row layout for teachers
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c).inflate(R.layout.teacher_row, parent, false);
        return new MyViewHolder(itemView);
    }

    // placing some specific teacher information in a row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(teacherList.get(position).getName());
        holder.mobileNo.setText(teacherList.get(position).getMobileNo());
        holder.email.setText(teacherList.get(position).getEmail());
        holder.specialization.setText(teacherList.get(position).getSpecialization());

        // if a teacher is selected from list, a new Activity that shows the selected teachers' details is opened.
        if (holder.rl != null) {
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), ViewTeacherDetailsActivity.class);
                    intent.putExtra("name", teacherList.get(position).getName());
                    intent.putExtra("email", teacherList.get(position).getEmail());
                    intent.putExtra("mobileNo", teacherList.get(position).getMobileNo());
                    intent.putExtra("username", teacherList.get(position).getUsername());
                    intent.putExtra("password", teacherList.get(position).getPassword());
                    intent.putExtra("specialization", teacherList.get(position).getSpecialization());
                    intent.putExtra("id", teacherList.get(position).getId());

                    c.startActivity(intent);
                }
            });
        }

    }

    // returns number of teachers in list
    @Override
    public int getItemCount() {
        if (teacherList == null)
            return 0;
        return teacherList.size();
    }

    // class to define elements of teacher row
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobileNo, email, specialization;
        RelativeLayout rl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.row_t_name2);
            mobileNo = itemView.findViewById(R.id.row_t_contact2);
            email = itemView.findViewById(R.id.row_t_email2);
            specialization = itemView.findViewById(R.id.row_t_qualification2);
            rl = itemView.findViewById(R.id.t_row);


        }
    }
}
