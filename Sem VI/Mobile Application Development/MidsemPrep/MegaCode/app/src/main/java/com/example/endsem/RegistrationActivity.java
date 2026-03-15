package com.example.endsem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_REG";
    EditText etName, etRegNo;
    RadioGroup rgGender;
    ToggleButton tbHosteller;
    Switch swNotify;
    CheckBox cbSports, cbCoding, cbMusic, cbRobotics;
    Spinner spDept;
    Button btnPickDate, btnPickTime, btnReset, btnSave, btnGoToCourses, btnViewRecords;
    TextView tvDate, tvTime;
    SeekBar sbColor;
    ScrollView rootLayout;
    DatabaseHelper db;

    String selectedDate = "", selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etName = findViewById(R.id.etRegName);
        etRegNo = findViewById(R.id.etRegNumber);
        rgGender = findViewById(R.id.rgGender);
        tbHosteller = findViewById(R.id.tbHosteller);
        swNotify = findViewById(R.id.swNotifications);
        cbSports = findViewById(R.id.cbSports);
        cbCoding = findViewById(R.id.cbCoding);
        cbMusic = findViewById(R.id.cbMusic);
        cbRobotics = findViewById(R.id.cbRobotics);
        spDept = findViewById(R.id.spDept);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnReset = findViewById(R.id.btnRegReset);
        btnSave = findViewById(R.id.btnRegSave);
        btnGoToCourses = findViewById(R.id.btnGoToCourses);
        btnViewRecords = findViewById(R.id.btnViewRecords);
        tvDate = findViewById(R.id.tvSelectedDate);
        tvTime = findViewById(R.id.tvSelectedTime);
        sbColor = findViewById(R.id.sbColor);
        rootLayout = findViewById(R.id.registration_root);

        String[] depts = {"ICT", "CCE", "IT", "CSE", "Mechanical", "Electrical"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, depts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDept.setAdapter(adapter);

        btnPickDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvDate.setText(selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnPickTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + minute;
                tvTime.setText(selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int color = Color.rgb(progress, 255 - progress, 150);
                rootLayout.setBackgroundColor(color);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnReset.setOnClickListener(v -> {
            etName.setText("");
            etRegNo.setText("");
            rgGender.clearCheck();
            tbHosteller.setChecked(false);
            swNotify.setChecked(false);
            cbSports.setChecked(false);
            cbCoding.setChecked(false);
            cbMusic.setChecked(false);
            cbRobotics.setChecked(false);
            spDept.setSelection(0);
            tvDate.setText("No Date Selected");
            tvTime.setText("No Time Selected");
            rootLayout.setBackgroundColor(Color.WHITE);
            sbColor.setProgress(0);
            Toast.makeText(this, "Form Reset", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String regno = etRegNo.getText().toString();
            String dept = spDept.getSelectedItem().toString();
            int selectedGenderId = rgGender.getCheckedRadioButtonId();
            String gender = (selectedGenderId != -1) ? ((RadioButton) findViewById(selectedGenderId)).getText().toString() : "N/A";
            
            StringBuilder interests = new StringBuilder();
            if (cbSports.isChecked()) interests.append("Sports ");
            if (cbCoding.isChecked()) interests.append("Coding ");
            if (cbMusic.isChecked()) interests.append("Music ");
            if (cbRobotics.isChecked()) interests.append("Robotics ");

            if (db.insertStudent(name, regno, dept, gender, interests.toString().trim(), selectedDate, selectedTime)) {
                Toast.makeText(this, "Student Record Saved", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Record Saved: " + name);
            } else {
                Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewRecords.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to Database Manager");
            startActivity(new Intent(this, DatabaseManagerActivity.class));
        });

        btnGoToCourses.setOnClickListener(v -> {
            startActivity(new Intent(this, CourseExplorerActivity.class));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
