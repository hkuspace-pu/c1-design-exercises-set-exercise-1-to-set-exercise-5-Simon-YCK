package com.example.restaurantapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.restaurantapp.database.DatabaseHelper;

/**
 * NotificationHelper - Manages user-specific notifications with database-backed preferences
 * Supports 5 notification categories:
 * - New Reservations (prefs[0])
 * - Reservation Updates (prefs[1])
 * - Reservation Cancellations (prefs[2])
 * - General Updates (prefs[3])
 * - Promotions & Offers (prefs[4])
 */
public class NotificationHelper {
    private static final String CHANNEL_ID = "restaurant_app_channel";
    private Context context;
    private String currentUsername;
    private DatabaseHelper dbHelper;

    /**
     * Constructor - accepts username for per-user notifications
     * @param context Application context
     * @param username The username to send notifications to
     */
    public NotificationHelper(Context context, String username) {
        this.context = context;
        this.currentUsername = username;
        this.dbHelper = new DatabaseHelper(context);
        createChannel();
    }

    /**
     * Create notification channel (required for Android O+)
     */
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Restaurant Updates",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Booking confirmations, updates, and promotions");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * PRIVATE - Saves notification to database and shows system notification
     * @param title Notification title
     * @param message Notification message
     * @param type Notification type (booking/update/cancel/general/promo)
     */
    private void sendNotification(String title, String message, String type) {
        // Save to database (notification history)
        dbHelper.addNotification(title, message, type, currentUsername);

        // Show system notification
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

    /**
     * Get unread notification count for current user from database
     * @return Number of unread notifications
     */
    public int getUnreadCount() {
        return dbHelper.getUnreadNotificationCountByUser(currentUsername);
    }

    // ==================== PUBLIC NOTIFICATION METHODS ====================

    /**
     * 1. NEW RESERVATION CONFIRMED
     * Checks user's "New Reservation" preference before sending (prefs[0])
     * @param name Guest name
     * @param date Reservation date
     * @param time Reservation time
     */
    public void sendBookingNotification(String name, String date, String time) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        // Check if user has new booking notifications enabled (prefs[0])
        if (prefs[0]) {
            sendNotification("Booking Confirmed!",
                    "Table for " + name + " on " + date + " at " + time,
                    "booking");
        }
    }

    /**
     * 2. RESERVATION UPDATED
     * Checks user's "Reservation Updates" preference before sending (prefs[1])
     * @param date New reservation date
     * @param time New reservation time
     */
    public void sendUpdateNotification(String date, String time) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        // Check if user has booking update notifications enabled (prefs[1])
        if (prefs[1]) {
            sendNotification("Reservation Updated",
                    "Your booking has been changed to " + date + " at " + time,
                    "update");
        }
    }

    /**
     * 3. RESERVATION CANCELLED
     * Checks user's "Reservation Cancellations" preference before sending (prefs[2])
     */
    public void sendCancelNotification() {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        // Check if user has cancellation notifications enabled (prefs[2])
        if (prefs[2]) {
            sendNotification("Booking Cancelled",
                    "Your reservation has been cancelled",
                    "cancel");
        }
    }

    /**
     * 4. GENERAL UPDATE NOTIFICATION
     * Checks user's "General Updates" preference before sending (prefs[3])
     * @param title Notification title
     * @param message Notification message
     */
    public void sendGeneralUpdateNotification(String title, String message) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        // Check if user has general updates enabled (prefs[3])
        if (prefs[3]) {
            sendNotification(title, message, "general");
        }
    }

    /**
     * 5. PROMOTIONAL NOTIFICATION
     * Checks user's "Promotions & Offers" preference before sending (prefs[4])
     * @param message Promotional message
     */
    public void sendPromoNotification(String message) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        // Check if user has promotions enabled (prefs[4])
        if (prefs[4]) {
            sendNotification("Special Offer!", message, "promo");
        }
    }

    /**
     * OPTIONAL: Send notification without preference check (admin override)
     * Use this for critical notifications that should always be sent
     * @param title Notification title
     * @param message Notification message
     */
    public void sendCriticalNotification(String title, String message) {
        sendNotification(title, message, "critical");
    }

    // ==================== CONVENIENCE METHODS ====================

    /**
     * Send a new booking notification with guest count
     * @param name Guest name
     * @param date Reservation date
     * @param time Reservation time
     * @param guests Number of guests
     */
    public void sendBookingNotificationWithGuests(String name, String date, String time, int guests) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (prefs[0]) {
            sendNotification("Booking Confirmed!",
                    "Table for " + guests + " guests under " + name + " on " + date + " at " + time,
                    "booking");
        }
    }

    /**
     * Send update notification with full details
     * @param oldDate Previous date
     * @param oldTime Previous time
     * @param newDate New date
     * @param newTime New time
     */
    public void sendDetailedUpdateNotification(String oldDate, String oldTime, String newDate, String newTime) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (prefs[1]) {
            sendNotification("Reservation Updated",
                    "Changed from " + oldDate + " " + oldTime + " to " + newDate + " " + newTime,
                    "update");
        }
    }

    /**
     * Send cancellation with reason
     * @param reason Cancellation reason (optional)
     */
    public void sendCancelNotificationWithReason(String reason) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (prefs[2]) {
            String message = "Your reservation has been cancelled";
            if (reason != null && !reason.isEmpty()) {
                message += ". Reason: " + reason;
            }
            sendNotification("Booking Cancelled", message, "cancel");
        }
    }

    /**
     * Send menu update notification
     * @param itemName New menu item name
     */
    public void sendMenuUpdateNotification(String itemName) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (prefs[3]) {
            sendNotification("Menu Update",
                    "New item added: " + itemName,
                    "general");
        }
    }

    /**
     * Send time-limited promotion
     * @param title Promotion title
     * @param message Promotion details
     * @param expiryDate When promotion expires
     */
    public void sendTimedPromoNotification(String title, String message, String expiryDate) {
        boolean[] prefs = dbHelper.getNotificationPreferences(currentUsername);

        if (prefs[4]) {
            sendNotification(title, message + " (Valid until " + expiryDate + ")", "promo");
        }
    }
}
