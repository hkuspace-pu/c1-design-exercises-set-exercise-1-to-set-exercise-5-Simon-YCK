package com.example.restaurantapp.utils;

import android.content.Context;
import com.example.restaurantapp.database.DatabaseHelper;

public class ReservationFacade {

    private DatabaseHelper dbHelper;
    private NotificationHelper notificationHelper;

    public ReservationFacade(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.notificationHelper = new NotificationHelper(context);
    }

    // ✅ UPDATED: Add specialRequests parameter
    public boolean placeReservation(String name, String date, String time, int guests, String specialRequests) {
        // 1. Save to DB
        boolean success = dbHelper.addReservation(name, date, time, guests, specialRequests);

        // 2. Send Notification if successful
        if (success) {
            notificationHelper.sendBookingNotification(name, date, time);
        }

        return success;
    }
}
