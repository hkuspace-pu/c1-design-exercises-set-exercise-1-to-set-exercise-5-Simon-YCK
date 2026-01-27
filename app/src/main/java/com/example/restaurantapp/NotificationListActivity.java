package com.example.restaurantapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private Button btnClearAll, btnBack;

    // ✅ ADD THESE TWO FIELDS
    private String currentUsername;
    private boolean isStaff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewNotifications);
        btnClearAll = findViewById(R.id.btnClearAll);
        btnBack = findViewById(R.id.btnBack);

        // ✅ ADD: Get username and role from intent
        currentUsername = getIntent().getStringExtra("username");
        isStaff = getIntent().getBooleanExtra("isStaff", false);

        loadNotifications();

        // ✅ CHANGED: Mark as read based on user role
        if (isStaff) {
            dbHelper.markAllAsRead(); // Staff mark all as read
        } else {
            dbHelper.markAllAsReadForUser(currentUsername); // Guest mark only theirs
        }

        // Clear All Button
        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> {
                clearAllNotifications();
                Toast.makeText(this, "All notifications cleared", Toast.LENGTH_SHORT).show();
                loadNotifications();
            });
        }

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                setResult(RESULT_OK);
                finish();
            });
        }
    }

    // ✅ UPDATED: Clear based on user role
    private void clearAllNotifications() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (isStaff) {
            db.execSQL("DELETE FROM notifications"); // Staff clear all
        } else {
            db.execSQL("DELETE FROM notifications WHERE username = ?", new String[]{currentUsername}); // ✅ Guest clear only theirs
        }
    }

    // ✅ UPDATED: Load based on user role
    private void loadNotifications() {
        List<NotificationItem> items = new ArrayList<>();

        // ✅ CHANGED: Use filtered query
        Cursor cursor;
        if (isStaff) {
            cursor = dbHelper.getAllNotifications(); // Staff see all
        } else {
            cursor = dbHelper.getNotificationsByUser(currentUsername); // ✅ Guest see only theirs
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                String msg = cursor.getString(2);
                String date = cursor.getString(4); // ✅ CHANGED: timestamp is column 4 now (was 3)
                items.add(new NotificationItem(title, msg, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (items.isEmpty()) {
            items.add(new NotificationItem("No notifications", "You're all caught up!", ""));
        }

        NotificationAdapter adapter = new NotificationAdapter(this, items);
        listView.setAdapter(adapter);
    }

    static class NotificationItem {
        String title, message, date;
        public NotificationItem(String t, String m, String d) { title=t; message=m; date=d; }
    }

    class NotificationAdapter extends ArrayAdapter<NotificationItem> {
        public NotificationAdapter(Context context, List<NotificationItem> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification_row, parent, false);
            }
            NotificationItem item = getItem(position);

            TextView title = convertView.findViewById(R.id.notifTitle);
            TextView msg = convertView.findViewById(R.id.notifMessage);
            TextView date = convertView.findViewById(R.id.notifDate);

            title.setText(item.title);
            msg.setText(item.message);

            try {
                long timeMillis = Long.parseLong(item.date);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
                date.setText(sdf.format(new Date(timeMillis)));
            } catch (Exception e) {
                date.setText("Recent");
            }

            return convertView;
        }
    }
}
