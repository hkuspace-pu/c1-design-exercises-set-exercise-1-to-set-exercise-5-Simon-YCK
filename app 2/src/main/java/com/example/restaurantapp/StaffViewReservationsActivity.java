package com.example.restaurantapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffViewReservationsActivity extends AppCompatActivity {

    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_reservations);

        reservationsContainer = findViewById(R.id.reservationsContainer);

        // Load fake reservations
        loadReservations();
    }

    private void loadReservations() {
        // Sample data (name, date, time, guests)
        String[][] reservations = {
                {"David Chen", "Friday, Nov 29", "7:00 PM", "4"},
                {"Sarah Johnson", "Saturday, Nov 30", "6:30 PM", "2"},
                {"Mike Williams", "Sunday, Dec 1", "8:00 PM", "6"}
        };

        for (String[] res : reservations) {
            addReservationTicket(res[0], res[1], res[2], res[3]);
        }
    }

    private void addReservationTicket(String name, String date, String time, String guests) {
        View ticketView = LayoutInflater.from(this).inflate(R.layout.item_reservation_ticket_staff, reservationsContainer, false);

        TextView dateText = ticketView.findViewById(R.id.reservationDate);
        TextView timeText = ticketView.findViewById(R.id.reservationTime);
        TextView guestCountText = ticketView.findViewById(R.id.guestCount);
        TextView guestNameText = ticketView.findViewById(R.id.guestName);
        Button cancelButton = ticketView.findViewById(R.id.btnCancelReservation);

        dateText.setText(date);
        timeText.setText(time);
        guestCountText.setText(guests + " guests");
        guestNameText.setText(name);

        // Cancel button click
        cancelButton.setOnClickListener(v -> showCancelDialog(ticketView, name));

        reservationsContainer.addView(ticketView);
    }

    private void showCancelDialog(View ticketView, String guestName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView confirmMessage = dialogView.findViewById(R.id.confirmMessage);

        if (dialogTitle != null) {
            dialogTitle.setText("Cancel Reservation?");
        }
        confirmMessage.setText("Cancel " + guestName + "'s reservation? This action cannot be undone.");

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnConfirm.setText("Cancel Booking");

        btnConfirm.setOnClickListener(v -> {
            reservationsContainer.removeView(ticketView);
            Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
