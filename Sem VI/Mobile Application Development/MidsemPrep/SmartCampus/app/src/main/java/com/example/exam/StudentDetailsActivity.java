package com.example.exam;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView tvDetailName, tvDetailRegNo, tvDetailDept, tvDetailFull;
    private ListView lvDetailSkills;
    private GridView gvDetailPrefs;
    private LinearLayout llDetailMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        setupToolbar();
        initViews();

        int index = getIntent().getIntExtra("STUDENT_INDEX", -1);
        if (index != -1 && index < DataManager.studentList.size()) {
            displayDetails(DataManager.studentList.get(index));
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.student_details_title);
        }
    }

    private void initViews() {
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailRegNo = findViewById(R.id.tvDetailRegNo);
        tvDetailDept = findViewById(R.id.tvDetailDept);
        tvDetailFull = findViewById(R.id.tvDetailFull);
        lvDetailSkills = findViewById(R.id.lvDetailSkills);
        gvDetailPrefs = findViewById(R.id.gvDetailPrefs);
        
        // Find or add a marks container in layout
        llDetailMarks = new LinearLayout(this);
        llDetailMarks.setOrientation(LinearLayout.VERTICAL);
        ((LinearLayout)tvDetailFull.getParent()).addView(llDetailMarks, ((LinearLayout)tvDetailFull.getParent()).indexOfChild(tvDetailFull));
    }

    private void displayDetails(Student s) {
        tvDetailName.setText(s.getName());
        tvDetailRegNo.setText(s.getRegNo());
        tvDetailDept.setText(s.getDepartment());

        // Skills List
        lvDetailSkills.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s.getSkills()));

        // Preferences Grid
        List<String> prefs = new ArrayList<>();
        prefs.add("Type: " + s.getGender());
        prefs.add("Resident: " + (s.isHostelResident() ? "Yes" : "No"));
        prefs.add("Status: " + (s.isScholarshipEligible() ? "Yes" : "No"));
        prefs.add("Date: " + s.getDob());
        prefs.add("Total: " + s.getTotalMarks());
        gvDetailPrefs.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prefs));

        // Dynamic Marks Display
        String[] markLabels = getResources().getStringArray(R.array.assessments_array);
        llDetailMarks.removeAllViews();
        TextView head = new TextView(this);
        head.setText("Values / Marks Breakdown:");
        head.setTypeface(null, Typeface.BOLD);
        llDetailMarks.addView(head);
        
        for (int i = 0; i < markLabels.length; i++) {
            if (i < s.getMarks().size()) {
                TextView tv = new TextView(this);
                tv.setText(markLabels[i] + ": " + s.getMarks().get(i));
                tv.setPadding(10, 5, 10, 5);
                llDetailMarks.addView(tv);
            }
        }

        String summary = "Email: " + s.getEmail() + "\nPhone: " + s.getPhone() + "\nTotal Calculated: " + s.getTotalMarks();
        tvDetailFull.setText(summary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.menu_register || id == R.id.menu_add_student) {
            startActivity(new Intent(this, StudentFormActivity.class));
            return true;
        } else if (id == R.id.menu_view_students) {
            startActivity(new Intent(this, StudentListActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
