package com.example.restaurantapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.adapters.MenuAdapter;
import java.util.ArrayList;
import java.util.List;

public class GuestMenuBrowseActivity extends AppCompatActivity {

    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu_browse);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Browse Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize RecyclerView
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample menu data (hardcoded for UI demonstration)
        menuItems = createSampleMenuData();

        // Set up adapter
        menuAdapter = new MenuAdapter(this, menuItems);
        menuRecyclerView.setAdapter(menuAdapter);
    }

    private List<MenuItem> createSampleMenuData() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Margherita Pizza", "$12.99", "Classic cheese and tomato pizza", R.drawable.ic_launcher_background));
        items.add(new MenuItem("Caesar Salad", "$8.99", "Fresh romaine lettuce with caesar dressing", R.drawable.ic_launcher_background));
        items.add(new MenuItem("Grilled Salmon", "$18.99", "Atlantic salmon with seasonal vegetables", R.drawable.ic_launcher_background));
        items.add(new MenuItem("Beef Burger", "$14.99", "Angus beef patty with fries", R.drawable.ic_launcher_background));
        items.add(new MenuItem("Pasta Carbonara", "$13.99", "Creamy pasta with bacon", R.drawable.ic_launcher_background));
        items.add(new MenuItem("Chocolate Cake", "$6.99", "Rich chocolate dessert", R.drawable.ic_launcher_background));
        return items;
    }

    // Inner class for Menu Item data model
    public static class MenuItem {
        private String name;
        private String price;
        private String description;
        private int imageResource;

        public MenuItem(String name, String price, String description, int imageResource) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.imageResource = imageResource;
        }

        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getDescription() { return description; }
        public int getImageResource() { return imageResource; }
    }
}
