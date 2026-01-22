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

    // Database Info
    private static final String DATABASE_NAME = "Restaurant.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_MENU = "menu_items";

    // Column Names
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DESC = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Menu Table
        String createTable = "CREATE TABLE " + TABLE_MENU + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_CATEGORY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    // CRUD OPERATIONS (Create, Read, Update, Delete)

    public boolean addMenuItem(String name, String desc, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESC, desc);
        cv.put(COL_PRICE, price);
        cv.put(COL_CATEGORY, category);

        long result = db.insert(TABLE_MENU, null, cv);
        return result != -1; // Returns true if insert successful
    }

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
}
