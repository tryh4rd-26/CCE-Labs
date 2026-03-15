package com.example.q1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SummaryActivity extends AppCompatActivity {

    TextView tvDetails;
    Button btnConfirm, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        tvDetails = findViewById(R.id.tv_details);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnEdit = findViewById(R.id.btn_edit);

        String type = getIntent().getStringExtra("TYPE");
        String vehNum = getIntent().getStringExtra("VEH_NUM");
        String rcNum = getIntent().getStringExtra("RC_NUM");

        String displayText = "Vehicle Type: " + type + "\n" +
                             "Vehicle Number: " + vehNum + "\n" +
                             "RC Number: " + rcNum;
        
        tvDetails.setText(displayText);

        btnConfirm.setOnClickListener(v -> {
            String serialNumber = "PN-" + (1000 + new Random().nextInt(9000));
            Toast.makeText(this, "Parking Allotted! Serial Number: " + serialNumber, Toast.LENGTH_LONG).show();
            // Optionally finish or return to main
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            // Just go back to MainActivity
            finish();
        });
    }
}
