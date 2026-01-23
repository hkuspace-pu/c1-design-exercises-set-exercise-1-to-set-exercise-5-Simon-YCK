package com.example.restaurantapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;

public class GuestReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput;
    private Spinner timeSpinner;
    private SeekBar guestsSeekBar;
    private TextView guestsLabel;
    private Button confirmButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservation);

        dbHelper = new DatabaseHelper(this);

        // IDs must match your XML (activity_guest_reservation.xml)
        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestsSeekBar = findViewById(R.id.guestsSeekBar); // Or guestCountSlider if you named it that
        guestsLabel = findViewById(R.id.guestsLabel);
        confirmButton = findViewById(R.id.confirmButton);

        // Update Guest Count Label
        guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int count = Math.max(1, progress);
                guestsLabel.setText(count + " People");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        confirmButton.setOnClickListener(v -> handleSubmit());
    }

    private void handleSubmit() {
        String name = nameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeSpinner.getSelectedItem().toString();
        // Your logic: slider progress + 1 (if slider starts at 0) or simple max(1, val)
        int guestCount = Math.max(1, guestsSeekBar.getProgress());

        // --- YOUR VALIDATION LOGIC RESTORED ---
        if (name.isEmpty()) {
            nameInput.setError("Name is required"); // Visual error on field
            return;
        }
        if (date.isEmpty()) {
            dateInput.setError("Date is required");
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- DATABASE SAVE ---
        boolean success = dbHelper.addReservation(name, date, time, guestCount);

        if (success) {
            Toast.makeText(this, "Reservation created for " + guestCount + " guests at " + time,
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving reservation. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
