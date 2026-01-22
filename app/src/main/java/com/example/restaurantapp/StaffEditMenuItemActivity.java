package com.example.restaurantapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;

public class StaffEditMenuItemActivity extends AppCompatActivity {

    private EditText nameInput, priceInput, descriptionInput;
    private TextView imagePreview;
    private View uploadImageButton;
    private Button saveButton;

    private DatabaseHelper dbHelper;
    private int menuItemId = -1; // Default to -1 (New Item)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_edit_menu_item);

        dbHelper = new DatabaseHelper(this);

        // Initialize views (IDs from activity_staff_edit_menu_item.xml)
        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        imagePreview = findViewById(R.id.imagePreview);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        saveButton = findViewById(R.id.saveButton);

        // Check if we are Editing or Adding
        if (getIntent().hasExtra("menuId")) {
            menuItemId = getIntent().getIntExtra("menuId", -1);
            if (menuItemId != -1) {
                // We are editing! Pre-fill data.
                nameInput.setText(getIntent().getStringExtra("name"));
                descriptionInput.setText(getIntent().getStringExtra("description"));
                double price = getIntent().getDoubleExtra("price", 0.0);
                priceInput.setText(String.valueOf(price));
                saveButton.setText("Update Item");
            }
        }

        // Image Picker Logic (Cycling Emojis)
        uploadImageButton.setOnClickListener(v -> {
            String[] emojis = {"🍕", "🍔", "🍗", "🥗", "🍰", "☕", "🍝", "🌮", "🍜", "🥘"};
            String current = imagePreview.getText().toString();
            int nextIndex = 0;
            for (int i = 0; i < emojis.length; i++) {
                if (emojis[i].equals(current)) {
                    nextIndex = (i + 1) % emojis.length;
                    break;
                }
            }
            imagePreview.setText(emojis[nextIndex]);
        });

        saveButton.setOnClickListener(v -> saveMenuItem());
    }

    private void saveMenuItem() {
        String name = nameInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String desc = descriptionInput.getText().toString().trim();
        String emoji = imagePreview.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Name and Price are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        boolean success;
        if (menuItemId == -1) {
            // INSERT NEW
            success = dbHelper.addMenuItem(name, desc, price, "Main"); // 'Main' is default category
        } else {
            // UPDATE EXISTING (We need to add updateMenuItem to DatabaseHelper!)
            success = dbHelper.updateMenuItem(menuItemId, name, desc, price, "Main");
        }

        if (success) {
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Go back to list
        } else {
            Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
        }
    }
}
