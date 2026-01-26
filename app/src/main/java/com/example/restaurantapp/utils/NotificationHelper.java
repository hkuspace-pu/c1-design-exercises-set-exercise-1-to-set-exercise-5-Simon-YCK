package com.example.restaurantapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "restaurant_app_channel";
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Restaurant Updates", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    // INTERNAL HELPER to actually build/show the notification
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    // --- PUBLIC METHODS CALLED BY YOUR APP ---

    // 1. Send Booking Notification (Checks 'notif_booking' pref)
    public void sendBookingNotification(String name, String date, String time) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean allowBooking = prefs.getBoolean("notif_booking", true);

        if (allowBooking) {
            sendNotification("Booking Confirmed!", "Table for " + name + " on " + date + " at " + time);
        }
    }

    // 2. Send Promo Notification (Checks 'notif_promo' pref)
    public void sendPromoNotification(String message) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean allowPromo = prefs.getBoolean("notif_promo", false);

        if (allowPromo) {
            sendNotification("Special Offer!", message);
        }
    }
}
