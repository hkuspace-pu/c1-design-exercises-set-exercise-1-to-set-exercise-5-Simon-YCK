package com.example.restaurantapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.restaurantapp.database.DatabaseHelper;

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

    private void sendNotification(String title, String message) {
        // 1. Save to Database (History)
        DatabaseHelper db = new DatabaseHelper(context);
        db.addNotification(title, message);

        // 2. Build System Notification
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

    public void sendBookingNotification(String name, String date, String time) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("notif_booking", true)) {
            sendNotification("Booking Confirmed!", "Table for " + name + " on " + date + " at " + time);
        }
    }

    public void sendPromoNotification(String message) {
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("notif_promo", false)) {
            sendNotification("Special Offer!", message);
        }
    }
}
