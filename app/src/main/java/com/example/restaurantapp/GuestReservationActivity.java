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
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.utils.ReservationFacade;
import java.util.Calendar;

public class GuestReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput;
    private Spinner timeSpinner;
    private SeekBar guestsSeekBar;
    private TextView guestsLabel;
    private Button confirmButton;
    private ReservationFacade reservationFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservation);

        // Initialize Facade (Handles DB + Notifications)
        reservationFacade = new ReservationFacade(this);

        // Find Views
        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);
        confirmButton = findViewById(R.id.confirmButton);

        // Setup Time Spinner
        String[] timeSlots = {"5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        // Setup Seekbar
        if (guestsSeekBar != null) {
            guestsSeekBar.setMax(10);
            guestsSeekBar.setProgress(2);
            guestsLabel.setText("2 People");

            guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int guests = Math.max(1, progress);
                    guestsLabel.setText(guests + " People");
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        // Date Picker
        if (dateInput != null) {
            dateInput.setOnClickListener(v -> showDatePicker());
        }

        // Confirm Button
        if (confirmButton != null) {
            confirmButton.setOnClickListener(v -> handleSubmit());
        }
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                dateInput.setText(day + "/" + (month + 1) + "/" + year),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void handleSubmit() {
        String name = nameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeSpinner.getSelectedItem().toString();
        int guests = Math.max(1, guestsSeekBar.getProgress());

        // Validation
        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use Facade (Handles DB + Notification automatically)
        boolean success = reservationFacade.createReservation(name, date, time, guests);

        if (success) {
            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            finish(); // Close and return to dashboard
        } else {
            Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
