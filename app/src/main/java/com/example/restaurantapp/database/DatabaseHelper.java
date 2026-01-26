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
    // INCREMENT VERSION TO 3 TO TRIGGER UPGRADE
    private static final int DATABASE_VERSION = 3;

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

    // --- TABLE: NOTIFICATIONS (NEW) ---
    private static final String TABLE_NOTIF = "notifications";
    private static final String COL_NOTIF_ID = "id";
    private static final String COL_NOTIF_TITLE = "title";
    private static final String COL_NOTIF_MSG = "message";
    private static final String COL_NOTIF_DATE = "date_created";
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

        // Create Reservation Table
        String createRes = "CREATE TABLE " + TABLE_RESERVATION + " (" +
                COL_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RES_GUEST + " TEXT, " +
                COL_RES_DATE + " TEXT, " +
                COL_RES_TIME + " TEXT, " +
                COL_RES_COUNT + " INTEGER)";
        db.execSQL(createRes);

        // Create Notification Table
        String createNotif = "CREATE TABLE " + TABLE_NOTIF + " (" +
                COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTIF_TITLE + " TEXT, " +
                COL_NOTIF_MSG + " TEXT, " +
                COL_NOTIF_DATE + " TEXT, " +
                COL_NOTIF_READ + " INTEGER)";
        db.execSQL(createNotif);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Add notification table if upgrading from v2
            String createNotif = "CREATE TABLE " + TABLE_NOTIF + " (" +
                    COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NOTIF_TITLE + " TEXT, " +
                    COL_NOTIF_MSG + " TEXT, " +
                    COL_NOTIF_DATE + " TEXT, " +
                    COL_NOTIF_READ + " INTEGER)";
            db.execSQL(createNotif);
        }
        // Ideally handle other upgrades properly, but for assessment drop/create is often okay
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        // onCreate(db);
    }

    // --- MENU OPS ---
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
                list.add(new MenuItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateMenuItem(int id, String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name); cv.put(COL_DESC, desc); cv.put(COL_PRICE, price); cv.put(COL_CATEGORY, category);
        return db.update(TABLE_MENU, cv, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- RESERVATION OPS ---
    public boolean addReservation(String name, String date, String time, int guests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RES_GUEST, name); cv.put(COL_RES_DATE, date); cv.put(COL_RES_TIME, time); cv.put(COL_RES_COUNT, guests);
        return db.insert(TABLE_RESERVATION, null, cv) != -1;
    }

    public Cursor getAllReservations() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_RESERVATION + " ORDER BY " + COL_RES_ID + " DESC", null);
    }

    public boolean updateReservation(int id, String date, String time, int guests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RES_DATE, date); cv.put(COL_RES_TIME, time); cv.put(COL_RES_COUNT, guests);
        return db.update(TABLE_RESERVATION, cv, COL_RES_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteReservation(int id) {
        return this.getWritableDatabase().delete(TABLE_RESERVATION, COL_RES_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- NOTIFICATION OPS (NEW) ---
    public void addNotification(String title, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_TITLE, title);
        cv.put(COL_NOTIF_MSG, message);
        cv.put(COL_NOTIF_DATE, String.valueOf(System.currentTimeMillis()));
        cv.put(COL_NOTIF_READ, 0); // 0 = unread
        db.insert(TABLE_NOTIF, null, cv);
    }

    public Cursor getAllNotifications() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NOTIF + " ORDER BY " + COL_NOTIF_ID + " DESC", null);
    }

    public int getUnreadCount() {
        Cursor c = this.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTIF + " WHERE " + COL_NOTIF_READ + " = 0", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public void markAllAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_READ, 1);
        db.update(TABLE_NOTIF, cv, null, null);
    }
}
