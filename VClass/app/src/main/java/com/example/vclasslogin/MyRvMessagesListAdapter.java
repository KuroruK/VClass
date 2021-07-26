package com.example.vclasslogin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    static int count = 1;
    String photo;


    public MyRvMessagesListAdapter(Context c, List<message> messages, String senderID, String photo) {
        this.senderID = senderID;
        this.photo = photo;
        this.c = c;
        this.messages = messages;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("profiles");
    }


    @Override
    public int getItemViewType(int position) {
        boolean isImage = messages.get(position).getResource();
        if (!(isImage)) {
            if (messages.get(position).getSenderID().equals(senderID)) {
                return 0;
            }
            return 1;
        }
        if (isImage && messages.get(position).getResourceType().equals("images")) {
            Log.v("myLogmsg", senderID);
            if (messages.get(position).getSenderID().equals(senderID)) {
                return 2;
            }
            return 3;
        }
        if (isImage)
            if (messages.get(position).getSenderID().equals(senderID)) {
                return 4;
            }
        return 5;
    }

    /*  @NonNull
      @Override
      public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View itemView= LayoutInflater.from(c).inflate(R.layout.message_row,parent,false);
          return new MyViewHolder(itemView);
      }
  */
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
            case 4:
                View v5 = inflater.inflate(R.layout.sender_file_row, viewGroup, false);
                viewHolder = new MyViewHolder5(v5);
                break;
            case 5:
                View v6 = inflater.inflate(R.layout.receiver_file_row, viewGroup, false);
                viewHolder = new MyViewHolder6(v6);
                break;
            default:

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        switch (this.getItemViewType(position)) {
            case 0://sen message
                MyViewHolder1 vh1 = (MyViewHolder1) viewHolder;
                vh1.senMessage.setText(messages.get(position).getMessage());
                vh1.senTime.setText(messages.get(position).getTime());
                break;
            case 1://rec message
                MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                Log.v("message", messages.get(position).getMessage());
                vh2.recName.setText(messages.get(position).getSenderID());
                vh2.recMessage.setText(messages.get(position).getMessage());
                vh2.recTime.setText(messages.get(position).getTime());

                break;
            case 2://sen image
                final MyViewHolder3 vh3 = (MyViewHolder3) viewHolder;
                Picasso.get().load(messages.get(position).getMessage()).into(vh3.senImage);
                vh3.senImageTime.setText(messages.get(position).getTime());
                //0001
                vh3.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(messages.get(position).getMessage()));
                        vh3.itemView.getContext().startActivity(intent);
                    }
                });
                break;
            case 3://rec image
                final MyViewHolder4 vh4 = (MyViewHolder4) viewHolder;
                Picasso.get().load(messages.get(position).getMessage()).into(vh4.recImage);
                vh4.recImageTime.setText(messages.get(position).getTime());
                vh4.recName.setText(messages.get(position).getSenderID());
                //Picasso.get().load(photo).into(vh4.recPhoto);

                vh4.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(messages.get(position).getMessage()));
                        vh4.itemView.getContext().startActivity(intent);
                    }
                });
                break;
            case 4://sen file
                final MyViewHolder5 vh5 = (MyViewHolder5) viewHolder;
                vh5.senTime.setText(messages.get(position).getTime());
                String msg = messages.get(position).message;
                String fName=msg.substring(msg.indexOf("%2F")+3,msg.indexOf("?alt"));
                Log.v("check fName",fName);
                final String fileName = msg.substring(0, msg.indexOf("##"));
                vh5.senFileName.setText(fName);
                Log.v("check filename", fileName);
                Log.v("check msg", msg);
                vh5.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileName));
                        vh5.itemView.getContext().startActivity(intent);
                    }
                });
                break;

            case 5://rec file
                final MyViewHolder6 vh6 = (MyViewHolder6) viewHolder;
                String msg1 = messages.get(position).message;
                final String fileName1 = msg1.substring(0, msg1.indexOf("##"));
                String fName1=msg1.substring(msg1.indexOf("%2F")+3,msg1.indexOf("?alt"));
                Log.v("check fName",fName1);
                vh6.recFileName.setText(fName1);
                vh6.recTime.setText(messages.get(position).getTime());
                vh6.recName.setText(messages.get(position).getSenderID());
                Log.v("check filename1", fileName1);
                Log.v("check msg1", msg1);
                vh6.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileName1));
                        vh6.itemView.getContext().startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView senMessage, senTime;
        RelativeLayout senRow;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            senMessage = itemView.findViewById(R.id.row_sender_msg);
            senTime = itemView.findViewById(R.id.row_sender_time);
            senRow = itemView.findViewById(R.id.row_sender);

        }
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView recMessage, recTime,recName;
        RelativeLayout recRow;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            recName=itemView.findViewById(R.id.row_rm_username);
            recMessage = itemView.findViewById(R.id.row_rm_msg);
            recTime = itemView.findViewById(R.id.row_rm_time);
            recRow = itemView.findViewById(R.id.row_rec);

        }
    }

    public class MyViewHolder3 extends RecyclerView.ViewHolder {
        ImageView senImage;
        TextView senImageTime;
        RelativeLayout senImageRow;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);
            senImage = itemView.findViewById(R.id.sender_image);
            senImageTime = itemView.findViewById(R.id.sender_time);
            senImageRow = itemView.findViewById(R.id.row_sender_image);

        }
    }

    public class MyViewHolder4 extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recImageTime,recName;
        RelativeLayout recImageRow;

        public MyViewHolder4(@NonNull View itemView) {
            super(itemView);
            recName=itemView.findViewById(R.id.file_rec_sender);
            recImage = itemView.findViewById(R.id.row_rec_image);
            recImageTime = itemView.findViewById(R.id.row_rec_time);
            recImageRow = itemView.findViewById(R.id.row_rec);

        }
    }


    public class MyViewHolder5 extends RecyclerView.ViewHolder {
        TextView senFileName,senTime;
        RelativeLayout senFile;

        public MyViewHolder5(@NonNull View itemView) {
            super(itemView);
            senFileName = itemView.findViewById(R.id.file_sender_msg);
            senFile = itemView.findViewById(R.id.row_file_sender);
            senTime=itemView.findViewById(R.id.file_sender_time);

        }
    }

    public class MyViewHolder6 extends RecyclerView.ViewHolder {
        TextView recFileName,recName,recTime;
        RelativeLayout recFile;

        public MyViewHolder6(@NonNull View itemView) {
            super(itemView);
            recName=itemView.findViewById(R.id.file_rcv_username);
            recFileName = itemView.findViewById(R.id.file_receiver_msg);
            recFile = itemView.findViewById(R.id.row_file_receiver);
            recTime=itemView.findViewById(R.id.file_rcv_time);
        }
    }
}