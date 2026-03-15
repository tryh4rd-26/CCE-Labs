package com.example.midsem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomButton;
import androidx.appcompat.app.AppCompatActivity;

public class CampusDashboardActivity extends AppCompatActivity {
    private static final String TAG = "MAD_APP";
    
    ToggleButton toggleAlerts;
    Switch switchWifi;
    SeekBar seekBarColor;
    ZoomButton zoomIn, zoomOut;
    RadioGroup rgTransport;
    CheckBox cbLibrary, cbGym, cbCafeteria;
    Button btnNextPage;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_dashboard);

        root = findViewById(R.id.dashboard_root);
        toggleAlerts = findViewById(R.id.toggleAlerts);
        switchWifi = findViewById(R.id.switchWifi);
        seekBarColor = findViewById(R.id.seekBarColor);
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        rgTransport = findViewById(R.id.rgTransport);
        cbLibrary = findViewById(R.id.cbLibrary);
        cbGym = findViewById(R.id.cbGym);
        cbCafeteria = findViewById(R.id.cbCafeteria);
        btnNextPage = findViewById(R.id.btnNextPage);

        // ToggleButton
        toggleAlerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String msg = "Alerts: " + (isChecked ? "ON" : "OFF");
                Log.d(TAG, msg);
                Toast.makeText(CampusDashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Switch
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String msg = "WiFi: " + (isChecked ? "Enabled" : "Disabled");
                Log.d(TAG, msg);
                Toast.makeText(CampusDashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // SeekBar
        seekBarColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                root.setBackgroundColor(Color.rgb(progress, 255 - progress, 200));
                Log.d(TAG, "SeekBar value: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // ZoomButton
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Map Zoom In");
                Toast.makeText(CampusDashboardActivity.this, "Zoom In", Toast.LENGTH_SHORT).show();
            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Map Zoom Out");
                Toast.makeText(CampusDashboardActivity.this, "Zoom Out", Toast.LENGTH_SHORT).show();
            }
        });

        // RadioGroup
        rgTransport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                if (rb != null) {
                    Log.d(TAG, "Transport selected: " + rb.getText());
                    Toast.makeText(CampusDashboardActivity.this, "Mode: " + rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // CheckBoxes
        CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String msg = buttonView.getText() + (isChecked ? " selected" : " unselected");
                Log.d(TAG, msg);
                Toast.makeText(CampusDashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        };
        cbLibrary.setOnCheckedChangeListener(cbListener);
        cbGym.setOnCheckedChangeListener(cbListener);
        cbCafeteria.setOnCheckedChangeListener(cbListener);

        // Navigation
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CampusDashboardActivity.this, CampusInformationActivity.class);
                startActivity(i);
            }
        });
    }
}