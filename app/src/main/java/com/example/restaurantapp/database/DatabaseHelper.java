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
    private static final int DATABASE_VERSION = 5; // ✅ Version 5 for special_requests

    // --- TABLE: MENU ---
    private static final String TABLE_MENU = "menu_items";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DESC = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_CATEGORY = "category";

    // --- TABLE: RESERVATIONS ---
    private static final String TABLE_RESERVATION = "reservations";
    private static final String COL_RES_ID = "res_id";
    private static final String COL_RES_GUEST = "guest_name";
    private static final String COL_RES_DATE = "res_date";
    private static final String COL_RES_TIME = "res_time";
    private static final String COL_RES_COUNT = "guest_count";
    private static final String COL_RES_SPECIAL = "special_requests"; // ✅ ADD THIS

    // --- TABLE: NOTIFICATIONS ---
    private static final String TABLE_NOTIF = "notifications";
    private static final String COL_NOTIF_ID = "id";
    private static final String COL_NOTIF_TITLE = "title";
    private static final String COL_NOTIF_MSG = "message";
    private static final String COL_NOTIF_TYPE = "type";
    private static final String COL_NOTIF_DATE = "timestamp";
    private static final String COL_NOTIF_READ = "is_read";

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
                COL_CATEGORY + " TEXT)";
        db.execSQL(createMenu);

        // ✅ Create Reservation Table WITH special_requests
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
                COL_NOTIF_READ + " INTEGER DEFAULT 0)";
        db.execSQL(createNotif);
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

        // ✅ Add special_requests column if upgrading from version < 5
        if (oldVersion < 5) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_RESERVATION + " ADD COLUMN " + COL_RES_SPECIAL + " TEXT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== MENU OPERATIONS ====================

    public boolean addMenuItem(String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);
        return db.insert(TABLE_MENU, null, cv) != -1;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new MenuItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateMenuItem(int id, String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);
        return db.update(TABLE_MENU, cv, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==================== RESERVATION OPERATIONS ====================

    // ✅ UPDATED: 5 parameters including specialRequests
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

    // ✅ UPDATED: 5 parameters including specialRequests
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

    // ==================== NOTIFICATION OPERATIONS ====================

    public void addNotification(String title, String message, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_TITLE, title);
        cv.put(COL_NOTIF_MSG, message);
        cv.put(COL_NOTIF_TYPE, type);
        cv.put(COL_NOTIF_DATE, String.valueOf(System.currentTimeMillis()));
        cv.put(COL_NOTIF_READ, 0);
        db.insert(TABLE_NOTIF, null, cv);
    }

    public int getUnreadNotificationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTIF + " WHERE " + COL_NOTIF_READ + " = 0", null);
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

    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIF + " ORDER BY " + COL_NOTIF_DATE + " DESC", null);
    }
}
