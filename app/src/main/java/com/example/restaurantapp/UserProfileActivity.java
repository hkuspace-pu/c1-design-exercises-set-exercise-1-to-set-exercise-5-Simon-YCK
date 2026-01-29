package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;

public class UserProfileActivity extends AppCompatActivity {

    // Updated to match 3-category system
    private Switch switchBookingNew, switchBookingUpdate, switchBookingCancel, switchGeneralUpdates, switchPromotions;
    private TextView tvName, tvEmail;
    private Button btnLogout;
    private View btnBack;

    private DatabaseHelper dbHelper;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        dbHelper = new DatabaseHelper(this);

        // Get username from Intent (works for both guest and staff)
        currentUsername = getIntent().getStringExtra("guestName");
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = getIntent().getStringExtra("staffName");
        }

        // Initialize Views
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);

        // Map existing switches to the 5 categories
        switchBookingNew = findViewById(R.id.switchBookingCreated);
        switchBookingUpdate = findViewById(R.id.switchBookingUpdated);
        switchBookingCancel = findViewById(R.id.switchBookingCancelled);
        switchGeneralUpdates = findViewById(R.id.switchGeneralUpdates);
        switchPromotions = findViewById(R.id.switchPromoNotif);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        // Set Profile Info
        if (tvName != null) {
            tvName.setText(currentUsername != null ? currentUsername : "User");
        }
        if (tvEmail != null) {
            String role = currentUsername != null && currentUsername.toLowerCase().contains("staff") ? "staff" : "guest";
            tvEmail.setText(currentUsername != null ? currentUsername + "@" + role + ".com" : "user@restaurant.com");
        }

        // Load user-specific preferences from database
        loadNotificationPreferences();

        // Save when switches change
        if (switchBookingNew != null) {
            switchBookingNew.setOnCheckedChangeListener((v, isChecked) -> saveNotificationPreferences());
        }
        if (switchBookingUpdate != null) {
            switchBookingUpdate.setOnCheckedChangeListener((v, isChecked) -> saveNotificationPreferences());
        }
        if (switchBookingCancel != null) {
            switchBookingCancel.setOnCheckedChangeListener((v, isChecked) -> saveNotificationPreferences());
        }
        if (switchGeneralUpdates != null) {
            switchGeneralUpdates.setOnCheckedChangeListener((v, isChecked) -> saveNotificationPreferences());
        }
        if (switchPromotions != null) {
            switchPromotions.setOnCheckedChangeListener((v, isChecked) -> saveNotificationPreferences());
        }

        // Logout Button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    /**
     * Load preferences from database for current user
     */
    private void loadNotificationPreferences() {
        if (currentUsername == null) return;

        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (switchBookingNew != null) switchBookingNew.setChecked(prefs[0]);
        if (switchBookingUpdate != null) switchBookingUpdate.setChecked(prefs[1]);
        if (switchBookingCancel != null) switchBookingCancel.setChecked(prefs[2]);
        if (switchGeneralUpdates != null) switchGeneralUpdates.setChecked(prefs[3]);
        if (switchPromotions != null) switchPromotions.setChecked(prefs[4]);
    }

    /**
     * Save preferences to database for current user
     */
    private void saveNotificationPreferences() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "Unable to save preferences", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean bookingNew = switchBookingNew != null && switchBookingNew.isChecked();
        boolean bookingUpdate = switchBookingUpdate != null && switchBookingUpdate.isChecked();
        boolean bookingCancel = switchBookingCancel != null && switchBookingCancel.isChecked();
        boolean updates = switchGeneralUpdates != null && switchGeneralUpdates.isChecked();
        boolean promotions = switchPromotions != null && switchPromotions.isChecked();

        boolean success = dbHelper.updateNotificationPreferences(currentUsername, bookingNew, bookingUpdate, bookingCancel, updates, promotions);

        if (success) {
            Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save preferences", Toast.LENGTH_SHORT).show();
        }
    }
}
