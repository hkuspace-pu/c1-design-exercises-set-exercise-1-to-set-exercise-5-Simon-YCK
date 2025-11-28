package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    private LinearLayout manageMenuButton;
    private LinearLayout viewReservationsButton;
    private View logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // Initialize views
        manageMenuButton = findViewById(R.id.manageMenuButton);
        viewReservationsButton = findViewById(R.id.viewReservationsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Notification button
        findViewById(R.id.notificationButton).setOnClickListener(v -> {
            Toast.makeText(this, "🔔 You have 3 new notifications:\n• New reservation request\n• Menu update needed\n• Low stock alert", Toast.LENGTH_LONG).show();
        });

        // Manage Menu button
        manageMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, StaffMenuEditorActivity.class);
            startActivity(intent);
        });

        // View Reservations button
        viewReservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, StaffViewReservationsActivity.class);
            startActivity(intent);
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            finish();
        });
    }
}
