package com.example.foodorder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CheckBox cbPizza, cbBurger, cbPasta;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbPizza = findViewById(R.id.cbPizza);
        cbBurger = findViewById(R.id.cbBurger);
        cbPasta = findViewById(R.id.cbPasta);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            ArrayList<String> items = new ArrayList<>();
            ArrayList<Integer> costs = new ArrayList<>();
            int total = 0;

            if (cbPizza.isChecked()) {
                items.add("Pizza");
                costs.add(200);
                total += 200;
            }
            if (cbBurger.isChecked()) {
                items.add("Burger");
                costs.add(100);
                total += 100;
            }
            if (cbPasta.isChecked()) {
                items.add("Pasta");
                costs.add(150);
                total += 150;
            }

            Intent intent = new Intent(MainActivity.this, OrderSummaryActivity.class);
            intent.putStringArrayListExtra("ITEMS", items);
            intent.putIntegerArrayListExtra("COSTS", costs);
            intent.putExtra("TOTAL", total);
            startActivity(intent);

            // Disable further changes
            cbPizza.setEnabled(false);
            cbBurger.setEnabled(false);
            cbPasta.setEnabled(false);
            btnSubmit.setEnabled(false);
        });
    }
}
