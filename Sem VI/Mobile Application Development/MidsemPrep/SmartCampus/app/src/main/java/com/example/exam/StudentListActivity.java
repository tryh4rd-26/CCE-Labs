package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        setupToolbar();
        initViews();
        updateUIState();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.student_list_title);
        }
    }

    private void initViews() {
        lvStudents = findViewById(R.id.lvStudents);
        gvStudents = findViewById(R.id.gvStudents);
        tlStudents = findViewById(R.id.tlStudents);
        btnShowPopup = findViewById(R.id.btnShowPopup);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        svDataContainer = findViewById(R.id.svDataContainer);

        btnShowPopup.setOnClickListener(this::showPopupMenu);
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
                // Modular: showing total marks
                tvSub.setText(s.getRegNo() + " | Total: " + s.getTotalMarks());

                btnMenu.setOnClickListener(v -> showItemMenu(v, position));
                convertView.setOnClickListener(v -> openDetails(position));

                return convertView;
            }
        };
        lvStudents.setAdapter(listAdapter);
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
                ((TextView) convertView.findViewById(R.id.tvGridDept)).setText("Total: " + s.getTotalMarks());
                
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
            TextView tvTotal = new TextView(this); tvTotal.setText(String.valueOf(s.getTotalMarks())); tvTotal.setPadding(8, 8, 8, 8);
            
            row.addView(tvName); row.addView(tvDept); row.addView(tvTotal);
            
            final int pos = i;
            row.setOnClickListener(v -> openDetails(pos));
            tlStudents.addView(row);
        }
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
                startActivity(new Intent(this, StudentFormActivity.class).putExtra("EDIT_INDEX", position));
            } else if (action.equals("Delete")) {
                DataManager.studentList.remove(position);
                updateUIState();
                Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        popup.show();
    }

    private void openDetails(int position) {
        startActivity(new Intent(this, StudentDetailsActivity.class).putExtra("STUDENT_INDEX", position));
    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
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
            startActivity(new Intent(this, StudentFormActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            DataManager.studentList.clear();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
