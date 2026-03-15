package com.example.exam;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentFormActivity extends AppCompatActivity {

    private EditText etName, etRegNo, etPhone, etEmail;
    private RadioGroup rgGender;
    private CheckBox cbJava, cbPython, cbWeb, cbML, cbAndroid;
    private Switch swHostel;
    private ToggleButton tbScholarship;
    private Spinner spDepartment;
    private TextView tvSelectedDOB, tvSelectedTime;
    private Button btnSubmitForm, btnResetForm, btnViewStudents, btnShowDatePicker, btnShowTimePicker;
    private SeekBar sbTextSize;
    private ImageView ivProfile;
    private ZoomButton zbZoom;

    private static final String TAG = "MAD_APP_FORM";
    private String selectedDOB = "", selectedTime = "";
    private int editIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.etName);
        etRegNo = findViewById(R.id.etRegNo);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        rgGender = findViewById(R.id.rgGender);
        cbJava = findViewById(R.id.cbJava);
        cbPython = findViewById(R.id.cbPython);
        cbWeb = findViewById(R.id.cbWeb);
        cbML = findViewById(R.id.cbML);
        cbAndroid = findViewById(R.id.cbAndroid);
        swHostel = findViewById(R.id.swHostel);
        tbScholarship = findViewById(R.id.tbScholarship);
        spDepartment = findViewById(R.id.spDepartment);
        tvSelectedDOB = findViewById(R.id.tvSelectedDOB);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        btnSubmitForm = findViewById(R.id.btnSubmitForm);
        btnResetForm = findViewById(R.id.btnResetForm);
        btnViewStudents = findViewById(R.id.btnViewStudents);
        btnShowDatePicker = findViewById(R.id.btnShowDatePicker);
        btnShowTimePicker = findViewById(R.id.btnShowTimePicker);
        sbTextSize = findViewById(R.id.sbTextSize);
        ivProfile = findViewById(R.id.ivProfile);
        zbZoom = findViewById(R.id.zbZoom);

        // Spinner setup
        String[] depts = {"CCE", "IT", "CS", "AI", "Data Science"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, depts);
        spDepartment.setAdapter(adapter);

        // Check for edit mode
        editIndex = getIntent().getIntExtra("EDIT_INDEX", -1);
        if (editIndex != -1) {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Edit Student");
            btnSubmitForm.setText("Update Details");
            prefillForm(editIndex);
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Add New Student");
        }

        // Date Picker
        btnShowDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                selectedDOB = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                tvSelectedDOB.setText("DOB: " + selectedDOB);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker
        btnShowTimePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                selectedTime = hourOfDay + ":" + minute1;
                tvSelectedTime.setText("Lab Time: " + selectedTime);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        // SeekBar
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSelectedDOB.setTextSize(progress + 10);
                tvSelectedTime.setTextSize(progress + 10);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // ZoomButton
        zbZoom.setOnClickListener(v -> {
            ivProfile.setScaleX(ivProfile.getScaleX() + 0.1f);
            ivProfile.setScaleY(ivProfile.getScaleY() + 0.1f);
        });

        // Submit Form
        btnSubmitForm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String regNo = etRegNo.getText().toString();
            
            if(name.isEmpty() || regNo.isEmpty()){
                Toast.makeText(this, "Please enter Name and Reg No", Toast.LENGTH_SHORT).show();
                return;
            }

            int genderId = rgGender.getCheckedRadioButtonId();
            String gender = genderId == -1 ? "N/A" : ((RadioButton) findViewById(genderId)).getText().toString();

            List<String> skills = new ArrayList<>();
            if (cbJava.isChecked()) skills.add("Java");
            if (cbPython.isChecked()) skills.add("Python");
            if (cbWeb.isChecked()) skills.add("Web Development");
            if (cbML.isChecked()) skills.add("Machine Learning");
            if (cbAndroid.isChecked()) skills.add("Android");

            Student student = new Student(name, regNo, etPhone.getText().toString(), etEmail.getText().toString(), 
                gender, skills, swHostel.isChecked(), tbScholarship.isChecked(), 
                spDepartment.getSelectedItem().toString(), selectedDOB, selectedTime);
            
            if (editIndex != -1) {
                DataManager.studentList.set(editIndex, student);
                Toast.makeText(this, "Student Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                DataManager.studentList.add(student);
                Toast.makeText(this, "Student Registered Successfully", Toast.LENGTH_SHORT).show();
            }

            navigateToView();
        });

        btnResetForm.setOnClickListener(v -> resetForm());
        btnViewStudents.setOnClickListener(v -> navigateToView());
    }

    private void prefillForm(int index) {
        Student s = DataManager.studentList.get(index);
        etName.setText(s.getName());
        etRegNo.setText(s.getRegNo());
        etPhone.setText(s.getPhone());
        etEmail.setText(s.getEmail());
        
        if (s.getGender().equals("Male")) rgGender.check(R.id.rbMale);
        else if (s.getGender().equals("Female")) rgGender.check(R.id.rbFemale);
        else if (s.getGender().equals("Other")) rgGender.check(R.id.rbOther);

        List<String> skills = s.getSkills();
        cbJava.setChecked(skills.contains("Java"));
        cbPython.setChecked(skills.contains("Python"));
        cbWeb.setChecked(skills.contains("Web Development"));
        cbML.setChecked(skills.contains("Machine Learning"));
        cbAndroid.setChecked(skills.contains("Android"));

        swHostel.setChecked(s.isHostelResident());
        tbScholarship.setChecked(s.isScholarshipEligible());

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spDepartment.getAdapter();
        int pos = adapter.getPosition(s.getDepartment());
        spDepartment.setSelection(pos);

        selectedDOB = s.getDob();
        tvSelectedDOB.setText("DOB: " + selectedDOB);
        selectedTime = s.getPreferredLabTime();
        tvSelectedTime.setText("Lab Time: " + selectedTime);
    }

    private void resetForm() {
        editIndex = -1;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Add New Student");
        btnSubmitForm.setText("Submit Details");
        etName.setText(""); etRegNo.setText(""); etPhone.setText(""); etEmail.setText("");
        rgGender.clearCheck();
        cbJava.setChecked(false); cbPython.setChecked(false); cbWeb.setChecked(false); cbML.setChecked(false); cbAndroid.setChecked(false);
        swHostel.setChecked(false); tbScholarship.setChecked(false);
        selectedDOB = ""; selectedTime = "";
        tvSelectedDOB.setText("DOB: Not Selected"); tvSelectedTime.setText("Lab Time: Not Selected");
    }

    private void navigateToView() {
        Intent intent = new Intent(this, StudentListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_view_students) {
            navigateToView();
            return true;
        } else if (id == R.id.menu_register || id == R.id.menu_add_student) {
            resetForm();
            return true;
        } else if (id == R.id.menu_clear_data) {
            DataManager.studentList.clear();
            Toast.makeText(this, "All Data Cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_home) {
             Intent intent = new Intent(this, MainActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
             return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
