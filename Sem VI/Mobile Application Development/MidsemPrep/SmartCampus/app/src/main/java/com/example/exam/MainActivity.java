package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegisterUser;
    private static final String TAG = "MAD_APP_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        initViews();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.login_title);
        }
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegisterUser.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to RegistrationActivity");
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String[] creds = getResources().getStringArray(R.array.login_credentials);
        String validUser = creds[0];
        String validPass = creds[1];

        if (username.equals(validUser) && password.equals(validPass)) {
            Toast.makeText(this, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Login Successful: " + username);
            startActivity(new Intent(this, StudentFormActivity.class));
        } else {
            Toast.makeText(this, R.string.toast_login_failed, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Login Failed: " + username);
        }
    }
}
