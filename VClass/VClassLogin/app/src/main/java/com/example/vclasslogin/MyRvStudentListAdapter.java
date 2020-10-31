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

public class MyRvStudentListAdapter extends RecyclerView.Adapter<MyRvStudentListAdapter.MyViewHolder> {
    Context c;
    List<Student> studentList;

    public MyRvStudentListAdapter(Context c, List<Student> studentList) {
        this.c = c;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(c).inflate(R.layout.student_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(studentList.get(position).getName());
        holder.email.setText(studentList.get(position).getEmail());
        holder.Class.setText(studentList.get(position).getClassName());
        holder.section.setText(studentList.get(position).getSection());
        holder.contact.setText(studentList.get(position).getMobileNo());

        Log.v("t2","yes");
        if(holder.rl!=null) {
            Log.v("t2","no");
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("t1","here");
                    Intent intent=new Intent(view.getContext(),viewStudentDetailsActivity.class);
                    intent.putExtra("name",studentList.get(position).getName());
                    intent.putExtra("class",studentList.get(position).getClassName());
                    intent.putExtra("section",studentList.get(position).getSection());
                    intent.putExtra("email",studentList.get(position).getEmail());
                    intent.putExtra("mobileNo",studentList.get(position).getMobileNo());
                    intent.putExtra("username",studentList.get(position).getUsername());
                    intent.putExtra("password",studentList.get(position).getPassword());
                    intent.putExtra("id",studentList.get(position).getId());

                    c.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(studentList==null)
            return 0;
        return studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,Class,section,email,contact;
        RelativeLayout rl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.row_s_name2);
            Class=itemView.findViewById(R.id.row_s_class2);
            section=itemView.findViewById(R.id.row_s_section2);
            email=itemView.findViewById(R.id.row_s_email2);
            contact=itemView.findViewById(R.id.row_s_contact2);

            rl=itemView.findViewById(R.id.s_row);


        }
    }
}
