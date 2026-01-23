package com.example.restaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Or TextView if your cancel button is a text
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Reservation;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ResViewHolder> {

    private List<Reservation> list;
    private OnCancelListener listener;

    public interface OnCancelListener {
        void onCancelClick(Reservation res);
    }

    public ReservationAdapter(List<Reservation> list, OnCancelListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use YOUR XML: item_reservation_ticket_staff.xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation_ticket_staff, parent, false);
        return new ResViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResViewHolder holder, int position) {
        Reservation res = list.get(position);

        // Map data to your XML IDs (Check your item_reservation_ticket_staff.xml)
        if(holder.guestName != null) holder.guestName.setText(res.getGuestName());
        if(holder.date != null) holder.date.setText(res.getDate());
        if(holder.time != null) holder.time.setText(res.getTime());
        if(holder.guests != null) holder.guests.setText(res.getGuestCount() + " Guests");

        // Handle Cancel Click
        holder.btnCancel.setOnClickListener(v -> listener.onCancelClick(res));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ResViewHolder extends RecyclerView.ViewHolder {
        TextView guestName, date, time, guests;
        View btnCancel; // Can be Button or ImageButton

        public ResViewHolder(@NonNull View itemView) {
            super(itemView);
            // CHECK THESE IDs in your item_reservation_ticket_staff.xml
            guestName = itemView.findViewById(R.id.guestName);
            date = itemView.findViewById(R.id.reservationDate);
            time = itemView.findViewById(R.id.reservationTime);
            guests = itemView.findViewById(R.id.guestCount);
            btnCancel = itemView.findViewById(R.id.btnCancelReservation); // Or whatever ID your cancel button has
        }
    }
}
