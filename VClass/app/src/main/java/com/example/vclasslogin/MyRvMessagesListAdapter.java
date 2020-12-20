package com.example.vclasslogin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRvMessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    List<message> messages;
    String senderID;
    FirebaseDatabase database;
    DatabaseReference reference;
    CircleImageView receiverProfilePhoto;
    static int count=0;
    String photo;


    public MyRvMessagesListAdapter(Context c, List<message> messages, String senderID, String photo) {
        this.senderID=senderID;
        this.photo=photo;
        this.c = c;
        this.messages = messages;
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("profiles");

    }


    @Override
    public int getItemViewType(int position) {
        boolean isImage=messages.get(position).isImage;
        if(!(isImage)) {
            if (messages.get(position).getSenderID().equals(senderID)) {
                return 0;
            }
            return 1;
        }
        if (messages.get(position).getSenderID().equals(senderID)) {
            return 2;
        }
        return 3;

    }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


      LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
      RecyclerView.ViewHolder viewHolder = null;
      switch (viewType) {
          case 0:
              View v1 = inflater.inflate(R.layout.sender_message_row, viewGroup, false);
              viewHolder = new MyViewHolder1(v1);
              break;
          case 1:
              View v2 = inflater.inflate(R.layout.receiver_message_row, viewGroup, false);
              viewHolder = new MyViewHolder2(v2);
              break;
          case 2:
              View v3 = inflater.inflate(R.layout.sender_image_row, viewGroup, false);
              viewHolder = new MyViewHolder3(v3);
              break;
          case 3:
              View v4 = inflater.inflate(R.layout.receiver_image_row, viewGroup, false);
              viewHolder = new MyViewHolder4(v4);
              break;
          default:
              
              break;
      }
      return viewHolder;
  }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (this.getItemViewType(position)) {
            case 0:
                MyViewHolder1 vh1 = (MyViewHolder1) viewHolder;
                vh1.senMessage.setText(messages.get(position).getMessage());
                vh1.senTime.setText(messages.get(position).getTime());
                break;
            case 1:
                MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                Log.v("message",messages.get(position).getMessage());
                vh2.recMessage.setText(messages.get(position).getMessage());
                vh2.recTime.setText(messages.get(position).getTime());
                vh2.username.setText(messages.get(position).getSenderID());
                break;
            case 2:
                MyViewHolder3 vh3 = (MyViewHolder3) viewHolder;
                Picasso.get().load(messages.get(position).getMessage()).into(vh3.senImage);
                vh3.senImageTime.setText(messages.get(position).getTime());
                break;
            case 3:
                MyViewHolder4 vh4 = (MyViewHolder4) viewHolder;
                Picasso.get().load(messages.get(position).getMessage()).into(vh4.recImage);
                vh4.recImageTime.setText(messages.get(position).getTime());
                Picasso.get().load(photo).into(vh4.recPhoto);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount () {
        if (messages == null)
            return 0;
        return messages.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView senMessage,senTime;
        RelativeLayout senRow;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            senMessage=itemView.findViewById(R.id.row_sender_msg);
            senTime=itemView.findViewById(R.id.row_sender_time);
            senRow=itemView.findViewById(R.id.row_sender);

        }
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView recMessage,recTime,username;
        RelativeLayout recRow;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.row_rm_username);
            recMessage=itemView.findViewById(R.id.row_rm_msg);
            recTime=itemView.findViewById(R.id.row_rm_time);
            recRow=itemView.findViewById(R.id.row_rec);

        }
    }
    public class MyViewHolder3 extends RecyclerView.ViewHolder {
        ImageView senImage;
        TextView senImageTime;
        RelativeLayout senImageRow;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            senImage=itemView.findViewById(R.id.sender_image);
            senImageTime=itemView.findViewById(R.id.sender_time);
            senImageRow=itemView.findViewById(R.id.row_sender_image);

        }
    }

    public class MyViewHolder4 extends RecyclerView.ViewHolder {
      CircleImageView recPhoto;
        ImageView recImage;
        TextView recImageTime;
        RelativeLayout recImageRow;

        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            recPhoto=itemView.findViewById(R.id.row_rec_photo);
            recImage=itemView.findViewById(R.id.row_rec_image);
            recImageTime=itemView.findViewById(R.id.row_rec_time);
            recImageRow=itemView.findViewById(R.id.row_rec);

        }
    }

}



