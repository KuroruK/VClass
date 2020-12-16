package com.example.vclasslogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LiveStudentClassActivity extends AppCompatActivity {
    ToggleButton micToggle, voiceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_student_class);

        micToggle = findViewById(R.id.live_std_mic);
        voiceToggle = findViewById(R.id.live_std_voice2);
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