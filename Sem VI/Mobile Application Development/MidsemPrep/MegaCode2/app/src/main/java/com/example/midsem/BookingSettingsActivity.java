package com.example.midsem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookingSettingsActivity extends AppCompatActivity {
    private static final String TAG = "MAD_APP";

    Spinner spinnerDept;
    DatePicker datePicker;
    TimePicker timePicker;
    Button btnShowPopup, btnSubmit, btnReset, btnBack;

    String[] depts = {"CCE", "IT", "CS", "AI", "Data Science"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_settings);

        spinnerDept = findViewById(R.id.spinnerDept);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnShowPopup = findViewById(R.id.btnShowPopup);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        // Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, depts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(adapter);

        spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Spinner selected: " + depts[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Popup Menu
        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BookingSettingsActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d(TAG, "Popup menu clicked: " + item.getTitle());
                        Toast.makeText(BookingSettingsActivity.this, "Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                String bookingInfo = "Dept: " + spinnerDept.getSelectedItem() + 
                                    "\nDate: " + day + "/" + month + "/" + year +
                                    "\nTime: " + hour + ":" + minute;
                
                Log.d(TAG, "Booking Submitted: " + bookingInfo);
                Toast.makeText(BookingSettingsActivity.this, "Booking Successful!", Toast.LENGTH_LONG).show();
            }
        });

        // Reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDept.setSelection(0);
                Log.d(TAG, "Form Reset");
                Toast.makeText(BookingSettingsActivity.this, "Reset Done", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingSettingsActivity.this, CampusDashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "Options menu clicked: " + item.getTitle());
        Toast.makeText(this, "Menu: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        
        if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}