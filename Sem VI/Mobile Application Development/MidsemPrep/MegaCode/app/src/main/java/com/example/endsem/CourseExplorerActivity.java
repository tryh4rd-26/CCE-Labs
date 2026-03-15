package com.example.endsem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

public class CourseExplorerActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_EXPLORER";
    ListView lvCourses;
    GridView gvFacilities;
    Button btnMoreActions, btnToReg, btnToDB;

    ArrayList<String> coursesList;
    ArrayAdapter<String> courseAdapter;

    String[] facilities = {"Library", "Hostel", "Cafeteria", "Gym", "Auditorium", "Sports Complex"};
    int[] facilityIcons = {
            android.R.drawable.ic_menu_agenda,
            android.R.drawable.ic_menu_myplaces,
            android.R.drawable.ic_menu_view,
            android.R.drawable.ic_menu_directions,
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_gallery
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_explorer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Course Explorer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lvCourses = findViewById(R.id.lvCourses);
        gvFacilities = findViewById(R.id.gvFacilities);
        btnMoreActions = findViewById(R.id.btnMoreActions);
        btnToReg = findViewById(R.id.btnRegFromExplorer);
        btnToDB = findViewById(R.id.btnGoToDB);

        // Course List with dynamic capability
        coursesList = new ArrayList<>(Arrays.asList("Android Development", "Machine Learning", "Computer Networks", "Operating Systems", "Cyber Security"));
        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coursesList);
        lvCourses.setAdapter(courseAdapter);

        // Navigation
        btnToReg.setOnClickListener(v -> startActivity(new Intent(this, RegistrationActivity.class)));
        btnToDB.setOnClickListener(v -> startActivity(new Intent(this, DatabaseManagerActivity.class)));

        lvCourses.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "Course: " + coursesList.get(position), Toast.LENGTH_SHORT).show();
        });

        registerForContextMenu(lvCourses);

        // GridView
        gvFacilities.setAdapter(new FacilityAdapter());
        gvFacilities.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "Facility: " + facilities[position], Toast.LENGTH_SHORT).show();
        });

        // Popup Menu
        btnMoreActions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, btnMoreActions);
            popup.getMenu().add("Share Explorer");
            popup.getMenu().add("Bookmark Page");
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popup.show();
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvCourses) {
            menu.setHeaderTitle("Manage Course");
            menu.add(0, 1, 0, "View Details");
            menu.add(0, 2, 0, "Edit Course Name");
            menu.add(0, 3, 0, "Delete Course");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        String courseName = coursesList.get(position);

        switch (item.getItemId()) {
            case 1: // View
                new AlertDialog.Builder(this)
                        .setTitle("Course Details")
                        .setMessage("Course: " + courseName + "\nStatus: Available\nCredits: 4")
                        .setPositiveButton("OK", null)
                        .show();
                return true;
            case 2: // Edit
                showEditDialog(position);
                return true;
            case 3: // Delete
                coursesList.remove(position);
                courseAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Deleted course at pos: " + position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showEditDialog(int position) {
        EditText et = new EditText(this);
        et.setText(coursesList.get(position));
        new AlertDialog.Builder(this)
                .setTitle("Edit Course")
                .setView(et)
                .setPositiveButton("Update", (dialog, which) -> {
                    String updated = et.getText().toString();
                    coursesList.set(position, updated);
                    courseAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Updated to " + updated, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class FacilityAdapter extends BaseAdapter {
        @Override
        public int getCount() { return facilities.length; }
        @Override
        public Object getItem(int position) { return facilities[position]; }
        @Override
        public long getItemId(int position) { return position; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.grid_item, null);
            }
            ImageView iv = convertView.findViewById(R.id.ivGridIcon);
            TextView tv = convertView.findViewById(R.id.tvGridLabel);
            iv.setImageResource(facilityIcons[position]);
            tv.setText(facilities[position]);
            return convertView;
        }
    }
}
