package com.example.restaurantapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewNotifications);

        loadNotifications();
        dbHelper.markAllAsRead();
    }

    private void loadNotifications() {
        List<NotificationItem> items = new ArrayList<>();
        Cursor cursor = dbHelper.getAllNotifications();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                String msg = cursor.getString(2);
                String date = cursor.getString(3);
                items.add(new NotificationItem(title, msg, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        NotificationAdapter adapter = new NotificationAdapter(this, items);
        listView.setAdapter(adapter);
    }

    // --- INNER CLASS: MODEL ---
    static class NotificationItem {
        String title, message, date;
        public NotificationItem(String t, String m, String d) { title=t; message=m; date=d; }
    }

    // --- INNER CLASS: ADAPTER ---
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

            // Format Timestamp
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
