package com.example.currentmode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleMode;
    private ImageView imgMode;
    private Button btnChangeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleMode = findViewById(R.id.toggleMode);
        imgMode = findViewById(R.id.imgMode);
        btnChangeMode = findViewById(R.id.btnChangeMode);

        // Initial state
        updateUI(toggleMode.isChecked());

        // ToggleButton listener
        toggleMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateUI(isChecked);
        });

        // Change Mode button listener
        btnChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = !toggleMode.isChecked();
                toggleMode.setChecked(newState); // this will trigger updateUI via listener
            }
        });
    }

    private void updateUI(boolean isWifi) {
        if (isWifi) {
            imgMode.setImageResource(R.drawable.ic_wifi);
            Toast.makeText(this, "Current Mode: Wi-Fi", Toast.LENGTH_SHORT).show();
        } else {
            imgMode.setImageResource(R.drawable.ic_mobiledata);
            Toast.makeText(this, "Current Mode: Mobile Data", Toast.LENGTH_SHORT).show();
        }
    }
}
