package com.example.restaurantapp.utils;

import android.content.Context;
import com.example.restaurantapp.database.DatabaseHelper;

// FACADE PATTERN: Hides complexity of DB + Notifications
public class ReservationFacade {
    private DatabaseHelper dbHelper;
    private NotificationHelper notificationHelper;

    public ReservationFacade(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.notificationHelper = new NotificationHelper(context);
    }

    public boolean placeReservation(String name, String date, String time, int guests) {
        // 1. Save to DB
        boolean success = dbHelper.addReservation(name, date, time, guests);

        // 2. Send Notification if successful
        if (success) {
            notificationHelper.sendBookingNotification(name, date, time);
        }

        return success;
    }
}
