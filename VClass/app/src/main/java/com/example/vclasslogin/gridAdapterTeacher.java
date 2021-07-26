package com.example.vclasslogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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


public class GridAdapterTeacher extends BaseAdapter {
    ArrayList names;
    public static Activity activity;
    String username;

    public GridAdapterTeacher(Activity activity, ArrayList names, String username) {
        this.activity = activity;
        this.names = names;
        this.username = username;
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
        TextView textView = (TextView) v.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageHolder);
        if (names.get(position).toString().equals("Schedule Class")) {
            imageView.setImageResource(R.drawable.schedule);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //      Intent launchinIntent = new Intent(activity, scheduleClassActivity.class);
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

        } else if (names.get(position).toString().equals("Schedule")) {
            imageView.setImageResource(R.drawable.schedule);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchinIntent = new Intent(activity, view_schedule_activity.class);
                    launchinIntent.putExtra("type", "teacher");
                    launchinIntent.putExtra("name", username);
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

        } else if (names.get(position).toString().equals("Timetable")) {
            imageView.setImageResource(R.drawable.timetable);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, timeTableMainActivity.class);
                    intent.putExtra("type", "teacher");
                    intent.putExtra("name", username);
                    activity.startActivity(intent);
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

        } else if (names.get(position).toString().equals("Manage Tasks")) {
            imageView.setImageResource(R.drawable.tasks);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //      Intent launchingIntent = new Intent(activity, scheduler.class);
                    //      activity.startActivity(launchingIntent);
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

        } else if (names.get(position).toString().equals("Classes")) {
            imageView.setImageResource(R.drawable.main_class);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, TeacherClassesActivity.class);
                    intent.putExtra("type", "teacher");
                    intent.putExtra("name", username);
                    activity.startActivity(intent);
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

        } else if (names.get(position).toString().equals("Chat")) {
            imageView.setImageResource(R.drawable.chat);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.v("teacher_chat", username);
                    Intent intent = new Intent(activity, TeacherGroupChatsActivity.class);
                    intent.putExtra("type", "teacher");
                    intent.putExtra("name", username);
                    activity.startActivity(intent);
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

    public static class createRequest extends DialogFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View v = inflater.inflate(R.layout.pick_period, null);
            final DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
            final EditText hour = (EditText) v.findViewById(R.id.periodID);
            final Spinner spn = (Spinner) v.findViewById(R.id.spinnerSubject);

            String qu = "SELECT DISTINCT sub FROM NOTES";
            ArrayList<String> subs = new ArrayList<>();
            subs.add("Not Specified");
            Cursor cr = StudentView.handler.execQuery(qu);
            if (cr != null) {
                cr.moveToFirst();
                while (!cr.isAfterLast()) {
                    subs.add(cr.getString(0));
                    Log.d("gridAdapter.class", "Cached " + cr.getString(0));
                    cr.moveToNext();
                }
            } else
                Log.d("gridAdapter.class", "No SUBS" + cr.getString(0));

            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, subs);
            assert spn != null;
            spn.setAdapter(adapterSpinner);
            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            int day = datePicker.getDayOfMonth();
                            int month = datePicker.getMonth() + 1;
                            int year = datePicker.getYear();
                            String date = year + "-" + month + "-" + day;
                            String subject = spn.getSelectedItem().toString();
                            String qx = "SELECT title FROM NOTES where sub = '" + subject + "'";
                            Cursor cr = StudentView.handler.execQuery(qx);
                            String subnames = "";
                            if (cr != null) {
                                cr.moveToFirst();
                                while (!cr.isAfterLast()) {
                                    subnames += (cr.getString(0)) + "\n";
                                    cr.moveToNext();
                                }
                            }
                            //      makeNotification(subnames);

                            Cursor cursor = StudentView.handler.execQuery("SELECT * FROM ATTENDANCE WHERE datex = '" +
                                    date + "' AND hour = " + hour.getText() + ";");
                            if (cursor == null || cursor.getCount() == 0) {
                                //    Intent launchinIntent = new Intent(StudentView.activity, attendanceActivity.class);
                                //    launchinIntent.putExtra("DATE", date);
                                //    launchinIntent.putExtra("PERIOD", hour.getText().toString());
                                //    StudentView.activity.startActivity(launchinIntent);
                            } else {
                                Toast.makeText(getActivity(), "Period Already Added", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }
    }

   /* public static void makeNotification(String userIntrouble) {
        Log.d("NOTIFICATION","Building..........");
        Intent notificationIntent = new Intent(activity.getApplicationContext(), noteActivity.class);
//        notificationIntent.putExtra(MainListAdapter.USER_EMAIL,userIntrouble);
//        notificationIntent.putExtra(MainListAdapter.IS_EMERGENCY, true);
        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, notificationIntent,
                0);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        Uri ring = Uri.parse(sharedPrefs.getString("Notification_Sound", Settings.System.DEFAULT_RINGTONE_URI.toString()));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getBaseContext())
                .setTicker("Ticker Title").setContentTitle("Notes Are Available For this subject")
                .setSmallIcon(R.drawable.ic_notes)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(userIntrouble))
                .setContentIntent(pIntent)
                .setSound(ring);

        Notification noti = builder.build();
        noti.contentIntent = pIntent;
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }*/
}
