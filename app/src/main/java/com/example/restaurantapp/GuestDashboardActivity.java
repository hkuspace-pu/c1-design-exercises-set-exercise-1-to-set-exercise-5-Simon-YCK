package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GuestDashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button bookTableButton;
    private Button browseMenuButton;
    private LinearLayout reservationsContainer;
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
        bookTableButton = findViewById(R.id.bookTableButton);
        browseMenuButton = findViewById(R.id.browseMenuButton);
        reservationsContainer = findViewById(R.id.reservationsContainer);

        // HIGH VISIBILITY
        welcomeText.setText("Hello, " + username + "!");
        welcomeText.setTextSize(28);
        welcomeText.setTextColor(getResources().getColor(android.R.color.black));

        // Book Table Button
        bookTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestReservationActivity.class);
                intent.putExtra("MODE", "CREATE");
                startActivity(intent);
            }
        });

        // Browse Menu Button
        browseMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestMenuBrowseActivity.class);
                startActivity(intent);
            }
        });

        // Load fake reservations INLINE
        loadInlineReservations();
    }

    private void loadInlineReservations() {
        // FAKE DATA - Shows directly on dashboard
        addInlineReservation("2025-11-25", "19:00", "2 guests");
        addInlineReservation("2025-11-28", "20:30", "4 guests");
    }

    private void addInlineReservation(String date, String time, String guests) {
        LinearLayout reservationRow = new LinearLayout(this);
        reservationRow.setOrientation(LinearLayout.HORIZONTAL);
        reservationRow.setPadding(16, 12, 16, 12);
        reservationRow.setBackgroundColor(getResources().getColor(android.R.color.white));
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 0, 0, 1); // 1px divider
        reservationRow.setLayoutParams(rowParams);

        // Date Column
        TextView dateText = new TextView(this);
        dateText.setText(date);
        dateText.setTextSize(14);
        dateText.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        dateText.setLayoutParams(dateParams);

        // Time Column
        TextView timeText = new TextView(this);
        timeText.setText(time);
        timeText.setTextSize(14);
        timeText.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        timeText.setLayoutParams(timeParams);

        // Guests Column
        TextView guestsText = new TextView(this);
        guestsText.setText(guests);
        guestsText.setTextSize(14);
        guestsText.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams guestsParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        guestsText.setLayoutParams(guestsParams);

        reservationRow.addView(dateText);
        reservationRow.addView(timeText);
        reservationRow.addView(guestsText);

        reservationsContainer.addView(reservationRow);
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
