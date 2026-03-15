package com.example.endsem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_REG_USER";
    EditText etUser, etPass;
    TextView tvStrength, tvBackToLogin;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        db = new DatabaseHelper(this);
        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        tvStrength = findViewById(R.id.tvRegStrength);
        btnRegister = findViewById(R.id.btnRegUserSubmit);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = s.toString();
                if (pass.isEmpty()) {
                    tvStrength.setVisibility(View.GONE);
                    return;
                }
                tvStrength.setVisibility(View.VISIBLE);
                
                boolean hasLength = pass.length() >= 8;
                boolean hasNumber = Pattern.compile("[0-9]").matcher(pass).find();
                boolean hasSymbol = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(pass).find();

                if (hasLength && hasNumber && hasSymbol) {
                    tvStrength.setText("Strength: Strong");
                    tvStrength.setTextColor(Color.GREEN);
                } else if (hasLength && (hasNumber || hasSymbol)) {
                    tvStrength.setText("Strength: Medium");
                    tvStrength.setTextColor(Color.YELLOW);
                } else {
                    tvStrength.setText("Strength: Weak (Needs 8 chars, 0-9, & Symbol)");
                    tvStrength.setTextColor(Color.RED);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnRegister.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();

            if (user.isEmpty() || pass.length() < 8) {
                Toast.makeText(this, "Valid username and 8+ char password required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.registerUser(user, pass)) {
                Log.i(TAG, "Registration Successful: " + user);
                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to login
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }
}
