package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private RadioGroup userTypeRadioGroup;
    private RadioButton guestRadio;
    private RadioButton staffRadio;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        guestRadio = findViewById(R.id.guestRadio);
        staffRadio = findViewById(R.id.staffRadio);
        loginButton = findViewById(R.id.loginButton);

        // Set default selection to Guest
        guestRadio.setChecked(true);

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Basic validation (UI only, no actual authentication)
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check selected user type
        int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.guestRadio) {
            // Navigate to Guest Dashboard
            Intent intent = new Intent(MainActivity.this, GuestDashboardActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            Toast.makeText(this, "Welcome Guest: " + username, Toast.LENGTH_SHORT).show();

        } else if (selectedId == R.id.staffRadio) {
            // Navigate to Staff Menu Editor
            Intent intent = new Intent(MainActivity.this, StaffMenuEditorActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            Toast.makeText(this, "Welcome Staff: " + username, Toast.LENGTH_SHORT).show();
        }
    }
}
