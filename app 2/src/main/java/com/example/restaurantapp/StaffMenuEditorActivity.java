package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffMenuEditorActivity extends AppCompatActivity {

    private LinearLayout menuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_editor);

        menuContainer = findViewById(R.id.menuContainer);

        // Load sample menu items
        loadSampleMenuItems();
    }

    private void loadSampleMenuItems() {
        // Sample data (emoji, name, description, price)
        String[][] menuItems = {
                {"🍕", "Margherita Pizza", "Classic tomato sauce, mozzarella, and basil.", "$12.99"},
                {"🍔", "Cheeseburger", "Juicy beef patty with cheddar cheese.", "$15.00"},
                {"🥗", "Caesar Salad", "Fresh romaine lettuce with Caesar dressing.", "$8.99"}
        };

        for (String[] item : menuItems) {
            addMenuItemCard(item[1], item[2], item[3], item[0]);
        }
    }

    private void addMenuItemCard(String name, String description, String price, String emoji) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_menu_card_staff, menuContainer, false);

        TextView itemImage = cardView.findViewById(R.id.menuItemImage);
        TextView itemName = cardView.findViewById(R.id.menuItemName);
        TextView itemDesc = cardView.findViewById(R.id.menuItemDescription);
        TextView itemPrice = cardView.findViewById(R.id.menuItemPrice);
        Button editButton = cardView.findViewById(R.id.btnEdit);
        Button deleteButton = cardView.findViewById(R.id.btnDelete);

        if (itemImage != null) itemImage.setText(emoji);
        if (itemName != null) itemName.setText(name);
        if (itemDesc != null) itemDesc.setText(description);
        if (itemPrice != null) itemPrice.setText(price);

        // Edit button
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffEditMenuItemActivity.class);
            intent.putExtra("itemName", name);
            intent.putExtra("itemPrice", price.replace("$", ""));
            intent.putExtra("itemDescription", description);
            startActivity(intent);
        });

        // Delete button
        deleteButton.setOnClickListener(v -> {
            showDeleteDialog(cardView, name);
        });

        menuContainer.addView(cardView);
    }

    private void showDeleteDialog(View cardView, String itemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Update dialog title
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        if (dialogTitle != null) {
            dialogTitle.setText("Delete '" + itemName + "'?");
        }

        // Update confirmation message
        TextView confirmMessage = dialogView.findViewById(R.id.confirmMessage);
        confirmMessage.setText("Are you sure you want to delete this menu item? This action cannot be undone.");

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(v -> {
            menuContainer.removeView(cardView);
            Toast.makeText(this, itemName + " deleted successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
