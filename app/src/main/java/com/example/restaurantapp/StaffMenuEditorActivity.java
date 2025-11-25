package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StaffMenuEditorActivity extends AppCompatActivity {

    private LinearLayout menuItemsContainer;
    private FloatingActionButton fabAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Staff - Menu Editor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuItemsContainer = findViewById(R.id.menuItemsContainer);
        fabAddItem = findViewById(R.id.fabAddItem);

        loadSampleMenuItems();


        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMenuItemDialog();
            }
        });
    }

    private void loadSampleMenuItems() {
        addMenuItemCard("Margherita Pizza", "$12.99", "Classic cheese and tomato pizza");
        addMenuItemCard("Caesar Salad", "$8.99", "Fresh romaine lettuce with caesar dressing");
        addMenuItemCard("Grilled Salmon", "$18.99", "Atlantic salmon with seasonal vegetables");
        addMenuItemCard("Beef Burger", "$14.99", "Angus beef patty with fries");
        addMenuItemCard("Pasta Carbonara", "$13.99", "Creamy pasta with bacon");
    }

    private void addMenuItemCard(String name, String price, String description) {
        // Card container with proper spacing
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(24, 24, 24, 24);
        cardLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        cardLayout.setElevation(4);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24); // 24dp spacing between cards
        cardLayout.setLayoutParams(cardParams);

        // Item name (Large, bold, high contrast)
        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextSize(20);
        nameText.setTextColor(getResources().getColor(android.R.color.black));
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        nameParams.setMargins(0, 0, 0, 8);
        nameText.setLayoutParams(nameParams);

        // Item description
        TextView descText = new TextView(this);
        descText.setText(description);
        descText.setTextSize(16);
        descText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.setMargins(0, 0, 0, 8);
        descText.setLayoutParams(descParams);

        // Item price (Orange, bold)
        TextView priceText = new TextView(this);
        priceText.setText(price);
        priceText.setTextSize(18);
        priceText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        priceText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        priceParams.setMargins(0, 0, 0, 16);
        priceText.setLayoutParams(priceParams);

        // Button container with 48dp spacing
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(android.view.Gravity.END); // Align buttons to right
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayout.setLayoutParams(buttonLayoutParams);

        // Edit button with PENCIL ICON (no text)
        Button editButton = new Button(this);
        editButton.setText(""); // No text, icon only
        editButton.setBackground(null); // Remove background
        editButton.setCompoundDrawablesWithIntrinsicBounds(
                android.R.drawable.ic_menu_edit, 0, 0, 0
        );
        editButton.setContentDescription("Edit item");
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                convertDpToPx(48), // 48dp width
                convertDpToPx(48)  // 48dp height
        );
        editParams.setMargins(0, 0, 24, 0); // 48dp spacing
        editButton.setLayoutParams(editParams);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffMenuEditorActivity.this, StaffEditMenuItemActivity.class);
                intent.putExtra("ITEM_NAME", name);
                intent.putExtra("ITEM_PRICE", price);
                intent.putExtra("ITEM_DESCRIPTION", description);
                startActivity(intent);
            }
        });

        // Delete button with RECYCLE BIN ICON (no text)
        Button deleteButton = new Button(this);
        deleteButton.setText(""); // No text, icon only
        deleteButton.setBackground(null); // Remove background
        deleteButton.setCompoundDrawablesWithIntrinsicBounds(
                android.R.drawable.ic_menu_delete, 0, 0, 0
        );
        deleteButton.setContentDescription("Delete item");
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                convertDpToPx(48), // 48dp width
                convertDpToPx(48)  // 48dp height
        );
        deleteParams.setMargins(24, 0, 0, 0); // 48dp spacing
        deleteButton.setLayoutParams(deleteParams);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(name, cardLayout);
            }
        });

        buttonLayout.addView(editButton);
        buttonLayout.addView(deleteButton);

        cardLayout.addView(nameText);
        cardLayout.addView(descText);
        cardLayout.addView(priceText);
        cardLayout.addView(buttonLayout);

        menuItemsContainer.addView(cardLayout);

    }


    private void showDeleteConfirmationDialog(String itemName, LinearLayout cardLayout) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item?")
                .setMessage("Are you sure you want to delete this menu item? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        menuItemsContainer.removeView(cardLayout);
                        Toast.makeText(StaffMenuEditorActivity.this,
                                "Menu item deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void showAddMenuItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Menu Item");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Item Name");
        nameInput.setTextSize(16);
        nameInput.setPadding(16, 16, 16, 16);
        layout.addView(nameInput);

        final EditText priceInput = new EditText(this);
        priceInput.setHint("Price (e.g., 12.99)");
        priceInput.setTextSize(16);
        priceInput.setPadding(16, 16, 16, 16);
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(priceInput);

        final EditText descInput = new EditText(this);
        descInput.setHint("Description (optional)");
        descInput.setTextSize(16);
        descInput.setPadding(16, 16, 16, 16);
        layout.addView(descInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getText().toString().trim();
                String price = priceInput.getText().toString().trim();
                String desc = descInput.getText().toString().trim();

                if (!name.isEmpty() && !price.isEmpty()) {
                    addMenuItemCard(name, "$" + price, desc.isEmpty() ? "No description" : desc);
                    Toast.makeText(StaffMenuEditorActivity.this,
                            "Menu item added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StaffMenuEditorActivity.this,
                            "Please fill in name and price", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Helper method to convert dp to pixels
    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_notifications) {
            Toast.makeText(this, "Notifications: 3 new reservations", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            finish();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
