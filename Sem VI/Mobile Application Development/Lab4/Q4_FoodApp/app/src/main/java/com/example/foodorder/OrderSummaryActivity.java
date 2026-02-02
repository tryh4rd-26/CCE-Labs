package com.example.foodorder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView tvSummaryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        tvSummaryDetails = findViewById(R.id.tvSummaryDetails);

        ArrayList<String> items = getIntent().getStringArrayListExtra("ITEMS");
        ArrayList<Integer> costs = getIntent().getIntegerArrayListExtra("COSTS");
        int total = getIntent().getIntExtra("TOTAL", 0);

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            summary.append("• ").append(items.get(i))
                    .append(" — ₹").append(costs.get(i)).append("\n");
        }
        summary.append("\nTotal Cost: ₹").append(total);

        tvSummaryDetails.setText(summary.toString());
    }
}
