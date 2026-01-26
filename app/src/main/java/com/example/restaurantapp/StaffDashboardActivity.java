package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;

public class StaffDashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        dbHelper = new DatabaseHelper(this);
        notificationBadge = findViewById(R.id.notificationBadge);

        // 1. MANAGE MENU
        View btnManageMenu = findViewById(R.id.manageMenuButton);
        if (btnManageMenu != null) {
            btnManageMenu.setOnClickListener(v -> startActivity(new Intent(this, StaffMenuEditorActivity.class)));
        }

        // 2. VIEW RESERVATIONS
        View btnViewReservations = findViewById(R.id.viewReservationsButton);
        if (btnViewReservations != null) {
            btnViewReservations.setOnClickListener(v -> startActivity(new Intent(this, StaffViewReservationsActivity.class)));
        }

        // 3. SETTINGS
        View btnSettings = findViewById(R.id.settingsButton);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> startActivity(new Intent(this, StaffProfileActivity.class)));
        }

        // 4. NOTIFICATION BELL - OPEN HISTORY
        View btnNotifications = findViewById(R.id.notificationButton);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    startActivity(new Intent(this, NotificationListActivity.class)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadgeCount();
    }

    private void updateBadgeCount() {
        int count = dbHelper.getUnreadCount();
        if (notificationBadge != null) {
            if (count > 0) {
                notificationBadge.setVisibility(View.VISIBLE);
                notificationBadge.setText(String.valueOf(count));
            } else {
                notificationBadge.setVisibility(View.GONE);
            }
        }
    }
}
