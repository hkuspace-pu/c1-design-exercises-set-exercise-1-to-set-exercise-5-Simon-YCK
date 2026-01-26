package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // 1. MANAGE MENU BUTTON
        View btnManageMenu = findViewById(R.id.manageMenuButton);
        if (btnManageMenu != null) {
            btnManageMenu.setOnClickListener(v -> {
                Intent intent = new Intent(StaffDashboardActivity.this, StaffMenuEditorActivity.class);
                startActivity(intent);
            });
        }

        // 2. VIEW RESERVATIONS BUTTON
        View btnViewReservations = findViewById(R.id.viewReservationsButton);
        if (btnViewReservations != null) {
            btnViewReservations.setOnClickListener(v -> {
                Intent intent = new Intent(StaffDashboardActivity.this, StaffViewReservationsActivity.class);
                startActivity(intent);
            });
        }

        // 3. SETTINGS / PROFILE BUTTON (The Gear Icon)
        View btnSettings = findViewById(R.id.settingsButton);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                // Open the new Staff Profile/Settings screen
                Intent intent = new Intent(StaffDashboardActivity.this, StaffProfileActivity.class);
                startActivity(intent);
            });
        }

        // 4. NOTIFICATION BELL (Optional Interaction)
        View btnNotifications = findViewById(R.id.notificationButton);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "You have 3 new updates", Toast.LENGTH_SHORT).show()
            );
        }
    }
}
