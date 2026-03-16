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

    private EditText etField1, etField2, etField3, etField4;
    private RadioGroup rgGender;
    private LinearLayout llCheckBoxes, llMarksContainer;
    private List<CheckBox> dynamicCheckBoxes = new ArrayList<>();
    private List<EditText> dynamicMarksFields = new ArrayList<>();
    
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

        initViews();
        setupDynamicFields();
        setupToolbar();
        setupSpinner();
        checkEditMode();
        setupListeners();
    }

    private void initViews() {
        etField1 = findViewById(R.id.etField1);
        etField2 = findViewById(R.id.etField2);
        etField3 = findViewById(R.id.etField3);
        etField4 = findViewById(R.id.etField4);
        rgGender = findViewById(R.id.rgGender);
        llCheckBoxes = findViewById(R.id.llCheckBoxes);
        llMarksContainer = findViewById(R.id.llMarksContainer);

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

        // Modular labels from strings.xml
        etField1.setHint(getString(R.string.label_field1));
        etField2.setHint(getString(R.string.label_field2));
        etField3.setHint(getString(R.string.label_field3));
        etField4.setHint(getString(R.string.label_field4));
        ((TextView)findViewById(R.id.tvRadioLabel)).setText(getString(R.string.label_radio));
        ((TextView)findViewById(R.id.tvCheckLabel)).setText(getString(R.string.label_check));
        swHostel.setText(getString(R.string.label_switch));
        ((TextView)findViewById(R.id.tvSpinnerLabel)).setText(getString(R.string.label_spinner));
    }

    private void setupDynamicFields() {
        // Dynamic Checkboxes
        String[] skills = getResources().getStringArray(R.array.skills_array);
        for (String skill : skills) {
            CheckBox cb = new CheckBox(this);
            cb.setText(skill);
            llCheckBoxes.addView(cb);
            dynamicCheckBoxes.add(cb);
        }

        // Dynamic Marks/Price Fields
        String[] assessments = getResources().getStringArray(R.array.assessments_array);
        for (String item : assessments) {
            EditText et = new EditText(this);
            et.setHint("Enter score for: " + item);
            et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            llMarksContainer.addView(et);
            dynamicMarksFields.add(et);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapter);
    }

    private void checkEditMode() {
        editIndex = getIntent().getIntExtra("EDIT_INDEX", -1);
        if (editIndex != -1) {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.form_title_edit);
            btnSubmitForm.setText(R.string.update_details_btn);
            prefillForm(editIndex);
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.form_title_add);
        }
    }

    private void setupListeners() {
        btnShowDatePicker.setOnClickListener(v -> showDatePicker());
        btnShowTimePicker.setOnClickListener(v -> showTimePicker());
        
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float size = progress + 10;
                tvSelectedDOB.setTextSize(size);
                tvSelectedTime.setTextSize(size);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        zbZoom.setOnClickListener(v -> {
            ivProfile.setScaleX(ivProfile.getScaleX() + 0.1f);
            ivProfile.setScaleY(ivProfile.getScaleY() + 0.1f);
        });

        btnSubmitForm.setOnClickListener(v -> handleSubmit());
        btnResetForm.setOnClickListener(v -> resetForm());
        btnViewStudents.setOnClickListener(v -> navigateToView());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedDOB = day + "-" + (month + 1) + "-" + year;
            tvSelectedDOB.setText("Date: " + selectedDOB);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            selectedTime = hour + ":" + minute;
            tvSelectedTime.setText("Time: " + selectedTime);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void handleSubmit() {
        String f1 = etField1.getText().toString().trim();
        String f2 = etField2.getText().toString().trim();
        
        if(f1.isEmpty() || f2.isEmpty()){
            Toast.makeText(this, R.string.error_missing_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        int genderId = rgGender.getCheckedRadioButtonId();
        String radioVal = genderId == -1 ? "N/A" : ((RadioButton) findViewById(genderId)).getText().toString();

        List<String> selectedOptions = new ArrayList<>();
        for (CheckBox cb : dynamicCheckBoxes) {
            if (cb.isChecked()) selectedOptions.add(cb.getText().toString());
        }

        List<Double> marks = new ArrayList<>();
        for (EditText et : dynamicMarksFields) {
            String val = et.getText().toString();
            marks.add(val.isEmpty() ? 0.0 : Double.parseDouble(val));
        }

        Student student = new Student(f1, f2, etField3.getText().toString(), etField4.getText().toString(), 
            radioVal, selectedOptions, swHostel.isChecked(), tbScholarship.isChecked(), 
            spDepartment.getSelectedItem().toString(), selectedDOB, selectedTime, marks);
        
        if (editIndex != -1) {
            DataManager.studentList.set(editIndex, student);
            Toast.makeText(this, R.string.toast_updated, Toast.LENGTH_SHORT).show();
        } else {
            DataManager.studentList.add(student);
            Toast.makeText(this, R.string.toast_registered, Toast.LENGTH_SHORT).show();
        }
        navigateToView();
    }

    private void prefillForm(int index) {
        Student s = DataManager.studentList.get(index);
        etField1.setText(s.getName());
        etField2.setText(s.getRegNo());
        etField3.setText(s.getPhone());
        etField4.setText(s.getEmail());
        
        for (int i = 0; i < rgGender.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rgGender.getChildAt(i);
            if (rb.getText().toString().equals(s.getGender())) rb.setChecked(true);
        }

        for (CheckBox cb : dynamicCheckBoxes) {
            cb.setChecked(s.getSkills().contains(cb.getText().toString()));
        }

        for (int i = 0; i < dynamicMarksFields.size(); i++) {
            if (i < s.getMarks().size()) {
                dynamicMarksFields.get(i).setText(String.valueOf(s.getMarks().get(i)));
            }
        }

        swHostel.setChecked(s.isHostelResident());
        tbScholarship.setChecked(s.isScholarshipEligible());

        ArrayAdapter adapter = (ArrayAdapter) spDepartment.getAdapter();
        spDepartment.setSelection(adapter.getPosition(s.getDepartment()));

        selectedDOB = s.getDob();
        tvSelectedDOB.setText("Date: " + selectedDOB);
        selectedTime = s.getPreferredLabTime();
        tvSelectedTime.setText("Time: " + selectedTime);
    }

    private void resetForm() {
        editIndex = -1;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.form_title_add);
        btnSubmitForm.setText(R.string.submit_details_btn);
        etField1.setText(""); etField2.setText(""); etField3.setText(""); etField4.setText("");
        rgGender.clearCheck();
        for (CheckBox cb : dynamicCheckBoxes) cb.setChecked(false);
        for (EditText et : dynamicMarksFields) et.setText("");
        swHostel.setChecked(false); tbScholarship.setChecked(false);
        selectedDOB = ""; selectedTime = "";
        tvSelectedDOB.setText(R.string.dob_default); tvSelectedTime.setText(R.string.lab_time_default);
    }

    private void navigateToView() {
        startActivity(new Intent(this, StudentListActivity.class));
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
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
