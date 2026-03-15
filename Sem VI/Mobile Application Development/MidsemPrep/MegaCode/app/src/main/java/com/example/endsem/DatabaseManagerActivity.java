package com.example.endsem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class DatabaseManagerActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_DB";
    ListView lvStudents;
    EditText etUpdateName;
    Button btnUpdate, btnDelete, btnBackToDash;
    DatabaseHelper db;
    ArrayList<String> studentList;
    ArrayList<Integer> studentIds;
    ArrayAdapter<String> adapter;
    int selectedStudentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_manager);

        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Database Manager");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lvStudents = findViewById(R.id.lvStudents);
        etUpdateName = findViewById(R.id.etUpdateName);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBackToDash = findViewById(R.id.btnBackToDash);

        studentList = new ArrayList<>();
        studentIds = new ArrayList<>();
        
        loadData();

        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            selectedStudentId = studentIds.get(position);
            String currentData = studentList.get(position);
            String currentName = currentData.split("\n")[0].replace("Name: ", "");
            etUpdateName.setText(currentName);
            Toast.makeText(this, "Selected ID: " + selectedStudentId, Toast.LENGTH_SHORT).show();
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedStudentId == -1) {
                Toast.makeText(this, "Select a student first", Toast.LENGTH_SHORT).show();
                return;
            }
            String newName = etUpdateName.getText().toString();
            if (db.updateStudent(selectedStudentId, newName, "", "", "", "", "", "")) {
                Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Record Updated: ID " + selectedStudentId);
                loadData();
                etUpdateName.setText("");
                selectedStudentId = -1;
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedStudentId == -1) {
                Toast.makeText(this, "Select a student first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.deleteStudent(selectedStudentId)) {
                Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Record Deleted: ID " + selectedStudentId);
                loadData();
                etUpdateName.setText("");
                selectedStudentId = -1;
            }
        });

        btnBackToDash.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadData() {
        studentList.clear();
        studentIds.clear();
        Cursor cursor = db.getAllStudents();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String regno = cursor.getString(2);
                studentIds.add(id);
                studentList.add("Name: " + name + "\nReg: " + regno);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        lvStudents.setAdapter(adapter);
    }
}
