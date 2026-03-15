package com.example.q2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvSource = findViewById(R.id.tvSource);
        TextView tvDestination = findViewById(R.id.tvDestination);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTripType = findViewById(R.id.tvTripType);
        Button btnBack = findViewById(R.id.btnBack);

        String source = getIntent().getStringExtra("SOURCE");
        String destination = getIntent().getStringExtra("DESTINATION");
        String date = getIntent().getStringExtra("DATE");
        String tripType = getIntent().getStringExtra("TRIP_TYPE");

        tvSource.setText("Source: " + source);
        tvDestination.setText("Destination: " + destination);
        tvDate.setText("Travel Date: " + date);
        tvTripType.setText("Trip Type: " + tripType);

        btnBack.setOnClickListener(v -> finish());
    }
}