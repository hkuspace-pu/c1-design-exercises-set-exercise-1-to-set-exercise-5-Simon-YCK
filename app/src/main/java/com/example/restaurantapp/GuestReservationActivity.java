package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.utils.NotificationHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GuestReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput, specialRequestsInput;
    private Button btnSelectTime, confirmButton, btnBack;
    private TextView tvSelectedTime, guestsLabel;
    private SeekBar guestsSeekBar;

    private String selectedDate = "";
    private int selectedHour = -1;
    private int selectedMinute = -1;
    private int guestCount = 2;

    // Restaurant opening hours (24-hour format)
    private static final int OPENING_HOUR = 11;  // 11:00 AM
    private static final int CLOSING_HOUR = 22;  // 10:00 PM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservation);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        specialRequestsInput = findViewById(R.id.specialRequestsInput);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);
        confirmButton = findViewById(R.id.confirmButton);
        btnBack = findViewById(R.id.btnBack);

        // Set default date to today
        Calendar today = Calendar.getInstance();
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today.getTime());
        dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(today.getTime()));

        // Date Picker
        dateInput.setOnClickListener(v -> showDatePicker());

        // Time Picker Button
        btnSelectTime.setOnClickListener(v -> showTimePicker());

        // Pre-fill guest name from login
        String loggedInUser = getIntent().getStringExtra("guestName");
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            nameInput.setText(loggedInUser);
        }

        // Guests SeekBar
        guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                guestCount = progress < 1 ? 1 : progress; // Minimum 1 guest
                guestsLabel.setText(guestCount + " " + (guestCount == 1 ? "Person" : "People"));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Confirm Button
        confirmButton.setOnClickListener(v -> handleSubmit());


        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Format selected date
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);

                    selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCal.getTime());
                    dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedCal.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Get current time as default
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Suggest next available slot (round to next 15 min)
        if (minute % 15 != 0) {
            minute = ((minute / 15) + 1) * 15;
            if (minute == 60) {
                hour++;
                minute = 0;
            }
        }

        // Create TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    // Validate against opening hours
                    if (hourOfDay < OPENING_HOUR) {
                        Toast.makeText(this, "We open at 11:00 AM", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (hourOfDay >= CLOSING_HOUR) {
                        Toast.makeText(this, "Last reservation is at 9:30 PM", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Save selection
                    selectedHour = hourOfDay;
                    selectedMinute = minute1;

                    // Display formatted time (12-hour format)
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int displayHour = hourOfDay > 12 ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);

                    String timeDisplay = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute1, amPm);
                    tvSelectedTime.setText(timeDisplay);
                    tvSelectedTime.setTextColor(getResources().getColor(android.R.color.black));
                },
                hour,
                minute,
                false  // 12-hour format
        );

        timePickerDialog.setTitle("Select Reservation Time");
        timePickerDialog.show();
    }

    private void handleSubmit() {
        String name = nameInput.getText().toString().trim();
        String specialReq = specialRequestsInput.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            return;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedHour == -1 || selectedMinute == -1) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        // FORMAT TIME from selectedHour and selectedMinute
        String amPm = selectedHour >= 12 ? "PM" : "AM";
        int displayHour = selectedHour > 12 ? selectedHour - 12 : (selectedHour == 0 ? 12 : selectedHour);
        String time = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, selectedMinute, amPm);

        // Save to database with special requests
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean success = dbHelper.addReservation(name, selectedDate, time, guestCount, specialReq);

        if (success) {
            // Send notification
            NotificationHelper notificationHelper = new NotificationHelper(this, name);
            notificationHelper.sendBookingNotification(name, selectedDate, time);

            Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();

            // Return to dashboard
            Intent intent = new Intent(this, GuestDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
