package com.example.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StaffProfileActivity extends AppCompatActivity {

    private Switch switchCreated, switchUpdated, switchCancelled, switchPromo;
    private TextView tvName, tvEmail;
    private Button btnLogout, btnBack;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        // Initialize Views
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        switchCreated = findViewById(R.id.switchBookingCreated);
        switchUpdated = findViewById(R.id.switchBookingUpdated);
        switchCancelled = findViewById(R.id.switchBookingCancelled);
        switchPromo = findViewById(R.id.switchPromoNotif);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        // Set Profile Info (Staff)
        if (tvName != null) tvName.setText("Staff Administrator");
        if (tvEmail != null) tvEmail.setText("admin@restaurant.com");

        // Load Saved Preferences
        if (switchCreated != null) switchCreated.setChecked(prefs.getBoolean("notif_booking_created", true));
        if (switchUpdated != null) switchUpdated.setChecked(prefs.getBoolean("notif_booking_updated", true));
        if (switchCancelled != null) switchCancelled.setChecked(prefs.getBoolean("notif_booking_cancelled", true));
        if (switchPromo != null) switchPromo.setChecked(prefs.getBoolean("notif_promo", false));

        // Save Listeners
        if (switchCreated != null) {
            switchCreated.setOnCheckedChangeListener((v, isChecked) ->
                    prefs.edit().putBoolean("notif_booking_created", isChecked).apply());
        }
        if (switchUpdated != null) {
            switchUpdated.setOnCheckedChangeListener((v, isChecked) ->
                    prefs.edit().putBoolean("notif_booking_updated", isChecked).apply());
        }
        if (switchCancelled != null) {
            switchCancelled.setOnCheckedChangeListener((v, isChecked) ->
                    prefs.edit().putBoolean("notif_booking_cancelled", isChecked).apply());
        }
        if (switchPromo != null) {
            switchPromo.setOnCheckedChangeListener((v, isChecked) ->
                    prefs.edit().putBoolean("notif_promo", isChecked).apply());
        }

        // Logout Button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}
