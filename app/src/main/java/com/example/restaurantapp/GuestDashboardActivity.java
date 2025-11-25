package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GuestDashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button browseMenuButton;
    private Button makeReservationButton;
    private Button viewReservationsButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guest Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("USERNAME");

        welcomeText = findViewById(R.id.welcomeText);
        browseMenuButton = findViewById(R.id.browseMenuButton);
        makeReservationButton = findViewById(R.id.makeReservationButton);
        viewReservationsButton = findViewById(R.id.viewReservationsButton);

        // HIGH VISIBILITY - Figma fix for Ex4
        welcomeText.setText("Welcome, " + username + "!");
        welcomeText.setTextSize(28); // Larger text
        welcomeText.setTextColor(getResources().getColor(android.R.color.black)); // High contrast

        browseMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestMenuBrowseActivity.class);
                startActivity(intent);
            }
        });

        makeReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestReservationActivity.class);
                intent.putExtra("MODE", "CREATE");
                startActivity(intent);
            }
        });

        viewReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestViewReservationsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(this, "Notifications: You have 2 new updates", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            finish();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
