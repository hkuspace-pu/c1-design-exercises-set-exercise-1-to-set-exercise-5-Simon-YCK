package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GuestMenuBrowseActivity extends AppCompatActivity {

    private LinearLayout menuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu_browse);

        menuContainer = findViewById(R.id.menuContainer);

        // Load sample menu items
        loadSampleMenuItems();
    }

    private void loadSampleMenuItems() {
        // Sample data (emoji, name, description, price)
        String[][] menuItems = {
                {"🍕", "Margherita Pizza", "Classic tomato sauce, mozzarella, and basil.", "$12.99"},
                {"🍔", "Cheeseburger", "Juicy beef patty with cheddar cheese.", "$15.00"},
                {"🥗", "Caesar Salad", "Fresh romaine lettuce with Caesar dressing.", "$8.99"},
                {"🍰", "Chocolate Cake", "Rich chocolate cake with ganache.", "$6.50"}
        };

        for (String[] item : menuItems) {
            addMenuItemCard(item[0], item[1], item[2], item[3]);
        }
    }

    private void addMenuItemCard(String emoji, String name, String description, String price) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_menu_card_guest, menuContainer, false);

        TextView itemImage = cardView.findViewById(R.id.menuItemImage);
        TextView itemName = cardView.findViewById(R.id.menuItemName);
        TextView itemDesc = cardView.findViewById(R.id.menuItemDescription);
        TextView itemPrice = cardView.findViewById(R.id.menuItemPrice);

        if (itemImage != null) itemImage.setText(emoji);
        if (itemName != null) itemName.setText(name);
        if (itemDesc != null) itemDesc.setText(description);
        if (itemPrice != null) itemPrice.setText(price);

        menuContainer.addView(cardView);
    }
}
