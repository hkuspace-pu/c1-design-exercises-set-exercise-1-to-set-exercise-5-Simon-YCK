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
    private static final int DATABASE_VERSION = 7; // ✅ Version 5 for special_requests

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
    private static final String COL_RES_SPECIAL = "special_requests"; // ✅ ADD THIS

    // --- TABLE: NOTIFICATIONS ---
    private static final String TABLE_NOTIF = "notifications";
    private static final String COL_NOTIF_ID = "id";
    private static final String COL_NOTIF_TITLE = "title";
    private static final String COL_NOTIF_MSG = "message";
    private static final String COL_NOTIF_TYPE = "type";
    private static final String COL_NOTIF_DATE = "timestamp";
    private static final String COL_NOTIF_READ = "is_read";
    private static final String COL_NOTIF_USER = "username"; 

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
                COL_NOTIF_READ + " INTEGER DEFAULT 0, " +
                COL_NOTIF_USER + " TEXT)";
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
                  String imagePath = cursor.getString(5); // ✅ ADD THIS - get image path from column 5

                  list.add(new MenuItem(id, name, desc, price, category, imagePath)); // ✅ ADD imagePath
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

    // ✅ ADD: Get notifications by username
    public Cursor getNotificationsByUser(String username) {
         SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIF +
                           " WHERE " + COL_NOTIF_USER + " = ? " +
                           " ORDER BY " + COL_NOTIF_DATE + " DESC",
                           new String[]{username});
    }

    // ✅ UPDATE: Get unread count by username
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

    // ✅ ADD this method to DatabaseHelper.java (for staff to mark ALL notifications as read)
    public void markAllAsRead() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOTIF_READ, 1);
        db.update(TABLE_NOTIF, cv, COL_NOTIF_READ + " = 0", null);
    }

    // ✅ UPDATE: Mark all as read for specific user
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

    public Cursor getReservationsByGuest(String guestName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVATION +
                          " WHERE " + COL_RES_GUEST + " = ? " +
                          " ORDER BY " + COL_RES_ID + " DESC",
                          new String[]{guestName});
    }
}
