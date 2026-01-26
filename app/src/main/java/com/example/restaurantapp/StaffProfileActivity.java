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

    private Switch switchBooking, switchPromo;
    private TextView tvName, tvEmail;
    private Button btnLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile); // Reuse layout

        prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        switchBooking = findViewById(R.id.switchBookingNotif);
        switchPromo = findViewById(R.id.switchPromoNotif);
        btnLogout = findViewById(R.id.btnLogout);

        // 1. SET DYNAMIC PROFILE INFO (Staff)
        tvName.setText("Staff Administrator");
        tvEmail.setText("admin@restaurant.com");

        // 2. LOAD SAVED PREFERENCES (Share same keys or use different ones if you prefer)
        switchBooking.setChecked(prefs.getBoolean("notif_booking", true));
        switchPromo.setChecked(prefs.getBoolean("notif_promo", false));

        // 3. LISTENERS
        switchBooking.setOnCheckedChangeListener((v, isChecked) ->
                prefs.edit().putBoolean("notif_booking", isChecked).apply());

        switchPromo.setOnCheckedChangeListener((v, isChecked) ->
                prefs.edit().putBoolean("notif_promo", isChecked).apply());

        // 4. LOGOUT
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
