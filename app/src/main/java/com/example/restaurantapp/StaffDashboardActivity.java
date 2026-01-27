package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.utils.NotificationHelper; // ✅ ADD THIS IMPORT

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
            btnNotifications.setOnClickListener(v -> {
                Intent intent = new Intent(this, NotificationListActivity.class);
                startActivityForResult(intent, 100); // ✅ Use startActivityForResult to refresh badge
            });
        }

        // Initial badge update
        updateBadgeCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadgeCount();
    }

    // ✅ Handle return from NotificationListActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // Refresh badge after returning from notifications
            updateBadgeCount();
        }
    }

    private void updateBadgeCount() {
        // ✅ FIXED: Use NotificationHelper instead of DatabaseHelper
        NotificationHelper notificationHelper = new NotificationHelper(this);
        int count = notificationHelper.getUnreadCount();

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
