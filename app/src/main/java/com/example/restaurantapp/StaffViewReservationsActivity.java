package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StaffViewReservationsActivity extends AppCompatActivity {

    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_reservations);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Reservations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reservationsContainer = findViewById(R.id.reservationsContainer);

        // FAKE DATA - Sample reservations
        loadSampleReservations();
    }

    private void loadSampleReservations() {
        addReservationCard("Guest: David Chen", "Date: 27/11/2025", "Time: 19:00", "Guests: 4", "Table: 5");
        addReservationCard("Guest: Sarah Lee", "Date: 28/11/2025", "Time: 18:30", "Guests: 2", "Table: 12");
        addReservationCard("Guest: John Smith", "Date: 29/11/2025", "Time: 20:00", "Guests: 6", "Table: 8");
        addReservationCard("Guest: Emma Wong", "Date: 30/11/2025", "Time: 19:30", "Guests: 3", "Table: 3");
    }

    private void addReservationCard(String guestName, String date, String time, String numGuests, String table) {
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(24, 24, 24, 24);
        cardLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        cardLayout.setElevation(4);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        cardLayout.setLayoutParams(cardParams);

        TextView guestText = new TextView(this);
        guestText.setText(guestName);
        guestText.setTextSize(20);
        guestText.setTextColor(getResources().getColor(android.R.color.black));
        guestText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams guestParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        guestParams.setMargins(0, 0, 0, 8);
        guestText.setLayoutParams(guestParams);

        TextView dateText = new TextView(this);
        dateText.setText(date);
        dateText.setTextSize(16);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        dateParams.setMargins(0, 0, 0, 4);
        dateText.setLayoutParams(dateParams);

        TextView timeText = new TextView(this);
        timeText.setText(time);
        timeText.setTextSize(16);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 0, 0, 4);
        timeText.setLayoutParams(timeParams);

        TextView guestsText = new TextView(this);
        guestsText.setText(numGuests);
        guestsText.setTextSize(16);
        LinearLayout.LayoutParams guestsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        guestsParams.setMargins(0, 0, 0, 4);
        guestsText.setLayoutParams(guestsParams);

        TextView tableText = new TextView(this);
        tableText.setText(table);
        tableText.setTextSize(16);
        tableText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        tableText.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        tableParams.setMargins(0, 0, 0, 16);
        tableText.setLayoutParams(tableParams);

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel Reservation");
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        cancelButton.setMinHeight(convertDpToPx(48));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelConfirmationDialog(guestName, cardLayout);
            }
        });

        cardLayout.addView(guestText);
        cardLayout.addView(dateText);
        cardLayout.addView(timeText);
        cardLayout.addView(guestsText);
        cardLayout.addView(tableText);
        cardLayout.addView(cancelButton);

        reservationsContainer.addView(cardLayout);
    }

    private void showCancelConfirmationDialog(String guestName, LinearLayout cardLayout) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Reservation")
                .setMessage("Are you sure you want to cancel the reservation for " + guestName + "?")
                .setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reservationsContainer.removeView(cardLayout);
                        Toast.makeText(StaffViewReservationsActivity.this,
                                "Reservation cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
