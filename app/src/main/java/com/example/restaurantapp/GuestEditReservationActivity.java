package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Calendar;

public class GuestEditReservationActivity extends AppCompatActivity {

    private EditText dateInput;
    private Spinner timeSpinner;
    private SeekBar guestCountSlider;
    private TextView guestCountLabel;
    private EditText specialRequestsInput;
    private Button saveButton;
    private EditText timeInput;
    private EditText guestCountInput;
    private Button cancelReservationButton;

    private int year, month, day, hour, minute;
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_edit_reservation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestCountSlider = findViewById(R.id.guestCountSlider);
        guestCountLabel = findViewById(R.id.guestCountLabel);
        specialRequestsInput = findViewById(R.id.specialRequestsInput);
        saveButton = findViewById(R.id.saveButton);
        cancelReservationButton = findViewById(R.id.cancelReservationButton);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Get data from intent
        String date = getIntent().getStringExtra("DATE");
        String time = getIntent().getStringExtra("TIME");
        String guests = getIntent().getStringExtra("GUESTS");

        // Pre-fill data
        if (date != null) dateInput.setText(date.replace("Date: ", ""));
        if (guests != null) {
            String guestNum = guests.replace("Guests: ", "");
            try {
                int count = Integer.parseInt(guestNum);
                guestCountSlider.setProgress(count - 1);
                guestCountLabel.setText("Number of Guests: " + count);
            } catch (NumberFormatException e) {
                guestCountSlider.setProgress(1);
                guestCountLabel.setText("Number of Guests: 2");
            }
        }

        setupTimeSpinner();

        // Pre-select time if available
        if (time != null) {
            selectedTime = time.replace("Time: ", "");
        }

        guestCountSlider.setMax(9);
        guestCountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int guestCount = progress + 1;
                guestCountLabel.setText("Number of Guests: " + guestCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        GuestEditReservationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                dateInput.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSave();
            }
        });

        // CANCEL RESERVATION BUTTON HANDLER
        cancelReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelConfirmationDialog();
            }
        });
    }

    private void setupTimeSpinner() {
        // Create time slots array (30-minute intervals from 5:00 PM to 10:00 PM)
        String[] timeSlots = {
                "Select time",
                "17:00 (5:00 PM)",
                "17:30 (5:30 PM)",
                "18:00 (6:00 PM)",
                "18:30 (6:30 PM)",
                "19:00 (7:00 PM)",
                "19:30 (7:30 PM)",
                "20:00 (8:00 PM)",
                "20:30 (8:30 PM)",
                "21:00 (9:00 PM)",
                "21:30 (9:30 PM)",
                "22:00 (10:00 PM)"
        };

        // Create ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                timeSlots
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to spinner
        timeSpinner.setAdapter(adapter);

        // Handle selection
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Not "Select time"
                    selectedTime = timeSlots[position];
                } else {
                    selectedTime = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTime = "";
            }
        });
    }

    private void showCancelConfirmationDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Cancel Reservation?")
                .setMessage("Are you sure you want to cancel this reservation? This action cannot be undone.")
                .setPositiveButton("Yes, Cancel", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        Toast.makeText(GuestEditReservationActivity.this,
                                "Reservation cancelled successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No, Keep It", null)
                .show();
    }

    private void handleSave() {
        String date = dateInput.getText().toString().trim();
        int guestCount = guestCountSlider.getProgress() + 1;

        if (date.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Reservation updated successfully!", Toast.LENGTH_LONG).show();
        finish();
        }
}
