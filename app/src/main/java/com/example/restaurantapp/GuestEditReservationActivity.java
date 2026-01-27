package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private static final String TAG = "GuestEditReservation";

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

        Log.d(TAG, "onCreate started");

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

        // ✅ CHECK which views are null
        Log.d(TAG, "nameInput: " + (nameInput != null));
        Log.d(TAG, "dateInput: " + (dateInput != null));
        Log.d(TAG, "specialRequestsInput: " + (specialRequestsInput != null));
        Log.d(TAG, "btnSelectTime: " + (btnSelectTime != null));
        Log.d(TAG, "tvSelectedTime: " + (tvSelectedTime != null));
        Log.d(TAG, "guestsSeekBar: " + (guestsSeekBar != null));
        Log.d(TAG, "guestsLabel: " + (guestsLabel != null));
        Log.d(TAG, "saveButton: " + (saveButton != null));
        Log.d(TAG, "cancelBookingButton: " + (cancelBookingButton != null));
        Log.d(TAG, "btnBack: " + (btnBack != null));

        // Load existing reservation data
        loadReservationData();

        // Date Picker
        if (dateInput != null) {
            dateInput.setOnClickListener(v -> showDatePicker());
        }

        // Time Picker
        if (btnSelectTime != null) {
            btnSelectTime.setOnClickListener(v -> showTimePicker());
        }

        // Guests SeekBar
        if (guestsSeekBar != null) {
            guestsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    guestCount = progress < 1 ? 1 : progress;
                    if (guestsLabel != null) {
                        guestsLabel.setText(guestCount + " " + (guestCount == 1 ? "Person" : "People"));
                    }
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        // Save Button
        if (saveButton != null) {
            Log.d(TAG, "Setting up Save button listener");
            saveButton.setOnClickListener(v -> {
                Log.d(TAG, "Save button clicked");
                handleSave();
            });
        } else {
            Log.e(TAG, "saveButton (confirmButton) is NULL! Check XML ID");
        }

        // ✅ Cancel Button with extensive logging
        if (cancelBookingButton != null) {
            Log.d(TAG, "Setting up Cancel button listener");
            cancelBookingButton.setOnClickListener(v -> {
                Log.d(TAG, "Cancel button CLICKED!");
                showCancelDialog();
            });

            // Also check if button is clickable
            Log.d(TAG, "cancelBookingButton clickable: " + cancelBookingButton.isClickable());
            Log.d(TAG, "cancelBookingButton enabled: " + cancelBookingButton.isEnabled());
            Log.d(TAG, "cancelBookingButton visibility: " + cancelBookingButton.getVisibility());
        } else {
            Log.e(TAG, "❌ cancelBookingButton is NULL! Check XML ID: 'cancelBookingButton'");
        }

        // Back Button
        if (btnBack != null) {
            Log.d(TAG, "Setting up Back button listener");
            btnBack.setOnClickListener(v -> {
                Log.d(TAG, "Back button clicked");
                finish();
            });
        } else {
            Log.e(TAG, "btnBack is NULL");
        }

        Log.d(TAG, "onCreate finished");
    }

    private void loadReservationData() {
        Log.d(TAG, "loadReservationData started");

        if (getIntent().hasExtra("resId")) {
            resId = getIntent().getIntExtra("resId", -1);
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");
            int guests = getIntent().getIntExtra("guests", 2);
            String name = getIntent().getStringExtra("name");
            String specialReq = getIntent().getStringExtra("specialRequests");

            Log.d(TAG, "Loaded reservation - ID: " + resId + ", Name: " + name + ", Date: " + date);

            // Set data
            if (nameInput != null && name != null) nameInput.setText(name);
            if (dateInput != null && date != null) {
                selectedDate = date;
                dateInput.setText(date);
            }
            if (tvSelectedTime != null && time != null) {
                tvSelectedTime.setText(time);
                parseTime(time);
            }
            if (guestsSeekBar != null) {
                guestsSeekBar.setProgress(guests);
                guestCount = guests;
            }
            if (guestsLabel != null) {
                guestsLabel.setText(guests + " " + (guests == 1 ? "Person" : "People"));
            }
            if (specialRequestsInput != null && specialReq != null && !specialReq.isEmpty()) {
                specialRequestsInput.setText(specialReq);
                Log.d(TAG, "Special requests loaded: " + specialReq);
            }
        } else {
            Log.e(TAG, "No resId found in Intent!");
        }
    }

    private void parseTime(String time) {
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
            Log.d(TAG, "Parsed time: " + selectedHour + ":" + selectedMinute);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing time: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        Log.d(TAG, "showDatePicker called");
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);
                    selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCal.getTime());
                    dateInput.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedCal.getTime()));
                    Log.d(TAG, "Date selected: " + selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Log.d(TAG, "showTimePicker called");
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
                    Log.d(TAG, "Time selected: " + timeDisplay);
                },
                hour,
                minute,
                false
        );
        timePickerDialog.show();
    }

    private void handleSave() {
        Log.d(TAG, "handleSave called");

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

        Log.d(TAG, "Updating reservation - ID: " + resId + ", Date: " + selectedDate + ", Time: " + time);

        boolean success = dbHelper.updateReservation(resId, selectedDate, time, guestCount, specialReq);

        if (success) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.sendUpdateNotification(selectedDate, time);

            Toast.makeText(this, "Reservation Updated!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Reservation updated successfully");
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Failed to update reservation");
        }
    }

    private void showCancelDialog() {
        Log.d(TAG, "showCancelDialog called");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView confirmMessage = dialogView.findViewById(R.id.confirmMessage);

        if (dialogTitle != null) {
            dialogTitle.setText("Cancel Reservation?");
        }
        if (confirmMessage != null) {
            String name = nameInput != null ? nameInput.getText().toString().trim() : "";
            if (name.isEmpty()) name = "this";
            confirmMessage.setText("Cancel " + name + "'s booking?\nThis cannot be undone.");
        }

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        Log.d(TAG, "Dialog buttons - Confirm: " + (btnConfirm != null) + ", Cancel: " + (btnCancel != null));

        if (btnConfirm != null) {
            btnConfirm.setText("Cancel Booking");
            btnConfirm.setOnClickListener(v -> {
                Log.d(TAG, "Confirm cancel clicked - deleting reservation ID: " + resId);
                boolean success = dbHelper.deleteReservation(resId);
                if (success) {
                    NotificationHelper notificationHelper = new NotificationHelper(this);
                    notificationHelper.sendCancelNotification();

                    Toast.makeText(this, "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Reservation deleted successfully");
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(this, "Error cancelling reservation", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to delete reservation");
                }
            });
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {
                Log.d(TAG, "Cancel dialog dismissed");
                dialog.dismiss();
            });
        }

        dialog.show();
        Log.d(TAG, "Dialog shown");
    }
}
