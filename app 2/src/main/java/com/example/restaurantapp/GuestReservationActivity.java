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

public class GuestReservationActivity extends AppCompatActivity {

    private EditText dateInput;
    private Spinner timeSpinner;
    private SeekBar guestCountSlider;
    private TextView guestCountLabel;
    private EditText specialRequestsInput;
    private Button submitButton;

    private int year, month, day;
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservation);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Make Reservation");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateInput = findViewById(R.id.dateInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestCountSlider = findViewById(R.id.guestsSeekBar);
        guestCountLabel = findViewById(R.id.guestsLabel);
        specialRequestsInput = findViewById(R.id.specialRequestsInput);
        submitButton = findViewById(R.id.confirmButton);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Setup TIME DROPDOWN MENU (Spinner)
        setupTimeSpinner();

        // Setup SeekBar (Slider from 1 to 10)
        guestCountSlider.setMax(9); // 0-9, representing 1-10 people
        guestCountSlider.setProgress(1); // Default to 2 people
        guestCountLabel.setText("Number of Guests: 2");

        guestCountSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int guestCount = progress + 1; // Convert 0-9 to 1-10
                guestCountLabel.setText("Number of Guests: " + guestCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Date picker
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        GuestReservationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                dateInput.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void setupTimeSpinner() {
        // Create time slots array TEMP(30-minute intervals from 5:00 PM to 10:00 PM)
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

    private void handleSubmit() {
        String date = dateInput.getText().toString().trim();
        int guestCount = guestCountSlider.getProgress() + 1;

        if (date.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Reservation created for " + guestCount + " guests at " + selectedTime,
                Toast.LENGTH_LONG).show();
        finish();
    }
}
