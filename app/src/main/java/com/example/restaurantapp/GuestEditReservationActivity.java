package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.utils.NotificationHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GuestEditReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput, specialRequestsInput;
    private Button btnSelectTime, saveButton, cancelBookingButton, btnBack;
    private TextView tvSelectedTime, guestsLabel;
    private SeekBar guestsSeekBar;
    private DatabaseHelper dbHelper;

    private int resId = -1;
    private String selectedDate = "";
    private int selectedHour = -1;
    private int selectedMinute = -1;
    private int guestCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_edit_reservation);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        specialRequestsInput = findViewById(R.id.specialRequestsInput);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        guestsSeekBar = findViewById(R.id.guestsSeekBar);
        guestsLabel = findViewById(R.id.guestsLabel);
        saveButton = findViewById(R.id.confirmButton);
        cancelBookingButton = findViewById(R.id.cancelBookingButton);
        btnBack = findViewById(R.id.btnBack);

        // ✅ Load existing reservation data
        loadReservationData();

        // Date Picker
        dateInput.setOnClickListener(v -> showDatePicker());

        // Time Picker
        btnSelectTime.setOnClickListener(v -> showTimePicker());

        // Guests SeekBar
        guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                guestCount = progress < 1 ? 1 : progress;
                guestsLabel.setText(guestCount + " " + (guestCount == 1 ? "Person" : "People"));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Save Button
        if (saveButton != null) {
            saveButton.setOnClickListener(v -> handleSave());
        }

        // Cancel Button
        if (cancelBookingButton != null) {
            cancelBookingButton.setOnClickListener(v -> showCancelDialog());
        }

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void loadReservationData() {
        if (getIntent().hasExtra("resId")) {
            resId = getIntent().getIntExtra("resId", -1);
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");
            int guests = getIntent().getIntExtra("guests", 2);
            String name = getIntent().getStringExtra("name");
            String specialReq = getIntent().getStringExtra("specialRequests");

            // Set data
            if (nameInput != null && name != null) nameInput.setText(name);
            if (dateInput != null && date != null) {
                selectedDate = date;
                dateInput.setText(date);
            }
            if (tvSelectedTime != null && time != null) {
                tvSelectedTime.setText(time);
                // Parse time to set selectedHour/selectedMinute
                parseTime(time);
            }
            if (guestsSeekBar != null) {
                guestsSeekBar.setProgress(guests);
                guestCount = guests;
            }
            if (specialRequestsInput != null && specialReq != null) {
                specialRequestsInput.setText(specialReq);
            }
        }
    }

    private void parseTime(String time) {
        // Parse "07:30 PM" format
        try {
            String[] parts = time.split(" ");
            String[] timeParts = parts[0].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            String amPm = parts[1];

            if (amPm.equals("PM") && hour != 12) hour += 12;
            if (amPm.equals("AM") && hour == 12) hour = 0;

            selectedHour = hour;
            selectedMinute = minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);
                    selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCal.getTime());
                    dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedCal.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = selectedHour != -1 ? selectedHour : currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedMinute != -1 ? selectedMinute : currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute1;

                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int displayHour = hourOfDay > 12 ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
                    String timeDisplay = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute1, amPm);
                    tvSelectedTime.setText(timeDisplay);
                },
                hour,
                minute,
                false
        );
        timePickerDialog.show();
    }

    private void handleSave() {
        String name = nameInput.getText().toString().trim();
        String specialReq = specialRequestsInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedHour == -1) {
            Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format time
        String amPm = selectedHour >= 12 ? "PM" : "AM";
        int displayHour = selectedHour > 12 ? selectedHour - 12 : (selectedHour == 0 ? 12 : selectedHour);
        String time = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, selectedMinute, amPm);

        boolean success = dbHelper.updateReservation(resId, selectedDate, time, guestCount);

        if (success) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.sendUpdateNotification(selectedDate, time);

            Toast.makeText(this, "Reservation Updated!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Reservation?")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean success = dbHelper.deleteReservation(resId);
                    if (success) {
                        NotificationHelper notificationHelper = new NotificationHelper(this);
                        notificationHelper.sendCancelNotification();

                        Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
