package com.example.q3;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView contentTextView;
    private final String originalContent = "Digital transformation is the integration of digital technology into all areas of a business. " +
            "It fundamentally changes how you operate and deliver value to customers. " +
            "It's also a cultural change that requires organizations to continually challenge the status quo. " +
            "This often means walking away from long-standing business processes. " +
            "Instead, companies experiment and get comfortable with failure. " +
            "Digital transformation is essential for all businesses, from the small to the enterprise. " +
            "That message comes through loud and clear from every keynote, panel discussion, article, or study related to how businesses can remain competitive and relevant as the world becomes increasingly digital.";

    private List<String> sentences;
    private List<String> displayedSentences;
    private String currentSearchKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply padding to the root view to move the toolbar and content below the status bar/camera
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contentTextView = findViewById(R.id.contentTextView);
        
        sentences = new ArrayList<>(Arrays.asList(originalContent.split("(?<=\\.) ")));
        displayedSentences = new ArrayList<>(sentences);
        
        updateTextView();
    }

    private void updateTextView() {
        StringBuilder sb = new StringBuilder();
        for (String sentence : displayedSentences) {
            sb.append(sentence).append(" ");
        }
        contentTextView.setText(sb.toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_search) {
            showInputDialog("Search Keywords", (input) -> {
                currentSearchKeyword = input;
                filterContent(input);
            });
            return true;
        } else if (id == R.id.submenu_highlight) {
            showInputDialog("Highlight Keywords", this::highlightContent);
            return true;
        } else if (id == R.id.sort_alphabetical) {
            sortAlphabetically();
            return true;
        } else if (id == R.id.sort_relevance) {
            sortByRelevance();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInputDialog(String title, InputCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> callback.onInput(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void filterContent(String keyword) {
        displayedSentences.clear();
        if (keyword.isEmpty()) {
            displayedSentences.addAll(sentences);
        } else {
            for (String s : sentences) {
                if (s.toLowerCase().contains(keyword.toLowerCase())) {
                    displayedSentences.add(s);
                }
            }
        }
        updateTextView();
    }

    private void highlightContent(String keyword) {
        updateTextView();
        if (keyword.isEmpty()) return;

        String text = contentTextView.getText().toString();
        SpannableString spannable = new SpannableString(text);
        int index = text.toLowerCase().indexOf(keyword.toLowerCase());
        while (index >= 0) {
            spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = text.toLowerCase().indexOf(keyword.toLowerCase(), index + keyword.length());
        }
        contentTextView.setText(spannable);
    }

    private void sortAlphabetically() {
        Collections.sort(displayedSentences);
        updateTextView();
    }

    private void sortByRelevance() {
        if (currentSearchKeyword.isEmpty()) {
            Toast.makeText(this, "Search keyword is empty. Perform a search first or sort alphabetically.", Toast.LENGTH_SHORT).show();
            return;
        }
        Collections.sort(displayedSentences, (s1, s2) -> {
            int count1 = countOccurrences(s1, currentSearchKeyword);
            int count2 = countOccurrences(s2, currentSearchKeyword);
            return Integer.compare(count2, count1);
        });
        updateTextView();
    }

    private int countOccurrences(String text, String keyword) {
        int count = 0;
        int index = 0;
        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        while ((index = lowerText.indexOf(lowerKeyword, index)) != -1) {
            count++;
            index += lowerKeyword.length();
        }
        return count;
    }

    interface InputCallback {
        void onInput(String input);
    }
}
