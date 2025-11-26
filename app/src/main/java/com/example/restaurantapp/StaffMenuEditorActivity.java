package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
        View cardView = inflater.inflate(R.layout.item_menu_card_staff, menuItemsContainer, false);

        // Find views
        ImageView itemImage = cardView.findViewById(R.id.itemImage);
        TextView itemName = cardView.findViewById(R.id.itemName);
        TextView itemDescription = cardView.findViewById(R.id.itemDescription);
        TextView itemPrice = cardView.findViewById(R.id.itemPrice);
        LinearLayout editButton = cardView.findViewById(R.id.editButton);     // Changed to LinearLayout
        LinearLayout deleteButton = cardView.findViewById(R.id.deleteButton); // Changed to LinearLayout

        // Set data
        itemName.setText(name);
        itemDescription.setText(description);
        itemPrice.setText(price);
        itemImage.setImageResource(android.R.drawable.ic_menu_gallery);

        // Edit button
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

        // Delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(name, cardView);
            }
        });

        menuItemsContainer.addView(cardView);
    }

    private void showDeleteConfirmationDialog(String itemName, View cardView) {
        new AlertDialog.Builder(this)
                .setTitle("Delete '" + itemName + "'?")
                .setMessage("Are you sure you want to delete this menu item? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        menuItemsContainer.removeView(cardView);
                        Toast.makeText(StaffMenuEditorActivity.this,
                                itemName + " deleted", Toast.LENGTH_SHORT).show();
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
