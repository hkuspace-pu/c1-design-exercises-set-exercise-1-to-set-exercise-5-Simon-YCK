package com.example.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;

public class GuestDashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout upcomingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        dbHelper = new DatabaseHelper(this);
        upcomingContainer = findViewById(R.id.reservationsContainer); // Ensure this ID exists in XML

        // Navigation Buttons
        findViewById(R.id.bookTableButton).setOnClickListener(v ->
                startActivity(new Intent(this, GuestReservationActivity.class)));

        findViewById(R.id.browseMenuButton).setOnClickListener(v ->
                startActivity(new Intent(this, GuestMenuBrowseActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpcomingReservations(); // Refresh list every time we return
    }

    private void loadUpcomingReservations() {
        upcomingContainer.removeAllViews(); // Clear old list
        Cursor cursor = dbHelper.getAllReservations();

        if (cursor.getCount() == 0) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No upcoming bookings.");
            emptyView.setPadding(16, 16, 16, 16);
            upcomingContainer.addView(emptyView);
            return;
        }

        while (cursor.moveToNext()) {
            // Get data
            int id = cursor.getInt(0);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            int guests = cursor.getInt(4);

            // Inflate your ticket layout dynamically
            View ticket = getLayoutInflater().inflate(R.layout.item_reservation_ticket, upcomingContainer, false);

            // Populate Ticket UI
            TextView tvDate = ticket.findViewById(R.id.reservationDate);
            TextView tvTime = ticket.findViewById(R.id.reservationTime);
            TextView tvGuests = ticket.findViewById(R.id.guestCount);

            if (tvDate != null) tvDate.setText(date);
            if (tvTime != null) tvTime.setText(time);
            if (tvGuests != null) tvGuests.setText(guests + " Guests");

            // Handle Edit Click
            ticket.setOnClickListener(v -> {
                Intent intent = new Intent(GuestDashboardActivity.this, GuestEditReservationActivity.class);
                intent.putExtra("resId", id);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("guests", guests);
                startActivity(intent);
            });

            upcomingContainer.addView(ticket);
        }
        cursor.close();
    }
}
