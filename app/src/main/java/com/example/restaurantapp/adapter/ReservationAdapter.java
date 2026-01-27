package com.example.restaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation_ticket_staff, parent, false);
        return new ResViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResViewHolder holder, int position) {
        Reservation res = list.get(position);

        // Map data to your XML IDs
        if(holder.guestName != null) holder.guestName.setText(res.getGuestName());
        if(holder.date != null) holder.date.setText(res.getDate());
        if(holder.time != null) holder.time.setText(res.getTime());
        if(holder.guests != null) holder.guests.setText(res.getGuestCount() + " Guests");

        // ✅ DISPLAY SPECIAL REQUESTS
        String specialReq = res.getSpecialRequests();
        if (specialReq != null && !specialReq.trim().isEmpty()) {
            holder.specialRequestsSection.setVisibility(View.VISIBLE);
            holder.specialRequests.setText(specialReq);
        } else {
            holder.specialRequestsSection.setVisibility(View.GONE);
        }

        // Handle Cancel Click
        holder.btnCancel.setOnClickListener(v -> listener.onCancelClick(res));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ResViewHolder extends RecyclerView.ViewHolder {
        TextView guestName, date, time, guests, specialRequests;
        LinearLayout specialRequestsSection;
        Button btnCancel;

        public ResViewHolder(@NonNull View itemView) {
            super(itemView);
            guestName = itemView.findViewById(R.id.guestName);
            date = itemView.findViewById(R.id.reservationDate);
            time = itemView.findViewById(R.id.reservationTime);
            guests = itemView.findViewById(R.id.guestCount);
            specialRequests = itemView.findViewById(R.id.specialRequests); // ✅ ADD
            specialRequestsSection = itemView.findViewById(R.id.specialRequestsSection); // ✅ ADD
            btnCancel = itemView.findViewById(R.id.btnCancelReservation);
        }
    }
}
