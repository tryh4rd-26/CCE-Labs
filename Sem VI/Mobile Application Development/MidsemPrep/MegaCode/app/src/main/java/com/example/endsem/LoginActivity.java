package com.example.endsem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MAD_APP_LOGIN";
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvGoToReg;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginSubmit);
        tvGoToReg = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            if (db.checkUser(user, pass)) {
                Log.d(TAG, "Login Successful: " + user);
                Toast.makeText(this, "Welcome " + user, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } else {
                Log.e(TAG, "Login Failed");
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoToReg.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });
    }
}
