package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;
import java.util.List;

public class GuestMenuBrowseActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu_browse); // Ensure this XML has a RecyclerView

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMenuGuest); // CHECK THIS ID IN XML
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        loadMenu();
    }

    private void loadMenu() {
        List<MenuItem> menuList = dbHelper.getAllMenuItems();
        // Pass 'false' for isStaff so it uses the Guest Layout (no delete buttons)
        adapter = new MenuAdapter(menuList, false, null);
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }
}
