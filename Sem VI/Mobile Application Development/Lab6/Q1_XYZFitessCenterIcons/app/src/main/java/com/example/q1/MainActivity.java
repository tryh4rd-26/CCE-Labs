package com.example.q1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView contentDisplay;
    private LinearLayout trainerPhotosLayout;
    private ImageView trainer1, trainer2, trainer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        contentDisplay = findViewById(R.id.content_display);
        trainerPhotosLayout = findViewById(R.id.trainer_photos_layout);
        trainer1 = findViewById(R.id.trainer_1);
        trainer2 = findViewById(R.id.trainer_2);
        trainer3 = findViewById(R.id.trainer_3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Hide trainer photos by default
        trainerPhotosLayout.setVisibility(View.GONE);

        if (id == R.id.workout_plans) {
            contentDisplay.setText("Workout Plans:\n\n1. Weight Loss - High Intensity Interval Training\n2. Cardio - Endurance and Heart Health\n3. Strength Training - Muscle Building");
            return true;
        } else if (id == R.id.trainers) {
            contentDisplay.setText("Our Trainers:\n\n1. Mike Tyson - Boxing Specialist\n2. Arnold S. - Bodybuilding Expert\n3. Serena Williams - Fitness & Agility");
            
            // Set images from drawable
            trainer1.setImageResource(R.drawable.images); // Mike Tyson
            trainer2.setImageResource(R.drawable.arnold); // Arnold
            trainer3.setImageResource(R.drawable.serena); // Serena (Assuming file is renamed)
            
            trainerPhotosLayout.setVisibility(View.VISIBLE);
            return true;
        } else if (id == R.id.membership) {
            contentDisplay.setText("Membership Packages:\n\n- Basic: $29/month\n- Pro: $59/month\n- Elite: $99/month");
            return true;
        } else if (id == R.id.homepage) {
            contentDisplay.setText("Welcome to XYZ Fitness Center\n\nYour journey to a healthier lifestyle starts here!");
            return true;
        } else if (id == R.id.about_us) {
            contentDisplay.setText("About Us:\n\nXYZ Fitness Center has been leading the fitness industry for 10 years.");
            return true;
        } else if (id == R.id.contact_us) {
            contentDisplay.setText("Contact Us:\n\nEmail: info@xyzfitness.com\nPhone: +1 234 567 890");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}