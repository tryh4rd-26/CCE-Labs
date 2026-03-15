package com.example.q3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMovie, spinnerTheatre;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private ToggleButton toggleTicketType;
    private Button btnBookNow, btnReset;

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

        // Initialize views
        spinnerMovie = findViewById(R.id.spinnerMovie);
        spinnerTheatre = findViewById(R.id.spinnerTheatre);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        toggleTicketType = findViewById(R.id.toggleTicketType);
        btnBookNow = findViewById(R.id.btnBookNow);
        btnReset = findViewById(R.id.btnReset);

        // Populate Spinners
        String[] movies = {"Inception", "Interstellar", "The Dark Knight", "Oppenheimer", "The Prestige"};
        String[] theatres = {"PVR Cinemas", "INOX", "Cinepolis", "Carnival Cinemas", "Miraj Cinemas"};

        ArrayAdapter<String> movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, movies);
        movieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMovie.setAdapter(movieAdapter);

        ArrayAdapter<String> theatreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, theatres);
        theatreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheatre.setAdapter(theatreAdapter);

        // Premium ticket logic: Clickable only after 12:00 PM
        toggleTicketType.setOnCheckedChangeListener((buttonView, isChecked) -> updateButtonState());
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateButtonState());

        btnBookNow.setOnClickListener(v -> {
            if (validateInputs()) {
                bookTicket();
            }
        });

        btnReset.setOnClickListener(v -> resetFields());

        // Initial check
        updateButtonState();
    }

    private void updateButtonState() {
        if (toggleTicketType.isChecked()) {
            int hour = timePicker.getHour();
            // 12:00 PM is hour 12. "after 12:00 PM" usually means 12:00:01 and onwards, 
            // but in common app logic it means 12:00 PM onwards (hour >= 12).
            if (hour < 12) {
                btnBookNow.setEnabled(false);
                Toast.makeText(this, "Premium tickets only available for shows after 12:00 PM", Toast.LENGTH_SHORT).show();
            } else {
                btnBookNow.setEnabled(true);
            }
        } else {
            btnBookNow.setEnabled(true);
        }
    }

    private boolean validateInputs() {
        if (spinnerMovie.getSelectedItem() == null || spinnerTheatre.getSelectedItem() == null) {
            Toast.makeText(this, "Please select movie and theatre", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void bookTicket() {
        String movie = spinnerMovie.getSelectedItem().toString();
        String theatre = spinnerTheatre.getSelectedItem().toString();
        String date = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
        
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String amPm = (hour >= 12) ? "PM" : "AM";
        int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        String time = String.format("%02d:%02d %s", displayHour, minute, amPm);
        
        String ticketType = toggleTicketType.isChecked() ? "Premium" : "Standard";

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("movie", movie);
        intent.putExtra("theatre", theatre);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        intent.putExtra("ticketType", ticketType);
        startActivity(intent);
    }

    private void resetFields() {
        spinnerMovie.setSelection(0);
        spinnerTheatre.setSelection(0);
        
        Calendar calendar = Calendar.getInstance();
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
        
        toggleTicketType.setChecked(false);
        updateButtonState();
        
        Toast.makeText(this, "Fields Reset", Toast.LENGTH_SHORT).show();
    }
}