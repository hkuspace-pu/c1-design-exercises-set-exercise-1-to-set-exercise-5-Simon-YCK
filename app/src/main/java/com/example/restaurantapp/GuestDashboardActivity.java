package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    private TextView guestNameText;
    private View bookTableButton;  // Changed from Button to View
    private View browseMenuButton; // Changed from Button to View
    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        // Initialize views
        guestNameText = findViewById(R.id.guestNameText);
        bookTableButton = findViewById(R.id.bookTableButton);
        browseMenuButton = findViewById(R.id.browseMenuButton);
        reservationsContainer = findViewById(R.id.reservationsContainer);

        // Get guest name from intent
        String guestName = getIntent().getStringExtra("guestName");
        if (guestName != null && !guestName.isEmpty()) {
            guestNameText.setText(guestName);
        }

        // Notification button
        findViewById(R.id.notificationButton).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "🔔 You have 2 new notifications:\n• Table confirmed for Nov 29\n• Special menu today!", android.widget.Toast.LENGTH_LONG).show();
        });

        // Book Table button
        bookTableButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuestDashboardActivity.this, GuestReservationActivity.class);
            startActivity(intent);
        });

        // Browse Menu button
        browseMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuestDashboardActivity.this, GuestMenuBrowseActivity.class);
            startActivity(intent);
        });

        // Load sample reservations
        loadSampleReservations();
    }

    private void loadSampleReservations() {
        // Sample data
        String[][] reservations = {
                {"Friday, Nov 29", "7:00 PM", "4"},
                {"Saturday, Dec 7", "6:30 PM", "2"}
        };

        for (String[] res : reservations) {
            addReservationCard(res[0], res[1], res[2]);
        }
    }

    private void addReservationCard(String date, String time, String guests) {
        View ticketView = LayoutInflater.from(this).inflate(R.layout.item_reservation_ticket, reservationsContainer, false);

        TextView dateText = ticketView.findViewById(R.id.reservationDate);
        TextView timeText = ticketView.findViewById(R.id.reservationTime);
        TextView guestCountText = ticketView.findViewById(R.id.guestCount);

        dateText.setText(date);
        timeText.setText(time);
        guestCountText.setText(guests + " guests");

        // Make card clickable to edit
        ticketView.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestEditReservationActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("guests", guests);
            startActivity(intent);
        });

        reservationsContainer.addView(ticketView);
    }
}
