package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import java.util.Calendar;

public class GuestEditReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput; // Added nameInput just in case
    private Spinner timeSpinner;
    private SeekBar guestsSeekBar;
    private TextView guestsLabel;
    private Button saveButton, cancelBookingButton;
    private DatabaseHelper dbHelper;

    private int resId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_edit_reservation);

        dbHelper = new DatabaseHelper(this);

        // --- FIXING THE IDs ---
        // 1. Try finding 'confirmButton' first (common name in your XMLs)
        saveButton = findViewById(R.id.confirmButton);
        if (saveButton == null) {
            // 2. If null, try finding 'saveButton'
            saveButton = findViewById(R.id.saveButton);
        }

        // 3. Find Cancel Button
        cancelBookingButton = findViewById(R.id.cancelBookingButton);

        // Other Inputs
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);

        // Setup Spinner
        setupTimeSpinner();

        // Get Intent Data
        if (getIntent().hasExtra("resId")) {
            resId = getIntent().getIntExtra("resId", -1);
            if (dateInput != null) dateInput.setText(getIntent().getStringExtra("date"));
            if (guestsSeekBar != null) guestsSeekBar.setProgress(getIntent().getIntExtra("guests", 1));
        }

        // --- NULL SAFETY CHECK BEFORE LISTENER ---
        if (saveButton != null) {
            saveButton.setOnClickListener(v -> handleSave());
        } else {
            // DEBUG: Log error if button is missing
            System.err.println("ERROR: Confirm/Save Button NOT FOUND in layout!");
        }

        if (cancelBookingButton != null) {
            cancelBookingButton.setOnClickListener(v -> showCancelDialog());
        }

        // Date Picker Listener
        if (dateInput != null) {
            dateInput.setOnClickListener(v -> showDatePicker());
        }

        // Seekbar Listener
        if (guestsSeekBar != null) {
            guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (guestsLabel != null) guestsLabel.setText(Math.max(1, progress) + " People");
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
    }

    private void setupTimeSpinner() {
        if (timeSpinner == null) return;
        String[] times = {"12:00 PM", "1:00 PM", "6:00 PM", "7:00 PM", "8:00 PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                dateInput.setText(d + "/" + (m + 1) + "/" + y),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleSave() {
        if (dateInput == null || timeSpinner == null || guestsSeekBar == null) return;

        String newDate = dateInput.getText().toString().trim();
        String newTime = timeSpinner.getSelectedItem().toString();
        int newGuests = Math.max(1, guestsSeekBar.getProgress());

        boolean success = dbHelper.updateReservation(resId, newDate, newTime, newGuests);
        if (success) {
            Toast.makeText(this, "Reservation Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Ensure you have dialog_delete_confirm.xml or use a simple message
        builder.setTitle("Cancel Reservation");
        builder.setMessage("Are you sure you want to cancel this booking?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            boolean success = dbHelper.deleteReservation(resId);
            if (success) {
                Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
