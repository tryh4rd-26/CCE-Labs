package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etRegUsername, etRegEmail, etRegPassword, etRegConfirmPassword;
    private TextView tvPasswordStrength;
    private Button btnCheckStrength, btnSubmitRegistration, btnBack;
    private static final String TAG = "MAD_APP_REG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setupToolbar();
        initViews();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.registration_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        tvPasswordStrength = findViewById(R.id.tvPasswordStrength);
        btnCheckStrength = findViewById(R.id.btnCheckStrength);
        btnSubmitRegistration = findViewById(R.id.btnSubmitRegistration);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnCheckStrength.setOnClickListener(v -> checkStrength());
        btnSubmitRegistration.setOnClickListener(v -> handleRegistration());
        btnBack.setOnClickListener(v -> finish());
    }

    private void handleRegistration() {
        String username = etRegUsername.getText().toString().trim();
        String email = etRegEmail.getText().toString().trim();
        String password = etRegPassword.getText().toString();
        String confirmPassword = etRegConfirmPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, R.string.error_missing_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Registration Successful: " + username);
        startActivity(new Intent(this, StudentFormActivity.class));
        finish();
    }

    private void checkStrength() {
        String password = etRegPassword.getText().toString();
        String strength = calculateStrength(password);

        tvPasswordStrength.setText(getString(R.string.password_strength_prefix) + strength);
        Toast.makeText(this, "Strength: " + strength, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Password strength: " + strength);
    }

    private String calculateStrength(String password) {
        if (password.length() < 6) return "Weak";
        if (password.length() < 8) {
            return password.matches(".*\\d.*") ? "Medium" : "Weak";
        }
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (hasUpper && hasLower && hasDigit && hasSpecial) return "Strong";
        if (hasDigit) return "Medium";
        return "Weak";
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
