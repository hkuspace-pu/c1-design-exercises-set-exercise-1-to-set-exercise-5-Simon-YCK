package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GuestViewReservationsActivity extends AppCompatActivity {

    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_view_reservations);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Reservations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reservationsContainer = findViewById(R.id.reservationsContainer);

        loadSampleReservations();

    }

    private void loadSampleReservations() {
        addReservationCard("Date: 27/11/2025", "Time: 19:00", "Guests: 4", "Table: 5");
        addReservationCard("Date: 30/11/2025", "Time: 18:30", "Guests: 2", "Table: 12");
    }

    private void addReservationCard(String date, String time, String numGuests, String table) {
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

        TextView dateText = new TextView(this);
        dateText.setText(date);
        dateText.setTextSize(18);
        dateText.setTextColor(getResources().getColor(android.R.color.black));
        dateText.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView timeText = new TextView(this);
        timeText.setText(time);
        timeText.setTextSize(16);

        TextView guestsText = new TextView(this);
        guestsText.setText(numGuests);
        guestsText.setTextSize(16);

        TextView tableText = new TextView(this);
        tableText.setText(table);
        tableText.setTextSize(16);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.setMargins(0, 16, 0, 0);
        buttonLayout.setLayoutParams(buttonLayoutParams);

        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setTextColor(getResources().getColor(android.R.color.white));
        editButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0,
                convertDpToPx(48),
                1.0f
        );
        editParams.setMargins(0, 0, 12, 0);
        editButton.setLayoutParams(editParams);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestViewReservationsActivity.this, GuestEditReservationActivity.class);
                intent.putExtra("DATE", date);
                intent.putExtra("TIME", time);
                intent.putExtra("GUESTS", numGuests);
                startActivity(intent);
            }
        });

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        cancelButton.setTextColor(getResources().getColor(android.R.color.white));
        cancelButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                0,
                convertDpToPx(48),
                1.0f
        );
        cancelParams.setMargins(12, 0, 0, 0);
        cancelButton.setLayoutParams(cancelParams);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationsContainer.removeView(cardLayout);
                Toast.makeText(GuestViewReservationsActivity.this,
                        "Reservation cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLayout.addView(editButton);
        buttonLayout.addView(cancelButton);

        cardLayout.addView(dateText);
        cardLayout.addView(timeText);
        cardLayout.addView(guestsText);
        cardLayout.addView(tableText);
        cardLayout.addView(buttonLayout);

        reservationsContainer.addView(cardLayout);
    }

    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
