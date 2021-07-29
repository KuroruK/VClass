package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRvListGroupChatsAdapter extends RecyclerView.Adapter<MyRvListGroupChatsAdapter.MyViewHolder> {
    Context c;
    List<String> groupList; // contains list of group chats
    String username, type;

    public MyRvListGroupChatsAdapter(Context c, List<String> contactList, String username, String type) {
        this.username = username;
        this.c = c;
        this.groupList = contactList;
        this.type = type;
    }

    // setting row layout for group chat
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(c).inflate(R.layout.group_chat_row, parent, false);
        return new MyViewHolder(itemView);
    }

    // placing name of group chat in each row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(groupList.get(position));

        // if a group chat is selected from list, a new Activity that shows group chat of selected class is opened.
        if (holder.ll != null) {
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroupChatViewActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("type", type);
                    intent.putExtra("courseName", groupList.get(position));

                    c.startActivity(intent);
                }
            });
        }
    }

    // returns number of group chats in list
    @Override
    public int getItemCount() {
        if (groupList == null)
            return 0;
        return groupList.size();
    }

    // class to define elements of group chat row
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, bio;
        RelativeLayout ll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rac_name);
            ll = itemView.findViewById(R.id.row);


        }
    }
}
