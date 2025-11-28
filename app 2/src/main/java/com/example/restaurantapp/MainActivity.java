package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Login button click
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Staff login
        if (username.equalsIgnoreCase("staff") || username.equalsIgnoreCase("admin")) {
            if (password.equals("staff123") || password.equals("admin")) {
                Intent intent = new Intent(MainActivity.this, StaffDashboardActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Welcome, Staff!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid staff password", Toast.LENGTH_SHORT).show();
            }
        }
        // Guest login
        else if (username.equalsIgnoreCase("guest")) {
            if (password.equals("guest123")) {
                Intent intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
                intent.putExtra("guestName", "Guest User");
                startActivity(intent);
                Toast.makeText(this, "Welcome, Guest!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid guest password", Toast.LENGTH_SHORT).show();
            }
        }
        // Any other username = guest (for flexibility)
        else {
            Intent intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
            intent.putExtra("guestName", username);
            startActivity(intent);
            Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
        }
    }
}
