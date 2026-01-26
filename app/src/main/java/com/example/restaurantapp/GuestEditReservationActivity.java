package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.utils.ReservationFacade;
import java.util.Calendar;

public class GuestEditReservationActivity extends AppCompatActivity {

    private EditText dateInput;
    private Spinner timeSpinner;
    private SeekBar guestsSeekBar;
    private TextView guestsLabel;
    private Button saveButton, cancelBookingButton;
    private ReservationFacade reservationFacade;

    private int resId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_edit_reservation);

        // Initialize Facade
        reservationFacade = new ReservationFacade(this);

        // Initialize Views
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);

        saveButton = findViewById(R.id.confirmButton);
        if (saveButton == null) saveButton = findViewById(R.id.saveButton);
        cancelBookingButton = findViewById(R.id.cancelBookingButton);

        setupTimeSpinner();

        // Get Intent Data
        if (getIntent().hasExtra("resId")) {
            resId = getIntent().getIntExtra("resId", -1);
            String existingDate = getIntent().getStringExtra("date");
            int existingGuests = getIntent().getIntExtra("guests", 2);

            if (dateInput != null && existingDate != null) dateInput.setText(existingDate);
            if (guestsSeekBar != null) guestsSeekBar.setProgress(existingGuests);
            if (guestsLabel != null) guestsLabel.setText(existingGuests + " People");
        }

        // Date Picker
        if (dateInput != null) {
            dateInput.setOnClickListener(v -> showDatePicker());
        }

        // SeekBar
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

        // Save Button
        if (saveButton != null) saveButton.setOnClickListener(v -> handleSave());

        // Cancel Button
        if (cancelBookingButton != null) cancelBookingButton.setOnClickListener(v -> showCancelDialog());
    }

    private void setupTimeSpinner() {
        if (timeSpinner == null) return;
        String[] times = {"5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                dateInput.setText(day + "/" + (month + 1) + "/" + year),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleSave() {
        String newDate = (dateInput != null) ? dateInput.getText().toString().trim() : "";
        String newTime = (timeSpinner != null) ? timeSpinner.getSelectedItem().toString() : "";
        int newGuests = (guestsSeekBar != null) ? Math.max(1, guestsSeekBar.getProgress()) : 1;

        if (newDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use Facade (Handles DB + Notification)
        boolean success = reservationFacade.updateReservation(resId, newDate, newTime, newGuests);

        if (success) {
            Toast.makeText(this, "Reservation Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Reservation");
        builder.setMessage("Are you sure you want to cancel this booking?");

        builder.setPositiveButton("Yes, Cancel", (dialog, which) -> {
            // Use Facade (Handles DB + Notification)
            boolean success = reservationFacade.cancelReservation(resId);

            if (success) {
                Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error cancelling", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }
}
