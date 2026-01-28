package com.example.restaurantapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.R;
import com.example.restaurantapp.model.MenuItem;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuItem> menuList;
    private boolean isStaff; // Flag to decide which layout to show
    private OnItemActionListener actionListener;

    // Interface for buttons
    public interface OnItemActionListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public MenuAdapter(List<MenuItem> menuList, boolean isStaff, OnItemActionListener listener) {
        this.menuList = menuList;
        this.isStaff = isStaff;
        this.actionListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        // Return 1 for Staff, 0 for Guest
        return isStaff ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            // STAFF LAYOUT
            View view = inflater.inflate(R.layout.item_menu_card_staff, parent, false);
            return new StaffViewHolder(view);
        } else {
            // GUEST LAYOUT
            View view = inflater.inflate(R.layout.item_menu_card_guest, parent, false);
            return new GuestViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        if (holder instanceof StaffViewHolder) {
            StaffViewHolder staffHolder = (StaffViewHolder) holder;
            // Map data to XML IDs
            staffHolder.tvName.setText(item.getName());
            staffHolder.tvDesc.setText(item.getDescription());
            staffHolder.tvPrice.setText(String.format("$%.2f", item.getPrice()));

            staffHolder.btnDelete.setOnClickListener(v -> actionListener.onDeleteClick(position));
            staffHolder.btnEdit.setOnClickListener(v -> actionListener.onEditClick(position));

        } else if (holder instanceof GuestViewHolder) {
            GuestViewHolder guestHolder = (GuestViewHolder) holder;
            guestHolder.tvName.setText(item.getName());
            guestHolder.tvDesc.setText(item.getDescription());
            guestHolder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // ViewHolder for Staff Layout
    static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        Button btnEdit, btnDelete; // Using Button as per your XML

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.menuItemName);
            tvDesc = itemView.findViewById(R.id.menuItemDescription);
            tvPrice = itemView.findViewById(R.id.menuItemPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // ViewHolder for Guest Layout
    static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.menuItemName);
            tvDesc = itemView.findViewById(R.id.menuItemDescription);
            tvPrice = itemView.findViewById(R.id.menuItemPrice);
        }
    }
}
