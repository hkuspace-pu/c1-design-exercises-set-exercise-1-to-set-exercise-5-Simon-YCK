package com.example.restaurantapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.utils.NotificationHelper;

public class GuestDashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout reservationsContainer;
    private TextView notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        dbHelper = new DatabaseHelper(this);
        reservationsContainer = findViewById(R.id.reservationsContainer);
        notificationBadge = findViewById(R.id.notificationBadge);

        // 1. BOOK TABLE BUTTON
        View btnBookTable = findViewById(R.id.bookTableButton);
        if (btnBookTable != null) {
            btnBookTable.setOnClickListener(v -> {
                Intent intent = new Intent(this, GuestReservationActivity.class);
                intent.putExtra("guestName", getIntent().getStringExtra("guestName")); // ✅ Pass name
                startActivity(intent);
            });
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
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("guestName", getIntent().getStringExtra("guestName"));
                startActivity(intent);
            });
        }

        // 4. NOTIFICATION BELL (Open History)
        View btnBell = findViewById(R.id.notificationButton);
        if (btnBell != null) {
            btnBell.setOnClickListener(v -> {
                Intent intent = new Intent(this, NotificationListActivity.class);
                startActivityForResult(intent, 100);
            });
        }

        // Initial load
        refreshDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            updateNotificationBadge();
        }
    }

    private void refreshDashboard() {
        loadUpcomingReservations();
        updateNotificationBadge();
    }

    private void loadUpcomingReservations() {
        if (reservationsContainer == null) return;

        reservationsContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllReservations();

        View emptyState = findViewById(R.id.emptyState);

        if (cursor.getCount() == 0) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            cursor.close();
            return;
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String guestName = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            int guests = cursor.getInt(4);
            String specialReq = cursor.getString(5);

            View ticket = getLayoutInflater().inflate(R.layout.item_reservation_ticket, reservationsContainer, false);

            TextView tDate = ticket.findViewById(R.id.reservationDate);
            TextView tTime = ticket.findViewById(R.id.reservationTime);
            TextView tGuests = ticket.findViewById(R.id.guestCount);

            if(tDate != null) tDate.setText(date);
            if(tTime != null) tTime.setText(time);
            if(tGuests != null) tGuests.setText(guests + " Guests");

            ticket.setOnClickListener(v -> {
                Intent i = new Intent(this, GuestEditReservationActivity.class);
                i.putExtra("resId", id);
                i.putExtra("name", guestName); // ✅ Pass name
                i.putExtra("date", date);
                i.putExtra("time", time);
                i.putExtra("guests", guests);
                i.putExtra("specialRequests", specialReq);
                startActivity(i);
            });

            reservationsContainer.addView(ticket);
        }
        cursor.close();
    }

    private void updateNotificationBadge() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        int unreadCount = notificationHelper.getUnreadCount();

        if (notificationBadge != null) {
            if (unreadCount > 0) {
                notificationBadge.setVisibility(View.VISIBLE);
                notificationBadge.setText(String.valueOf(unreadCount));
            } else {
                notificationBadge.setVisibility(View.GONE);
            }
        }
    }
}
