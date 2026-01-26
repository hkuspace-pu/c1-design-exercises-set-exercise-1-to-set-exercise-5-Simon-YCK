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
    private LinearLayout reservationsContainer; // Changed variable name to match ID for clarity
    private TextView notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        dbHelper = new DatabaseHelper(this);

        // FIX: Match the XML ID 'reservationsContainer'
        reservationsContainer = findViewById(R.id.reservationsContainer);

        // Find notification badge (Ensure ID exists in XML)
        notificationBadge = findViewById(R.id.notificationBadge);

        // 1. BOOK TABLE BUTTON
        // Note: XML ID is 'bookTableButton' (LinearLayout), so we attach click listener to that
        View btnBookTable = findViewById(R.id.bookTableButton);
        if (btnBookTable != null) {
            btnBookTable.setOnClickListener(v ->
                    startActivity(new Intent(this, GuestReservationActivity.class)));
        }

        // 2. BROWSE MENU BUTTON
        View btnViewMenu = findViewById(R.id.browseMenuButton);
        if (btnViewMenu != null) {
            btnViewMenu.setOnClickListener(v ->
                    startActivity(new Intent(this, GuestMenuBrowseActivity.class)));
        }

        // 3. PROFILE SETTINGS (Avatar)
        View btnProfile = findViewById(R.id.profileAvatar);
        if(btnProfile != null) {
            btnProfile.setOnClickListener(v ->
                    startActivity(new Intent(this, UserProfileActivity.class)));
        }

        // 4. NOTIFICATION BELL (Open History)
        View btnBell = findViewById(R.id.notificationButton);
        if (btnBell != null) {
            btnBell.setOnClickListener(v ->
                    startActivity(new Intent(this, NotificationListActivity.class)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUpcomingReservations();
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

    private void loadUpcomingReservations() {
        if (reservationsContainer == null) return;

        reservationsContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllReservations();

        // Toggle Empty State if you have one
        View emptyState = findViewById(R.id.emptyState);

        if (cursor.getCount() == 0) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            return;
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            int guests = cursor.getInt(4);

            // Inflate your ticket layout dynamically
            View ticket = getLayoutInflater().inflate(R.layout.item_reservation_ticket, reservationsContainer, false);

            // Populate Ticket Data
            TextView tDate = ticket.findViewById(R.id.reservationDate);
            TextView tTime = ticket.findViewById(R.id.reservationTime);
            TextView tGuests = ticket.findViewById(R.id.guestCount);

            if(tDate != null) tDate.setText(date);
            if(tTime != null) tTime.setText(time);
            if(tGuests != null) tGuests.setText(guests + " Guests");

            // Handle Click (Edit)
            ticket.setOnClickListener(v -> {
                Intent i = new Intent(this, GuestEditReservationActivity.class);
                i.putExtra("resId", id);
                i.putExtra("date", date);
                i.putExtra("guests", guests);
                startActivity(i);
            });

            reservationsContainer.addView(ticket);
        }
        cursor.close();
    }
}
