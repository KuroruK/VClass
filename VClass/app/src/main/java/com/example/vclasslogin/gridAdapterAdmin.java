package com.example.vclasslogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import android.support.v4.app.NotificationCompat;


public class gridAdapterAdmin extends BaseAdapter {
    ArrayList names;
    public static Activity activity;

    public gridAdapterAdmin(Activity activity, ArrayList names) {
        this.activity = activity;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.grid_layout, null);


}
        TextView textView = (TextView)v.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageHolder);
        if(names.get(position).toString().equals("Manage Teachers"))
        {
            imageView.setImageResource(R.drawable.teacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchinIntent = new Intent(activity, ListTeacherActivity.class);
                    activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }
        else if(names.get(position).toString().equals("Manage Students"))
        {
            imageView.setImageResource(R.drawable.student);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchinIntent = new Intent(activity, ListStudentActivity.class);
                    activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }

        else if(names.get(position).toString().equals("Manage Timetable"))
        {
            imageView.setImageResource(R.drawable.timetable);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchinIntent = new Intent(activity, timeTableMainActivity.class);
                    launchinIntent.putExtra("type: ","admin");
                    activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }else if(names.get(position).toString().equals("Manage Courses"))
        {
            imageView.setImageResource(R.drawable.courses);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchinIntent = new Intent(activity, ListCourseActivity.class);
                    activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }
        else if(names.get(position).toString().equals("View Teacher"))
        {
            imageView.setImageResource(R.drawable.viewteacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                          Intent launchinIntent = new Intent(activity, ListTeacherActivity.class);
                          activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }else if(names.get(position).toString().equals("Register Teacher"))
        {
            imageView.setImageResource(R.drawable.addteacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                          Intent launchinIntent = new Intent(activity, registerTeacherActivity.class);
                         activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }else if(names.get(position).toString().equals("Edit Teacher"))
        {
            imageView.setImageResource(R.drawable.editteacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //      Intent launchinIntent = new Intent(activity, scheduler.class);
                    //      activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }else if(names.get(position).toString().equals("Remove Teacher"))
        {
            imageView.setImageResource(R.drawable.removeteacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //      Intent launchinIntent = new Intent(activity, scheduler.class);
                    //      activity.startActivity(launchinIntent);
                }
            });
            Animation anim = new ScaleAnimation(
                    0.95f, 1f, // Start and end values for the X axis scaling
                    0.95f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(2000);
            anim.setRepeatMode(Animation.INFINITE);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);

        }
        textView.setText(names.get(position).toString());
        return v;
    }

}
