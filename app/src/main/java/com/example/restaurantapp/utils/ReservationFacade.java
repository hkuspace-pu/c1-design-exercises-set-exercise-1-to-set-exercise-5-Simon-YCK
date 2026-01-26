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

    // Create new reservation
    public boolean createReservation(String name, String date, String time, int guests) {
        boolean success = dbHelper.addReservation(name, date, time, guests);
        if (success) {
            notificationHelper.sendBookingNotification(name, date, time);
        }
        return success;
    }

    // Update existing reservation
    public boolean updateReservation(int id, String date, String time, int guests) {
        boolean success = dbHelper.updateReservation(id, date, time, guests);
        if (success) {
            notificationHelper.sendUpdateNotification(date, time);
        }
        return success;
    }

    // Cancel reservation
    public boolean cancelReservation(int id) {
        boolean success = dbHelper.deleteReservation(id);
        if (success) {
            notificationHelper.sendCancelNotification();
        }
        return success;
    }
}
