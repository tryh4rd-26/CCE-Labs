package com.example.q2_androidversion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCupcake, btnDonut, btnEclair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCupcake = findViewById(R.id.btnCupcake);
        btnDonut = findViewById(R.id.btnDonut);
        btnEclair = findViewById(R.id.btnEclair);

        btnCupcake.setOnClickListener(v -> showCustomToast("Cupcake", R.drawable.ic_cupcake));
        btnDonut.setOnClickListener(v -> showCustomToast("Donut", R.drawable.ic_donut));
        btnEclair.setOnClickListener(v -> showCustomToast("Eclair", R.drawable.ic_eclair));
    }

    private void showCustomToast(String versionName, int iconResId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, findViewById(android.R.id.content), false);

        ImageView img = layout.findViewById(R.id.imgToast);
        TextView tv = layout.findViewById(R.id.tvToastText);

        img.setImageResource(iconResId);
        tv.setText("Android " + versionName);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
