package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.adapter.ReservationAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.Reservation;
import java.util.ArrayList;
import java.util.List;

public class StaffViewReservationsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> resList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_reservations);

        dbHelper = new DatabaseHelper(this);

        // Ensure your XML has a RecyclerView with this ID
        recyclerView = findViewById(R.id.recyclerReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReservations();
    }

    private void loadReservations() {
        resList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllReservations();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // COL_RES_ID
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                int guests = cursor.getInt(4);
                resList.add(new Reservation(id, name, date, time, guests));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Setup Adapter with Cancel Listener
        adapter = new ReservationAdapter(resList, reservation -> {
            showCancelDialog(reservation);
        });

        recyclerView.setAdapter(adapter);
    }

    // --- YOUR CUSTOM DIALOG LOGIC RESTORED ---
    private void showCancelDialog(Reservation res) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate YOUR custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView confirmMessage = dialogView.findViewById(R.id.confirmMessage);

        if (dialogTitle != null) dialogTitle.setText("Cancel Reservation?");
        if (confirmMessage != null) {
            confirmMessage.setText("Cancel " + res.getGuestName() + "'s booking?\nThis cannot be undone.");
        }

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        if (btnConfirm != null) {
            btnConfirm.setText("Cancel Booking");
            btnConfirm.setOnClickListener(v -> {
                // DELETE FROM DATABASE
                boolean success = dbHelper.deleteReservation(res.getId());
                if (success) {
                    Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                    loadReservations(); // Refresh list immediately
                } else {
                    Toast.makeText(this, "Error cancelling", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            });
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}
