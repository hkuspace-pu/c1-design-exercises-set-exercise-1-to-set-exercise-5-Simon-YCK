package com.example.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Calendar;

public class GuestReservationActivity extends AppCompatActivity {

    private EditText dateInput;
    private EditText timeInput;
    private SeekBar guestCountSlider;
    private TextView guestCountLabel;
    private EditText specialRequestsInput;
    private Button submitButton;

    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        guestCountSlider = findViewById(R.id.guestCountSlider);
        guestCountLabel = findViewById(R.id.guestCountLabel);
        specialRequestsInput = findViewById(R.id.specialRequestsInput);
        submitButton = findViewById(R.id.submitButton);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

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

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        GuestReservationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                timeInput.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        int guestCount = guestCountSlider.getProgress() + 1;

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Reservation created for " + guestCount + " guests!", Toast.LENGTH_LONG).show();
        finish();
    }
}
