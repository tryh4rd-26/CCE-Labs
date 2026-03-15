package com.example.q3;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvMovie = findViewById(R.id.tvMovie);
        TextView tvTheatre = findViewById(R.id.tvTheatre);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvTicketType = findViewById(R.id.tvTicketType);
        TextView tvAvailableSeats = findViewById(R.id.tvAvailableSeats);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvMovie.setText("Movie: " + extras.getString("movie"));
            tvTheatre.setText("Theatre: " + extras.getString("theatre"));
            tvDate.setText("Date: " + extras.getString("date"));
            tvTime.setText("Time: " + extras.getString("time"));
            tvTicketType.setText("Ticket Type: " + extras.getString("ticketType"));
            tvAvailableSeats.setText("Available Seats: 45"); // Mocked value
        }
    }
}