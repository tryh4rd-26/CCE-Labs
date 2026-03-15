package com.example.exam;

import android.content.Intent;
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
    private int studentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Student Details");
        }

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailRegNo = findViewById(R.id.tvDetailRegNo);
        tvDetailDept = findViewById(R.id.tvDetailDept);
        tvDetailFull = findViewById(R.id.tvDetailFull);
        lvDetailSkills = findViewById(R.id.lvDetailSkills);
        gvDetailPrefs = findViewById(R.id.gvDetailPrefs);

        studentIndex = getIntent().getIntExtra("STUDENT_INDEX", -1);
        if (studentIndex != -1 && studentIndex < DataManager.studentList.size()) {
            Student s = DataManager.studentList.get(studentIndex);
            displayDetails(s);
        }
    }

    private void displayDetails(Student s) {
        tvDetailName.setText(s.getName());
        tvDetailRegNo.setText(s.getRegNo());
        tvDetailDept.setText(s.getDepartment());

        // ListView for Skills
        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s.getSkills());
        lvDetailSkills.setAdapter(skillsAdapter);

        // GridView for Preferences
        List<String> prefs = new ArrayList<>();
        prefs.add("Gender: " + s.getGender());
        prefs.add("Hostel: " + (s.isHostelResident() ? "Yes" : "No"));
        prefs.add("Scholarship: " + (s.isScholarshipEligible() ? "Yes" : "No"));
        prefs.add("DOB: " + s.getDob());
        prefs.add("Lab: " + s.getPreferredLabTime());

        ArrayAdapter<String> prefsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prefs);
        gvDetailPrefs.setAdapter(prefsAdapter);

        // Normal Text Details
        String fullInfo = "Email: " + s.getEmail() + "\n" +
                          "Phone: " + s.getPhone() + "\n" +
                          "Registration: " + s.getRegNo() + "\n" +
                          "Department: " + s.getDepartment() + "\n" +
                          "Preferred Lab Time: " + s.getPreferredLabTime();
        tvDetailFull.setText(fullInfo);
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
            Intent intent = new Intent(this, StudentFormActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_view_students) {
            Intent intent = new Intent(this, StudentListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
