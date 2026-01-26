package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import java.util.ArrayList;

public class NotificationListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list); // We need to create this XML

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewNotifications);

        loadNotifications();

        // Mark as read when opened
        dbHelper.markAllAsRead();
    }

    private void loadNotifications() {
        ArrayList<String> notifs = new ArrayList<>();
        Cursor cursor = dbHelper.getAllNotifications();

        if (cursor.getCount() == 0) {
            notifs.add("No notifications yet.");
        } else {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                String msg = cursor.getString(2);
                notifs.add(title + "\n" + msg);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, notifs);
        listView.setAdapter(adapter);
    }
}
