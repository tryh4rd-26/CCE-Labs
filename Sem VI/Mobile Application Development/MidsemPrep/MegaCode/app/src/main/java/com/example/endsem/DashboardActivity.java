package com.example.endsem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_DASHBOARD";
    Button btnStudentReg, btnCourseExplorer, btnDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Campus Dashboard");
        }

        btnStudentReg = findViewById(R.id.btnStudentReg);
        btnCourseExplorer = findViewById(R.id.btnCourseExplorer);
        btnDatabaseManager = findViewById(R.id.btnDatabaseManager);

        btnStudentReg.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to Student Registration");
            Toast.makeText(DashboardActivity.this, "Student Registration", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, RegistrationActivity.class));
        });

        btnCourseExplorer.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to Course Explorer");
            Toast.makeText(DashboardActivity.this, "Course Explorer", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, CourseExplorerActivity.class));
        });

        btnDatabaseManager.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to Database Manager");
            Toast.makeText(DashboardActivity.this, "Database Manager", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, DatabaseManagerActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_logout) {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
