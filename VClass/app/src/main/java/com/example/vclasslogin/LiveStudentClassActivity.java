package com.example.vclasslogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LiveStudentClassActivity extends AppCompatActivity {
    ToggleButton micToggle, voiceToggle;
    ImageView whiteboard;
    public static final String TAG = "AndroidDrawing";
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_student_class);

        micToggle = findViewById(R.id.live_std_mic);
        voiceToggle = findViewById(R.id.live_std_voice2);
        whiteboard=findViewById(R.id.live_std_whiteboard);


        whiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key="-MNUK-Lu_PfeT0nqRHqh";//firebase boardmeta key/id
                Log.i(TAG, "Opening board "+key);
                Toast.makeText(LiveStudentClassActivity.this, "Opening board: "+key, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LiveStudentClassActivity.this, DrawingActivity.class);
                intent.putExtra("FIREBASE_URL", FIREBASE_URL);
                intent.putExtra("BOARD_ID", key);
                startActivity(intent);
            }
        });
    }

    public void micToggleClick(View view) {
        if (micToggle.isChecked()) {
            //micToggle.setChecked(false);
            Toast.makeText(this, "Mic muted!", Toast.LENGTH_SHORT).show();
        }
        else {
            //micToggle.setChecked(true);
            Toast.makeText(this, "Mic unmuted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void voiceToggleClick(View view) {
        if (voiceToggle.isChecked()) {
            Toast.makeText(this, "Class volume on!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Class volume off!", Toast.LENGTH_SHORT).show();
        }
    }
}