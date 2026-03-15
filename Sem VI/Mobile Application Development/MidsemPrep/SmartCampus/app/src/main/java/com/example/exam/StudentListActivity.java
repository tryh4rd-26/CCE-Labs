package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StudentListActivity extends AppCompatActivity {

    private ListView lvStudents;
    private GridView gvStudents;
    private TableLayout tlStudents;
    private Button btnShowPopup;
    private TextView tvEmptyState;
    private View svDataContainer;
    private static final String TAG = "MAD_APP_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Student List");
        }

        lvStudents = findViewById(R.id.lvStudents);
        gvStudents = findViewById(R.id.gvStudents);
        tlStudents = findViewById(R.id.tlStudents);
        btnShowPopup = findViewById(R.id.btnShowPopup);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        svDataContainer = findViewById(R.id.svDataContainer);

        updateUIState();

        btnShowPopup.setOnClickListener(v -> showPopupMenu(v));
    }

    private void updateUIState() {
        if (DataManager.studentList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            svDataContainer.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            svDataContainer.setVisibility(View.VISIBLE);
            setupListView();
            setupGridView();
            setupTableLayout();
        }
    }

    private void setupListView() {
        BaseAdapter listAdapter = new BaseAdapter() {
            @Override public int getCount() { return DataManager.studentList.size(); }
            @Override public Object getItem(int position) { return DataManager.studentList.get(position); }
            @Override public long getItemId(int position) { return position; }
            @Override public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(StudentListActivity.this).inflate(R.layout.list_item_student, parent, false);
                }
                Student s = DataManager.studentList.get(position);
                TextView tvName = convertView.findViewById(R.id.tvStudentName);
                TextView tvSub = convertView.findViewById(R.id.tvStudentSub);
                ImageButton btnMenu = convertView.findViewById(R.id.btnItemMenu);

                tvName.setText(s.getName());
                tvSub.setText(s.getRegNo() + " | " + s.getDepartment());

                btnMenu.setOnClickListener(v -> showItemMenu(v, position));
                convertView.setOnClickListener(v -> openDetails(position));

                return convertView;
            }
        };
        lvStudents.setAdapter(listAdapter);
    }

    private void showItemMenu(View v, int position) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("View Details");
        popup.getMenu().add("Edit");
        popup.getMenu().add("Delete");
        popup.setOnMenuItemClickListener(item -> {
            String action = item.getTitle().toString();
            if (action.equals("View Details")) {
                openDetails(position);
            } else if (action.equals("Edit")) {
                // Requirement 1: Open actual edit option
                Intent intent = new Intent(this, StudentFormActivity.class);
                intent.putExtra("EDIT_INDEX", position);
                startActivity(intent);
                Toast.makeText(this, "Editing " + DataManager.studentList.get(position).getName(), Toast.LENGTH_SHORT).show();
            } else if (action.equals("Delete")) {
                DataManager.studentList.remove(position);
                updateUIState();
                Toast.makeText(this, "Deleted Student", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        popup.show();
    }

    private void openDetails(int position) {
        Intent intent = new Intent(this, StudentDetailsActivity.class);
        intent.putExtra("STUDENT_INDEX", position);
        startActivity(intent);
    }

    private void setupGridView() {
        BaseAdapter gridAdapter = new BaseAdapter() {
            @Override public int getCount() { return DataManager.studentList.size(); }
            @Override public Object getItem(int position) { return DataManager.studentList.get(position); }
            @Override public long getItemId(int position) { return position; }
            @Override public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(StudentListActivity.this).inflate(R.layout.grid_item, parent, false);
                }
                Student s = DataManager.studentList.get(position);
                ((TextView) convertView.findViewById(R.id.tvGridName)).setText(s.getName());
                ((TextView) convertView.findViewById(R.id.tvGridDept)).setText(s.getDepartment());
                
                convertView.setOnClickListener(v -> openDetails(position));
                return convertView;
            }
        };
        gvStudents.setAdapter(gridAdapter);
    }

    private void setupTableLayout() {
        int childCount = tlStudents.getChildCount();
        if (childCount > 1) tlStudents.removeViews(1, childCount - 1);
        for (int i = 0; i < DataManager.studentList.size(); i++) {
            Student s = DataManager.studentList.get(i);
            TableRow row = new TableRow(this);
            row.setPadding(0, 4, 0, 4);
            
            TextView tvName = new TextView(this); tvName.setText(s.getName()); tvName.setPadding(8, 8, 8, 8);
            TextView tvDept = new TextView(this); tvDept.setText(s.getDepartment()); tvDept.setPadding(8, 8, 8, 8);
            TextView tvGender = new TextView(this); tvGender.setText(s.getGender()); tvGender.setPadding(8, 8, 8, 8);
            
            row.addView(tvName); row.addView(tvDept); row.addView(tvGender);
            
            final int pos = i;
            row.setOnClickListener(v -> openDetails(pos));
            tlStudents.addView(row);
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("Change Theme");
        popup.getMenu().add("Sort Students");
        popup.getMenu().add("Refresh");
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Refresh")) updateUIState();
            return true;
        });
        popup.show();
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
            // Requirement 2: Fix Register option
            Intent intent = new Intent(this, StudentFormActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_clear_data) {
            DataManager.studentList.clear();
            updateUIState();
            Toast.makeText(this, "Data Cleared", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
