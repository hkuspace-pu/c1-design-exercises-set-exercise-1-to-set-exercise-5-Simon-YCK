package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class GuestMenuBrowseActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<MenuItem> menuItems;
    private LinearLayout tabContainer;
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu_browse);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.menuRecyclerView);
        tabContainer = findViewById(R.id.tabContainer);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        setupCategoryTabs();
        loadMenuItems(selectedCategory);
    }

    private void setupCategoryTabs() {
        tabContainer.removeAllViews();

        // Add "All" tab first
        addCategoryTab("All", true);

        // Get categories from database
        List<String> categories = dbHelper.getAllCategories();
        for (String category : categories) {
            addCategoryTab(category, false);
        }
    }

    private void addCategoryTab(String category, boolean isSelected) {
        View tabView = getLayoutInflater().inflate(R.layout.item_category_tab, tabContainer, false);
        TextView tabText = tabView.findViewById(R.id.tabText);
        tabText.setText(category);

        if (isSelected) {
            tabText.setBackgroundResource(R.drawable.bg_tab_selected);
            tabText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        }

        tabView.setOnClickListener(v -> {
            selectedCategory = category;
            setupCategoryTabs(); // Refresh tabs
            loadMenuItems(category);
        });

        tabContainer.addView(tabView);
    }

    private void loadMenuItems(String category) {
        if (category.equals("All")) {
            menuItems = dbHelper.getAllMenuItems(); // ✅ Already returns List<MenuItem>
        } else {
            menuItems = new ArrayList<>();
            Cursor cursor = dbHelper.getMenuByCategory(category);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String desc = cursor.getString(2);
                    double price = cursor.getDouble(3);
                    String cat = cursor.getString(4);
                    String imagePath = cursor.getString(5);

                    menuItems.add(new MenuItem(id, name, desc, price, cat, imagePath));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        adapter = new MenuAdapter(menuItems, false, null);
        recyclerView.setAdapter(adapter);
    }
}
