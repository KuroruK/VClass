package com.example.vclasslogin;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminGridAdapter extends BaseAdapter {
    ArrayList names;    // contains the names of menu items
    public static Activity activity;    // stores admin view activity
    String username;    // admin username

    // constructor
    public AdminGridAdapter(Activity activity, ArrayList names, String username) {
        this.activity = activity;
        this.names = names;
        this.username = username;
    }

    // returns total menu items size
    @Override
    public int getCount() {
        return names.size();
    }

    // returns particular menu item name from ArrayList by using item position
    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    // returns particular menu item id by using item position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // function to set the layout for each menu item and to animate them
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.grid_layout, null); // setting grid layout for menu items
        }

        // getting layout fields
        TextView textView = (TextView) v.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageHolder);

        // conditions to display items contained in names and animate them
        if (names.get(position).toString().equals("Manage Teachers")) {
            imageView.setImageResource(R.drawable.manage_teacher);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // launches other activity to display list of all teachers information
                    Intent launchingIntent = new Intent(activity, ListTeacherActivity.class);
                    launchingIntent.putExtra("username", username);
                    activity.startActivity(launchingIntent);
                }
            });

            // animates the menu item
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

        } else if (names.get(position).toString().equals("Manage Students")) {
            imageView.setImageResource(R.drawable.manage_student);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // launches other activity to display list of all students information
                    Intent launchingIntent = new Intent(activity, ListStudentActivity.class);
                    launchingIntent.putExtra("username", username);
                    activity.startActivity(launchingIntent);
                }
            });

            // animates the menu item
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

        } else if (names.get(position).toString().equals("Manage Timetable")) {
            imageView.setImageResource(R.drawable.timetable);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // launches other activity to display the timetable
                    Intent launchingIntent = new Intent(activity, TimeTableActivity.class);
                    launchingIntent.putExtra("username", username);
                    launchingIntent.putExtra("type", "admin");
                    activity.startActivity(launchingIntent);
                }
            });

            // animates the menu item
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

        } else if (names.get(position).toString().equals("Manage Courses")) {
            imageView.setImageResource(R.drawable.main_class);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // launches other activity to display list of all courses information
                    Intent launchingIntent = new Intent(activity, ListCourseActivity.class);
                    launchingIntent.putExtra("username", username);
                    activity.startActivity(launchingIntent);
                }
            });

            // animates the menu item
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

        // display the name of specific menu item in a textView
        textView.setText(names.get(position).toString());
        return v;
    }
}
