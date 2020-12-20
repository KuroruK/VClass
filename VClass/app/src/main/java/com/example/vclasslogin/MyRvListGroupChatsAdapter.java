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

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRvListGroupChatsAdapter extends RecyclerView.Adapter<MyRvListGroupChatsAdapter.MyViewHolder> {
    Context c;
    List<String> groupList;
    String username;

    public MyRvListGroupChatsAdapter(Context c, List<String> contactList, String username) {
        this.username = username;
        this.c = c;
        this.groupList = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c).inflate(R.layout.group_chat_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(groupList.get(position));

        //  holder.name.setText(contactList.get(position).getFirstName()+" "+contactList.get(position).getLastName());
        // holder.bio.setText(contactList.get(position).getBio());
        Log.v("t2", "yes");
        if (holder.ll != null) {
            Log.v("t2", "no");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("t1", "here");
                    Intent intent = new Intent(view.getContext(), StudentGroupChatViewActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("type", "student");
                    intent.putExtra("courseName", groupList.get(position));

                    c.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (groupList == null)
            return 0;
        return groupList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, bio;
        RelativeLayout ll;
        CircleImageView contactProfilePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rac_name);
            bio = itemView.findViewById(R.id.rac_msg);
            //   contactProfilePhoto=itemView.findViewById(R.id.rac_img);
            ll = itemView.findViewById(R.id.row);


        }
    }
}
