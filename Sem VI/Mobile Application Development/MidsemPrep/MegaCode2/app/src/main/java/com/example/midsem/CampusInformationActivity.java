package com.example.midsem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CampusInformationActivity extends AppCompatActivity {
    private static final String TAG = "MAD_APP";

    ListView lvClubs;
    GridView gvFacilities;
    Button btnToBooking;

    String[] clubs = {"Coding Club", "Robotics Club", "Music Club", "Drama Club", "Photography Club"};
    String[] facilities = {"Library", "Gym", "Auditorium", "Lab", "Hostel", "Cafeteria"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_information);

        lvClubs = findViewById(R.id.lvClubs);
        gvFacilities = findViewById(R.id.gvFacilities);
        btnToBooking = findViewById(R.id.btnToBooking);

        // ListView Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubs);
        lvClubs.setAdapter(adapter);

        lvClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Club clicked: " + clubs[position]);
                Toast.makeText(CampusInformationActivity.this, clubs[position], Toast.LENGTH_SHORT).show();
            }
        });

        // Register ListView for Context Menu
        registerForContextMenu(lvClubs);

        // GridView Custom Adapter
        FacilityAdapter facilityAdapter = new FacilityAdapter();
        gvFacilities.setAdapter(facilityAdapter);

        gvFacilities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Facility clicked: " + facilities[position]);
                Toast.makeText(CampusInformationActivity.this, facilities[position], Toast.LENGTH_SHORT).show();
            }
        });

        btnToBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CampusInformationActivity.this, BookingSettingsActivity.class));
            }
        });
    }

    // Context Menu for ListView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Select Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedClub = clubs[info.position];
        
        int id = item.getItemId();
        if (id == R.id.edit) {
            Toast.makeText(this, "Edit: " + selectedClub, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.delete) {
            Toast.makeText(this, "Delete: " + selectedClub, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.share) {
            Toast.makeText(this, "Share: " + selectedClub, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // Custom Adapter for GridView
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
                convertView = getLayoutInflater().inflate(R.layout.grid_item, parent, false);
            }
            TextView tv = convertView.findViewById(R.id.gridText);
            ImageView iv = convertView.findViewById(R.id.gridImage);
            tv.setText(facilities[position]);
            // Use default launcher icon as placeholder
            iv.setImageResource(R.mipmap.ic_launcher);
            return convertView;
        }
    }
}