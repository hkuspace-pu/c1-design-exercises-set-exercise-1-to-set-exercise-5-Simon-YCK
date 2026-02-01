package com.example.restaurantapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restaurantapp.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Restaurant.db";
    private static final int DATABASE_VERSION = 10; //

    // --- TABLE: MENU ---
    private static final String TABLE_MENU = "menu_items";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DESC = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_CATEGORY = "category";
    private static final String COL_IMAGE = "image_path";

    // --- TABLE: RESERVATIONS ---
    private static final String TABLE_RESERVATION = "reservations";
    private static final String COL_RES_ID = "res_id";
    private static final String COL_RES_GUEST = "guest_name";
    private static final String COL_RES_DATE = "res_date";
    private static final String COL_RES_TIME = "res_time";
    private static final String COL_RES_COUNT = "guest_count";
    private static final String COL_RES_SPECIAL = "special_requests";

    // --- TABLE: NOTIFICATIONS ---
    private static final String TABLE_NOTIF = "notifications";
    private static final String COL_NOTIF_ID = "id";
    private static final String COL_NOTIF_TITLE = "title";
    private static final String COL_NOTIF_MSG = "message";
    private static final String COL_NOTIF_TYPE = "type";
    private static final String COL_NOTIF_DATE = "timestamp";
    private static final String COL_NOTIF_READ = "is_read";
    private static final String COL_NOTIF_USER = "username";

    //  NEW TABLE: USER PREFERENCES
    private static final String TABLE_PREFERENCES = "user_preferences";
    private static final String COL_PREF_USERNAME = "username";
    private static final String COL_PREF_BOOKING_NEW = "notif_booking_new";
    private static final String COL_PREF_BOOKING_UPDATE = "notif_booking_update";
    private static final String COL_PREF_BOOKING_CANCEL = "notif_booking_cancel";
    private static final String COL_PREF_UPDATES = "notif_updates";
    private static final String COL_PREF_PROMOTIONS = "notif_promotions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Menu Table
        String createMenu = "CREATE TABLE " + TABLE_MENU + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_CATEGORY + " TEXT, " +
                COL_IMAGE + " TEXT)";
        db.execSQL(createMenu);

        // Create Reservation Table WITH special_requests
        String createRes = "CREATE TABLE " + TABLE_RESERVATION + " (" +
                COL_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RES_GUEST + " TEXT, " +
                COL_RES_DATE + " TEXT, " +
                COL_RES_TIME + " TEXT, " +
                COL_RES_COUNT + " INTEGER, " +
                COL_RES_SPECIAL + " TEXT)";
        db.execSQL(createRes);

        // Create Notification Table
        String createNotif = "CREATE TABLE " + TABLE_NOTIF + " (" +
                COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTIF_TITLE + " TEXT, " +
                COL_NOTIF_MSG + " TEXT, " +
                COL_NOTIF_TYPE + " TEXT, " +
                COL_NOTIF_DATE + " TEXT, " +
                COL_NOTIF_READ + " INTEGER DEFAULT 0, " +
                COL_NOTIF_USER + " TEXT)";
        db.execSQL(createNotif);

        // Create User Preferences Table
        String createPreferences = "CREATE TABLE " + TABLE_PREFERENCES + " (" +
                COL_PREF_USERNAME + " TEXT PRIMARY KEY, " +
                COL_PREF_BOOKING_NEW + " INTEGER DEFAULT 1, " +
                COL_PREF_BOOKING_UPDATE + " INTEGER DEFAULT 1, " +
                COL_PREF_BOOKING_CANCEL + " INTEGER DEFAULT 1, " +
                COL_PREF_UPDATES + " INTEGER DEFAULT 1, " +
                COL_PREF_PROMOTIONS + " INTEGER DEFAULT 1)";
        db.execSQL(createPreferences);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add notification table if upgrading from version < 3
        if (oldVersion < 3) {
            String createNotif = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIF + " (" +
                    COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NOTIF_TITLE + " TEXT, " +
                    COL_NOTIF_MSG + " TEXT, " +
                    COL_NOTIF_TYPE + " TEXT, " +
                    COL_NOTIF_DATE + " TEXT, " +
                    COL_NOTIF_READ + " INTEGER DEFAULT 0)";
            db.execSQL(createNotif);
        }

        // Add special_requests column if upgrading from version < 6
        if (oldVersion < 6) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_NOTIF + " ADD COLUMN " + COL_NOTIF_USER + " TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (oldVersion < 7) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_MENU + " ADD COLUMN " + COL_IMAGE + " TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Create user preferences table if upgrading from version < 10
        if (oldVersion < 10) {
            try {
                String createPreferences = "CREATE TABLE IF NOT EXISTS " + TABLE_PREFERENCES + " (" +
                        COL_PREF_USERNAME + " TEXT PRIMARY KEY, " +
                        COL_PREF_BOOKING_NEW + " INTEGER DEFAULT 1, " +
                        COL_PREF_BOOKING_UPDATE + " INTEGER DEFAULT 1, " +
                        COL_PREF_BOOKING_CANCEL + " INTEGER DEFAULT 1, " +
                        COL_PREF_UPDATES + " INTEGER DEFAULT 1, " +
                        COL_PREF_PROMOTIONS + " INTEGER DEFAULT 1)";
                db.execSQL(createPreferences);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== MENU OPERATIONS ====================

    public boolean addMenuItem(String name, String desc, double price, String category, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_IMAGE, imagePath);
        return db.insert(TABLE_MENU, null, cv) != -1;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                double price = cursor.getDouble(3);
                String category = cursor.getString(4);
                String imagePath = cursor.getString(5);

                list.add(new MenuItem(id, name, desc, price, category, imagePath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateMenuItem(int id, String name, String desc, double price, String category, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_IMAGE, imagePath);
        return db.update(TABLE_MENU, cv, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getMenuByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + COL_CATEGORY + " = ?",
                new String[]{category});
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COL_CATEGORY + " FROM " + TABLE_MENU +
                " ORDER BY " + COL_CATEGORY, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return categories;
    }

    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==================== RESERVATION OPERATIONS ====================

    public boolean addReservation(String name, String date, String time, int guests, String specialRequests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RES_GUEST, name);
        cv.put(COL_RES_DATE, date);
        cv.put(COL_RES_TIME, time);
        cv.put(COL_RES_COUNT, guests);
        cv.put(COL_RES_SPECIAL, specialRequests);
        return db.insert(TABLE_RESERVATION, null, cv) != -1;
    }

    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVATION + " ORDER BY " + COL_RES_ID + " DESC", null);
    }

    public boolean updateReservation(int id, String date, String time, int guests, String specialRequests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RES_DATE, date);
        cv.put(COL_RES_TIME, time);
        cv.put(COL_RES_COUNT, guests);
        cv.put(COL_RES_SPECIAL, specialRequests);
        return db.update(TABLE_RESERVATION, cv, COL_RES_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteReservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RESERVATION, COL_RES_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getReservationsByGuest(String guestName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVATION +
                        " WHERE " + COL_RES_GUEST + " = ? " +
                        " ORDER BY " + COL_RES_ID + " DESC",
                new String[]{guestName});
    }

    // ==================== NOTIFICATION OPERATIONS ====================

    public void addNotification(String title, String message, String type, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_TITLE, title);
        cv.put(COL_NOTIF_MSG, message);
        cv.put(COL_NOTIF_TYPE, type);
        cv.put(COL_NOTIF_DATE, String.valueOf(System.currentTimeMillis()));
        cv.put(COL_NOTIF_READ, 0);
        cv.put(COL_NOTIF_USER, username);
        db.insert(TABLE_NOTIF, null, cv);
    }

    public Cursor getNotificationsByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIF +
                        " WHERE " + COL_NOTIF_USER + " = ? " +
                        " ORDER BY " + COL_NOTIF_DATE + " DESC",
                new String[]{username});
    }

    public int getUnreadNotificationCountByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTIF +
                        " WHERE " + COL_NOTIF_READ + " = 0 AND " +
                        COL_NOTIF_USER + " = ?",
                new String[]{username});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void markAllAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_READ, 1);
        db.update(TABLE_NOTIF, cv, COL_NOTIF_READ + " = 0", null);
    }

    public void markAllAsReadForUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_READ, 1);
        db.update(TABLE_NOTIF, cv, COL_NOTIF_READ + " = 0 AND " + COL_NOTIF_USER + " = ?",
                new String[]{username});
    }

    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIF + " ORDER BY " + COL_NOTIF_DATE + " DESC", null);
    }

    // ==================== NOTIFICATION PREFERENCES OPERATIONS ====================

    /**
     * Update notification preferences for a user
     * @param username The username
     * @param *booking Enable/disable booking notifications
     * @param updates Enable/disable update notifications
     * @param promotions Enable/disable promotion notifications
     * @return true if successful
     */
    public boolean updateNotificationPreferences(String username, boolean bookingNew,
                                                 boolean bookingUpdate, boolean bookingCancel,
                                                 boolean updates, boolean promotions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PREF_USERNAME, username);
        cv.put(COL_PREF_BOOKING_NEW, bookingNew ? 1 : 0);
        cv.put(COL_PREF_BOOKING_UPDATE, bookingUpdate ? 1 : 0);
        cv.put(COL_PREF_BOOKING_CANCEL, bookingCancel ? 1 : 0);
        cv.put(COL_PREF_UPDATES, updates ? 1 : 0);
        cv.put(COL_PREF_PROMOTIONS, promotions ? 1 : 0);

        // Use REPLACE to insert or update
        long result = db.replace(TABLE_PREFERENCES, null, cv);
        return result != -1;
    }

    /**
     * Get notification preferences for a user
     * @param username The username
     * @return boolean array [booking, updates, promotions], defaults to all true
     */
    public boolean[] getNotificationPreferences(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                COL_PREF_BOOKING_NEW + ", " +
                COL_PREF_BOOKING_UPDATE + ", " +
                COL_PREF_BOOKING_CANCEL + ", " +
                COL_PREF_UPDATES + ", " +
                COL_PREF_PROMOTIONS +
                " FROM " + TABLE_PREFERENCES +
                " WHERE " + COL_PREF_USERNAME + " = ?", new String[]{username});

        boolean[] prefs = {true, true, true, true, true}; // Default all ON

        if (cursor != null && cursor.moveToFirst()) {
            prefs[0] = cursor.getInt(0) == 1; // Booking NEW
            prefs[1] = cursor.getInt(1) == 1; // Booking UPDATE
            prefs[2] = cursor.getInt(2) == 1; // Booking CANCEL
            prefs[3] = cursor.getInt(3) == 1; // General Updates
            prefs[4] = cursor.getInt(4) == 1; // Promotions
            cursor.close();
        }

        return prefs;
    }
}
