package com.example.restaurantapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StaffEditMenuItemActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText priceInput;
    private EditText descriptionInput;
    private ImageView imagePreview;
    private Button uploadImageButton;
    private Button saveButton;
    private Uri selectedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_edit_menu_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Menu Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        imagePreview = findViewById(R.id.imagePreview);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        saveButton = findViewById(R.id.saveButton);

        // Get data from intent
        String itemName = getIntent().getStringExtra("ITEM_NAME");
        String itemPrice = getIntent().getStringExtra("ITEM_PRICE");
        String itemDescription = getIntent().getStringExtra("ITEM_DESCRIPTION");

        if (itemName != null) nameInput.setText(itemName);
        if (itemPrice != null) priceInput.setText(itemPrice.replace("$", ""));
        if (itemDescription != null) descriptionInput.setText(itemDescription);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSave();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
            imagePreview.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSave() {
        String name = nameInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Menu item updated successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
}
