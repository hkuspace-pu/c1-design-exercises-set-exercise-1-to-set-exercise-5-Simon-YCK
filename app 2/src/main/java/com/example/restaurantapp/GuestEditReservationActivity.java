package com.example.restaurantapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class GuestEditReservationActivity extends AppCompatActivity {

    private EditText dateInput;
    private Spinner timeSpinner;
    private SeekBar guestCountSlider;
    private TextView guestCountLabel;
    private Button saveButton;
    private Button cancelBookingButton; // NEW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_edit_reservation);

        // Initialize views
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestCountSlider = findViewById(R.id.guestsSeekBar);
        guestCountLabel = findViewById(R.id.guestsLabel);
        saveButton = findViewById(R.id.confirmButton);
        cancelBookingButton = findViewById(R.id.cancelBookingButton); // NEW

        // Setup time spinner
        String[] timeSlots = {"5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        // Setup guest count slider
        guestCountSlider.setMax(10);
        guestCountSlider.setProgress(2);
        guestCountLabel.setText("2 People");

        guestCountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int guests = Math.max(1, progress);
                guestCountLabel.setText(guests + " People");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Get existing reservation data from intent
        String existingDate = getIntent().getStringExtra("date");
        String existingTime = getIntent().getStringExtra("time");
        String existingGuests = getIntent().getStringExtra("guests");

        if (existingDate != null) dateInput.setText(existingDate);
        if (existingTime != null) {
            int position = adapter.getPosition(existingTime);
            if (position >= 0) timeSpinner.setSelection(position);
        }
        if (existingGuests != null) {
            try {
                int guests = Integer.parseInt(existingGuests);
                guestCountSlider.setProgress(guests);
            } catch (NumberFormatException e) {}
        }

        // Save button
        saveButton.setOnClickListener(v -> handleSave());

        // Cancel Booking button (NEW)
        cancelBookingButton.setOnClickListener(v -> showCancelDialog());
    }

    private void handleSave() {
        String date = dateInput.getText().toString().trim();
        String time = timeSpinner.getSelectedItem().toString();
        int guests = Math.max(1, guestCountSlider.getProgress());

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Update reservation in database
        Toast.makeText(this, "Reservation updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView confirmMessage = dialogView.findViewById(R.id.confirmMessage);
        confirmMessage.setText("Cancel this reservation?\nThis cannot be undone.");

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnConfirm.setText("Cancel Booking"); // Change button text

        btnConfirm.setOnClickListener(v -> {
            // TODO: Delete from database
            Toast.makeText(this, "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish(); // Close edit screen and go back
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
