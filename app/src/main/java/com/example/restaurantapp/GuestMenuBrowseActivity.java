package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GuestMenuBrowseActivity extends AppCompatActivity {

    private LinearLayout menuItemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu_browse);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Browse Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuItemsContainer = findViewById(R.id.menuItemsContainer);

        loadSampleMenuItems();
    }

    private void loadSampleMenuItems() {
        addMenuItemCard("Margherita Pizza", "$12.99", "Classic cheese and tomato pizza with fresh basil");
        addMenuItemCard("Caesar Salad", "$8.99", "Fresh romaine lettuce with caesar dressing");
        addMenuItemCard("Grilled Salmon", "$18.99", "Atlantic salmon with seasonal vegetables");
        addMenuItemCard("Beef Burger", "$14.99", "Angus beef patty with fries and coleslaw");
        addMenuItemCard("Pasta Carbonara", "$13.99", "Creamy pasta with bacon and parmesan");
        addMenuItemCard("Chocolate Cake", "$6.99", "Rich chocolate dessert with vanilla ice cream");
    }

    private void addMenuItemCard(String name, String price, String description) {
        // INFLATE XML LAYOUT
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.item_menu_card_guest, menuItemsContainer, false);

        // Find views
        ImageView itemImage = cardView.findViewById(R.id.itemImage);
        TextView itemName = cardView.findViewById(R.id.itemName);
        TextView itemDescription = cardView.findViewById(R.id.itemDescription);
        TextView itemPrice = cardView.findViewById(R.id.itemPrice);

        // Set data
        itemName.setText(name);
        itemDescription.setText(description);
        itemPrice.setText(price);
        itemImage.setImageResource(android.R.drawable.ic_menu_gallery);

        // Click listener for guest to see item details
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GuestMenuBrowseActivity.this,
                        "Selected: " + name, Toast.LENGTH_SHORT).show();
            }
        });

        menuItemsContainer.addView(cardView);
    }
}
