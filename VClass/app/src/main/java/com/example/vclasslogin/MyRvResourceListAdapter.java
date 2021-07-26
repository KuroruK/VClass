package com.example.vclasslogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

public class MyRvResourceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    List<Resource> resources;
    String username, id;
    FirebaseDatabase database;
    DatabaseReference reference;

    public MyRvResourceListAdapter(Context c, List<Resource> resources, String username, String id) {
        this.c = c;
        this.resources = resources;
        this.username = username;
        this.id = id;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Resources");
    }

    @Override
    public int getItemViewType(int position) {
        switch (username) {
            case "view":
                return 0;
            case "attach":
                return 1;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.resource_row, viewGroup, false);
                viewHolder = new MyViewHolder(v1);
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.row_student_tasks, viewGroup, false);
                viewHolder = new MyViewHolder1(v2);
                break;
            default:
                break;
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (this.getItemViewType(position)) {
            case 0://view
                final MyRvResourceListAdapter.MyViewHolder vh1 = (MyRvResourceListAdapter.MyViewHolder) viewHolder;
                vh1.title.setText(resources.get(position).getTitle());
                vh1.time.setText(resources.get(position).getTime());
                vh1.teacher.setText(resources.get(position).getTeacherID());
                //Log.v("myLog", resources.get(position).getTeacherID());
                vh1.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resources.get(position).getResource()));
                        vh1.itemView.getContext().startActivity(intent);
                    }
                });
                break;
            case 1://edit
                final MyRvResourceListAdapter.MyViewHolder1 vh2 = (MyRvResourceListAdapter.MyViewHolder1) viewHolder;
                vh2.fileName.setText(resources.get(position).getTitle());

                vh2.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resources.get(position).getResource()));
                        vh2.itemView.getContext().startActivity(intent);
                    }
                });

                vh2.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (resources.size() == 1) {
                            resources.remove(0);
                            reference.child(id).removeValue();

                            notifyItemRemoved(0);

                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (resources == null)
            return 0;
        return resources.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, time, teacher;
        ImageView download;
        RelativeLayout rl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.row_rs_title);
            teacher = itemView.findViewById(R.id.row_rs_teacher_1);
            time = itemView.findViewById(R.id.row_rs_time_1);
            download = itemView.findViewById(R.id.row_rs_download);
            rl = itemView.findViewById(R.id.resource_row);
        }
    }

    public static class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView download, cancel;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.row_ss_fname);
            cancel = itemView.findViewById(R.id.row_std_s_cancel);
            download = itemView.findViewById(R.id.row_std_s_download);
        }
    }
}
