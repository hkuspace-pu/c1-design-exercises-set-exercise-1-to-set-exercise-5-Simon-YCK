package com.example.restaurantapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.File;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuItem> menuList;
    private boolean isStaff;
    private OnItemActionListener actionListener;

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
        return isStaff ? 1 : 0; //1 = Staff Layout, 0 = Guest Layout
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_menu_card_staff, parent, false);
            return new StaffViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_menu_card_guest, parent, false);
            return new GuestViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        if (holder instanceof StaffViewHolder) {
            StaffViewHolder staffHolder = (StaffViewHolder) holder;

            staffHolder.tvName.setText(item.getName());
            staffHolder.tvDesc.setText(item.getDescription());
            staffHolder.tvPrice.setText(String.format("$%.2f", item.getPrice()));

            // Display category
            if (staffHolder.tvCategory != null) {
                staffHolder.tvCategory.setText(item.getCategory());
            }

            // Display image
            if (staffHolder.ivMenuItem != null) {
                if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                    File imgFile = new File(item.getImagePath());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(item.getImagePath());
                        staffHolder.ivMenuItem.setImageBitmap(bitmap);
                    } else {
                        staffHolder.ivMenuItem.setImageResource(R.drawable.ic_restaurant_24);
                    }
                } else {
                    staffHolder.ivMenuItem.setImageResource(R.drawable.ic_restaurant_24);
                }
            }

            staffHolder.btnDelete.setOnClickListener(v -> actionListener.onDeleteClick(position));
            staffHolder.btnEdit.setOnClickListener(v -> actionListener.onEditClick(position));

        } else if (holder instanceof GuestViewHolder) {
            GuestViewHolder guestHolder = (GuestViewHolder) holder;

            guestHolder.tvName.setText(item.getName());
            guestHolder.tvDesc.setText(item.getDescription());
            guestHolder.tvPrice.setText(String.format("$%.2f", item.getPrice()));

            // Display image
            if (guestHolder.ivMenuItem != null) {
                if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                    File imgFile = new File(item.getImagePath());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(item.getImagePath());
                        guestHolder.ivMenuItem.setImageBitmap(bitmap);
                    } else {
                        guestHolder.ivMenuItem.setImageResource(R.drawable.ic_restaurant_24);
                    }
                } else {
                    guestHolder.ivMenuItem.setImageResource(R.drawable.ic_restaurant_24);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // ViewHolder for Staff Layout
    static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice, tvCategory;
        ImageView ivMenuItem;
        Button btnEdit, btnDelete;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.menuItemName);
            tvDesc = itemView.findViewById(R.id.menuItemDescription);
            tvPrice = itemView.findViewById(R.id.menuItemPrice);
            tvCategory = itemView.findViewById(R.id.menuItemCategory);
            ivMenuItem = itemView.findViewById(R.id.menuItemImage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // ViewHolder for Guest Layout
    static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        ImageView ivMenuItem;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.menuItemName);
            tvDesc = itemView.findViewById(R.id.menuItemDescription);
            tvPrice = itemView.findViewById(R.id.menuItemPrice);
            ivMenuItem = itemView.findViewById(R.id.menuItemImage);
        }
    }
}
