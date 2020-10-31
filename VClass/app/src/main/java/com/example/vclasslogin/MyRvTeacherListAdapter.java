package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRvTeacherListAdapter extends RecyclerView.Adapter<MyRvTeacherListAdapter.MyViewHolder> {
    Context c;
    List<Teacher> teacherList;

    public MyRvTeacherListAdapter(Context c, List<Teacher> teacherList) {
        this.c = c;
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(c).inflate(R.layout.teacher_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(teacherList.get(position).getName());
        holder.mobileNo.setText(teacherList.get(position).getMobileNo());
        holder.email.setText(teacherList.get(position).getEmail());
        holder.username.setText(teacherList.get(position).getUsername());
        holder.password.setText(teacherList.get(position).getPassword());
        holder.specialization.setText(teacherList.get(position).getSpecialization());

        Log.v("t2","yes");
        if(holder.ll!=null) {
            Log.v("t2","no");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("t1","here");
                    Intent intent=new Intent(view.getContext(),manageTeacherActivity.class);
            /*        intent.putExtra("name",contactList.get(position).getName());
                    intent.putExtra("name",contactList.get(position).getName());
                    intent.putExtra("name",contactList.get(position).getName());
                    intent.putExtra("name",contactList.get(position).getName());
                    intent.putExtra("name",contactList.get(position).getName());
                    intent.putExtra("name",contactList.get(position).getName());

              */
                    c.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(teacherList==null)
            return 0;
        return teacherList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,mobileNo,email,username,password,specialization;
        LinearLayout ll;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            mobileNo=itemView.findViewById(R.id.mobileNo);
            email=itemView.findViewById(R.id.email);
            username=itemView.findViewById(R.id.username);
            password=itemView.findViewById(R.id.password);
            specialization=itemView.findViewById(R.id.specialization);
            ll=itemView.findViewById(R.id.row);


        }
    }
}
