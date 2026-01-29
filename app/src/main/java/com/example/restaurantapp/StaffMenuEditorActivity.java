package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;

import java.util.List;

public class StaffMenuEditorActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private MenuAdapter adapter;
    private List<MenuItem> menuList;
    private RecyclerView recyclerView;
    private Button btnAddNew;
    private View btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_editor);

        // 1. Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // 2. Initialize Views
        recyclerView = findViewById(R.id.recyclerViewMenu);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnBack = findViewById(R.id.btnBack);

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 3. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 4. Setup "Add New" Button Click
        btnAddNew.setOnClickListener(v -> {
            // Launch Edit Activity in "Add Mode" (Pass ID = -1 to indicate new item)
            Intent intent = new Intent(StaffMenuEditorActivity.this, StaffEditMenuItemActivity.class);
            intent.putExtra("menuId", -1);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data every time we return to this screen (e.g. after adding/editing)
        loadMenuData();
    }

    private void loadMenuData() {
        // Fetch latest data from SQLite
        menuList = dbHelper.getAllMenuItems();

        // Create Adapter
        adapter = new MenuAdapter(menuList, true, new MenuAdapter.OnItemActionListener() {
            @Override
            public void onDeleteClick(int position) {
                confirmDelete(position);
            }

            @Override
            public void onEditClick(int position) {
                // Get the item clicked
                MenuItem item = menuList.get(position);

                // Open Edit Screen with this item's data
                Intent intent = new Intent(StaffMenuEditorActivity.this, StaffEditMenuItemActivity.class);
                intent.putExtra("menuId", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("price", item.getPrice());
                startActivity(intent);
            }
        });

        // Attach Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
    }

    private void confirmDelete(int position) {
        MenuItem item = menuList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // REAL DELETE LOGIC
                    boolean success = dbHelper.deleteMenuItem(item.getId());
                    if (success) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        loadMenuData(); // Refresh the list
                    } else {
                        Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
