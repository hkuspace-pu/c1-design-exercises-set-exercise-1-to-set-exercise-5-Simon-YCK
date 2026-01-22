package com.example.restaurantapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restaurantapp.model.MenuItem;
// Note: You will need a Reservation model later, I'll provide it in next step.
// For now this helper supports the raw SQL logic.

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "Restaurant.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to add Reservation table

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Menu Table
        String createMenuTable = "CREATE TABLE " + TABLE_MENU + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_CATEGORY + " TEXT)";
        db.execSQL(createMenuTable);

        // Create Reservation Table
        String createResTable = "CREATE TABLE " + TABLE_RESERVATION + " (" +
                COL_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RES_GUEST + " TEXT, " +
                COL_RES_DATE + " TEXT, " +
                COL_RES_TIME + " TEXT, " +
                COL_RES_COUNT + " INTEGER)";
        db.execSQL(createResTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        // Create tables again
        onCreate(db);
    }

    // ==========================================
    //              MENU OPERATIONS
    // ==========================================

    // CREATE (Add Item)
    public boolean addMenuItem(String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);

        long result = db.insert(TABLE_MENU, null, cv);
        return result != -1;
    }

    // READ (Get All Items)
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                double price = cursor.getDouble(3);
                String cat = cursor.getString(4);
                menuList.add(new MenuItem(id, name, desc, price, cat));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menuList;
    }

    // UPDATE
    public boolean updateMenuItem(int id, String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);

        int rows = db.update(TABLE_MENU, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // DELETE
    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ==========================================
    //           RESERVATION OPERATIONS
    // ==========================================

    // CREATE RESERVATION
    public boolean addReservation(String guestName, String date, String time, int guests) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RES_GUEST, guestName);
        cv.put(COL_RES_DATE, date);
        cv.put(COL_RES_TIME, time);
        cv.put(COL_RES_COUNT, guests);

        long result = db.insert(TABLE_RESERVATION, null, cv);
        return result != -1;
    }

    // READ RESERVATIONS
    // (We return Cursor for now, or List<Reservation> if you create that model class)
    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVATION, null);
    }

    // DELETE RESERVATION
    public boolean deleteReservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_RESERVATION, COL_RES_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
