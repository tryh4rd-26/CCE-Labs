package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etRegUsername = findViewById(R.id.etRegUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        tvPasswordStrength = findViewById(R.id.tvPasswordStrength);
        btnCheckStrength = findViewById(R.id.btnCheckStrength);
        btnSubmitRegistration = findViewById(R.id.btnSubmitRegistration);
        btnBack = findViewById(R.id.btnBack);

        btnCheckStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStrength();
            }
        });

        btnSubmitRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegUsername.getText().toString();
                String email = etRegEmail.getText().toString();
                String password = etRegPassword.getText().toString();
                String confirmPassword = etRegConfirmPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Registration Successful: " + username);
                Intent intent = new Intent(RegistrationActivity.this, StudentFormActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void checkStrength() {
        String password = etRegPassword.getText().toString();
        String strength = "Weak";

        if (password.length() < 6) {
            strength = "Weak";
        } else if (password.length() < 8) {
            if (password.matches(".*\\d.*")) {
                strength = "Medium";
            } else {
                strength = "Weak";
            }
        } else {
            boolean hasUpper = password.matches(".*[A-Z].*");
            boolean hasLower = password.matches(".*[a-z].*");
            boolean hasDigit = password.matches(".*\\d.*");
            boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                strength = "Strong";
            } else if (hasDigit) {
                strength = "Medium";
            } else {
                strength = "Weak";
            }
        }

        tvPasswordStrength.setText("Password Strength: " + strength);
        Toast.makeText(this, "Strength: " + strength, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Password strength checked: " + strength);
    }
}
