package com.example.q1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerType;
    private EditText etVehNum, etRcNum;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerType = findViewById(R.id.spinner_type);
        etVehNum = findViewById(R.id.et_veh_num);
        etRcNum = findViewById(R.id.et_rc_num);
        btnSubmit = findViewById(R.id.btn_submit);

        // Populate Spinner with Vehicle Types
        String[] vehicleTypes = {"Car", "Bike", "Scooter", "Bus", "Truck"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicleTypes);
        spinnerType.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String vehNum = etVehNum.getText().toString().trim();
            String rcNum = etRcNum.getText().toString().trim();

            if (vehNum.isEmpty() || rcNum.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Move to SummaryActivity
            Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
            intent.putExtra("TYPE", type);
            intent.putExtra("VEH_NUM", vehNum);
            intent.putExtra("RC_NUM", rcNum);
            startActivity(intent);
        });
    }
}
