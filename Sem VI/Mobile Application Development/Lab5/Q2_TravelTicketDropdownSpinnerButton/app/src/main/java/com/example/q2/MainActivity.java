package com.example.q2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSource, spinnerDestination;
    private DatePicker datePicker;
    private ToggleButton toggleTripType;
    private Button btnSubmit, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSource = findViewById(R.id.spinnerSource);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        datePicker = findViewById(R.id.datePicker);
        toggleTripType = findViewById(R.id.toggleTripType);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);

        String[] cities = {"New York", "London", "Paris", "Tokyo", "Dubai", "Mumbai", "Singapore"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSource.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> {
            String source = spinnerSource.getSelectedItem().toString();
            String destination = spinnerDestination.getSelectedItem().toString();
            String date = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
            String tripType = toggleTripType.isChecked() ? "Round Trip" : "One Way";

            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("SOURCE", source);
            intent.putExtra("DESTINATION", destination);
            intent.putExtra("DATE", date);
            intent.putExtra("TRIP_TYPE", tripType);
            startActivity(intent);
        });

        btnReset.setOnClickListener(v -> resetFields());
    }

    private void resetFields() {
        spinnerSource.setSelection(0);
        spinnerDestination.setSelection(0);
        toggleTripType.setChecked(false);

        Calendar calendar = Calendar.getInstance();
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }
}