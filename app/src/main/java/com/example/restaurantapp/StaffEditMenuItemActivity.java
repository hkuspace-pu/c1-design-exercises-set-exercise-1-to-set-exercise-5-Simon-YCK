package com.example.restaurantapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StaffEditMenuItemActivity extends AppCompatActivity {

    private EditText nameInput, priceInput, descriptionInput;
    private ImageView imagePreview;
    private View uploadImageButton;
    private Button saveButton;
    private Spinner categorySpinner;

    private DatabaseHelper dbHelper;
    private int menuItemId = -1;
    private String selectedImagePath = null;

    // Image Picker Launcher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imagePreview.setImageBitmap(bitmap);
                        selectedImagePath = saveImageToInternalStorage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_edit_menu_item);

        dbHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        imagePreview = findViewById(R.id.imagePreview);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        saveButton = findViewById(R.id.saveButton);
        categorySpinner = findViewById(R.id.categorySpinner);

        // Setup Category Spinner
        String[] categories = {"Appetizers", "Main Course", "Desserts", "Beverages", "Specials"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Check if editing existing item
        if (getIntent().hasExtra("menuId")) {
            menuItemId = getIntent().getIntExtra("menuId", -1);
            if (menuItemId != -1) {
                nameInput.setText(getIntent().getStringExtra("name"));
                descriptionInput.setText(getIntent().getStringExtra("description"));
                double price = getIntent().getDoubleExtra("price", 0.0);
                priceInput.setText(String.valueOf(price));

                String category = getIntent().getStringExtra("category");
                int spinnerPosition = adapter.getPosition(category);
                categorySpinner.setSelection(spinnerPosition);

                selectedImagePath = getIntent().getStringExtra("imagePath");
                if (selectedImagePath != null) {
                    loadImageFromPath(selectedImagePath);
                }

                saveButton.setText("Update Item");
            }
        }

        // Image Upload Button
        uploadImageButton.setOnClickListener(v -> openImagePicker());

        saveButton.setOnClickListener(v -> saveMenuItem());
    }

    // Open Gallery to pick image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    // Save image to internal storage
    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getFilesDir(); // Internal storage
        String filename = "menu_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Load image from saved path
    private void loadImageFromPath(String path) {
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
                imagePreview.setImageBitmap(bitmap);
            }
        }
    }

    private void saveMenuItem() {
        String name = nameInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String desc = descriptionInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Name and Price are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        boolean success;
        if (menuItemId == -1) {
            // INSERT NEW
            success = dbHelper.addMenuItem(name, desc, price, category, selectedImagePath);
        } else {
            // UPDATE EXISTING
            success = dbHelper.updateMenuItem(menuItemId, name, desc, price, category, selectedImagePath);
        }

        if (success) {
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
        }
    }
}
