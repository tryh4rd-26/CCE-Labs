package com.example.endsem;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen or Redirector to Login
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Redirect to LoginActivity immediately
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
