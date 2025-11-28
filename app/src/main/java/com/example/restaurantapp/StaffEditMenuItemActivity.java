package com.example.restaurantapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffEditMenuItemActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText priceInput;
    private EditText descriptionInput;
    private TextView imagePreview;
    private View uploadImageButton; // Changed from Button to View
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_edit_menu_item);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        imagePreview = findViewById(R.id.imagePreview);
        uploadImageButton = findViewById(R.id.uploadImageButton); // View, not Button
        saveButton = findViewById(R.id.saveButton);

        // Get data from intent (if editing existing item)
        String itemName = getIntent().getStringExtra("itemName");
        String itemPrice = getIntent().getStringExtra("itemPrice");
        String itemDesc = getIntent().getStringExtra("itemDescription");

        // Pre-fill fields if editing
        if (itemName != null) {
            nameInput.setText(itemName);
            priceInput.setText(itemPrice);
            descriptionInput.setText(itemDesc);
        }

        // Upload image button (For now, just cycle through emojis)
        uploadImageButton.setOnClickListener(v -> {
            // Simple emoji picker
            String[] emojis = {"🍕", "🍔", "🍗", "🥗", "🍰", "☕", "🍝", "🌮", "🍜", "🥘"};
            String currentEmoji = imagePreview.getText().toString();
            int currentIndex = 0;

            for (int i = 0; i < emojis.length; i++) {
                if (emojis[i].equals(currentEmoji)) {
                    currentIndex = i;
                    break;
                }
            }

            int nextIndex = (currentIndex + 1) % emojis.length;
            imagePreview.setText(emojis[nextIndex]);
            Toast.makeText(this, "Image selected: " + emojis[nextIndex], Toast.LENGTH_SHORT).show();
        });

        // Save button
        saveButton.setOnClickListener(v -> saveMenuItem());
    }

    private void saveMenuItem() {
        String name = nameInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String emoji = imagePreview.getText().toString();

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in Name and Price", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Save to database or pass back to previous activity
        Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close this activity and go back
    }
}
