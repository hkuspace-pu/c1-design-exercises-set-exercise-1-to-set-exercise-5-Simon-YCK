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
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import java.util.Calendar;

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

        // Initialize Views
        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);
        confirmButton = findViewById(R.id.confirmButton);

        // 1. SETUP DATE PICKER (Fixes "date calendar not shown")
        dateInput.setOnClickListener(v -> showDatePicker());

        // 2. SETUP TIME SPINNER (Fixes "box cannot fill in")
        setupTimeSpinner();

        // 3. SETUP SEEKBAR
        guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int count = Math.max(1, progress); // Min 1 guest
                guestsLabel.setText(count + " People");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 4. CONFIRM BUTTON
        confirmButton.setOnClickListener(v -> handleSubmit());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Format: DD/MM/YYYY
                    String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    dateInput.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setupTimeSpinner() {
        // Create a list of available times
        String[] times = new String[]{
                "12:00 PM", "1:00 PM", "2:00 PM",
                "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
    }

    private void handleSubmit() {
        String name = nameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeSpinner.getSelectedItem() != null ?
                timeSpinner.getSelectedItem().toString() : "";
        int guestCount = Math.max(1, guestsSeekBar.getProgress());

        // --- VALIDATION (Fixes "missing required alert") ---
        boolean isValid = true;

        if (name.isEmpty()) {
            nameInput.setError("Name is required*");
            isValid = false;
        }
        if (date.isEmpty()) {
            dateInput.setError("Date is required*");
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValid) return;

        // Save to Database
        boolean success = dbHelper.addReservation(name, date, time, guestCount);

        if (success) {
            Toast.makeText(this, "Booking Confirmed! See you on " + date, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Booking Failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
