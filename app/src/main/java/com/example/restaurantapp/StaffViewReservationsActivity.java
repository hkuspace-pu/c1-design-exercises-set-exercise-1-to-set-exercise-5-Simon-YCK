package com.example.restaurantapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.adapter.ReservationAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.Reservation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffViewReservationsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> allReservations; // Original list
    private List<Reservation> filteredList;    // Filtered/sorted list
    private SearchView searchView;
    private Button btnFilter;
    private View btnBack;
    private TextView tvActiveFilters;

    // Filter states
    private String sortBy = "newest"; // newest, oldest, name, date
    private String filterBy = "all";  // all, today, week, upcoming

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_reservations);

        dbHelper = new DatabaseHelper(this);
        searchView = findViewById(R.id.searchView);
        btnFilter = findViewById(R.id.btnFilter);
        btnBack = findViewById(R.id.btnBack);
        tvActiveFilters = findViewById(R.id.tvActiveFilters);

        recyclerView = findViewById(R.id.recyclerReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        loadReservations();
        setupSearchView();
        setupFilterButton();
    }

    private void loadReservations() {
        allReservations = new ArrayList<>();
        Cursor cursor = dbHelper.getAllReservations();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                int guests = cursor.getInt(4);
                String specialReq = cursor.getString(5);

                allReservations.add(new Reservation(id, name, date, time, guests, specialReq));
            } while (cursor.moveToNext());
            cursor.close();
        }

        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        filteredList = new ArrayList<>(allReservations);

        // Apply date filter
        filteredList = filterByDate(filteredList);

        // Apply sorting
        filteredList = sortReservations(filteredList);

        // Setup adapter
        adapter = new ReservationAdapter(filteredList, this::showCancelDialog);
        recyclerView.setAdapter(adapter);

        updateActiveFiltersDisplay();
    }

    private List<Reservation> filterByDate(List<Reservation> list) {
        if (filterBy.equals("all")) return list;

        List<Reservation> filtered = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (Reservation res : list) {
            try {
                Date resDate = sdf.parse(res.getDate());
                if (resDate == null) continue;

                Calendar resCal = Calendar.getInstance();
                resCal.setTime(resDate);

                switch (filterBy) {
                    case "today":
                        if (isSameDay(today, resCal)) {
                            filtered.add(res);
                        }
                        break;
                    case "week":
                        Calendar weekEnd = (Calendar) today.clone();
                        weekEnd.add(Calendar.DAY_OF_YEAR, 7);
                        if (!resDate.before(today.getTime()) && resDate.before(weekEnd.getTime())) {
                            filtered.add(res);
                        }
                        break;
                    case "upcoming":
                        Calendar upcoming = (Calendar) today.clone();
                        upcoming.add(Calendar.DAY_OF_YEAR, 7);
                        if (!resDate.before(today.getTime()) && resDate.before(upcoming.getTime())) {
                            filtered.add(res);
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filtered;
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private List<Reservation> sortReservations(List<Reservation> list) {
        List<Reservation> sorted = new ArrayList<>(list);

        switch (sortBy) {
            case "newest":
                Collections.reverse(sorted); // Assuming list is already oldest first from DB
                break;
            case "oldest":
                // Already in oldest first order from DB
                break;
            case "name":
                Collections.sort(sorted, (a, b) -> a.getGuestName().compareToIgnoreCase(b.getGuestName()));
                break;
            case "date":
                Collections.sort(sorted, (a, b) -> a.getDate().compareTo(b.getDate()));
                break;
        }
        return sorted;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterByName(newText);
                return true;
            }
        });
    }

    private void filterByName(String query) {
        if (query.isEmpty()) {
            applyFiltersAndSort();
            return;
        }

        List<Reservation> searchResults = new ArrayList<>();
        for (Reservation res : filteredList) {
            if (res.getGuestName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(res);
            }
        }

        adapter = new ReservationAdapter(searchResults, this::showCancelDialog);
        recyclerView.setAdapter(adapter);
    }

    private void setupFilterButton() {
        btnFilter.setOnClickListener(v -> showFilterBottomSheet());
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter_reservations, null);
        bottomSheet.setContentView(sheetView);

        // Get views
        RadioGroup radioGroupSort = sheetView.findViewById(R.id.radioGroupSort);
        RadioGroup radioGroupFilter = sheetView.findViewById(R.id.radioGroupFilter);
        Button btnApply = sheetView.findViewById(R.id.btnApplyFilter);
        Button btnReset = sheetView.findViewById(R.id.btnResetFilter);

        // Set current selections
        setRadioButtonByValue(radioGroupSort, sortBy);
        setRadioButtonByValue(radioGroupFilter, filterBy);

        // Apply button
        btnApply.setOnClickListener(v -> {
            sortBy = getSelectedSortValue(radioGroupSort);
            filterBy = getSelectedFilterValue(radioGroupFilter);
            applyFiltersAndSort();
            bottomSheet.dismiss();
            Toast.makeText(this, "Filters applied", Toast.LENGTH_SHORT).show();
        });

        // Reset button
        btnReset.setOnClickListener(v -> {
            sortBy = "newest";
            filterBy = "all";
            searchView.setQuery("", false);
            applyFiltersAndSort();
            bottomSheet.dismiss();
            Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show();
        });

        bottomSheet.show();
    }

    private void setRadioButtonByValue(RadioGroup group, String value) {
        // Set radio button based on current filter
        if (group.getId() == R.id.radioGroupSort) {
            switch (value) {
                case "newest": group.check(R.id.radioNewest); break;
                case "oldest": group.check(R.id.radioOldest); break;
                case "name": group.check(R.id.radioGuestName); break;
                case "date": group.check(R.id.radioDate); break;
            }
        } else {
            switch (value) {
                case "all": group.check(R.id.radioAll); break;
                case "today": group.check(R.id.radioToday); break;
                case "week": group.check(R.id.radioThisWeek); break;
                case "upcoming": group.check(R.id.radioUpcoming); break;
            }
        }
    }

    private String getSelectedSortValue(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        if (id == R.id.radioNewest) return "newest";
        if (id == R.id.radioOldest) return "oldest";
        if (id == R.id.radioGuestName) return "name";
        if (id == R.id.radioDate) return "date";
        return "newest";
    }

    private String getSelectedFilterValue(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        if (id == R.id.radioAll) return "all";
        if (id == R.id.radioToday) return "today";
        if (id == R.id.radioThisWeek) return "week";
        if (id == R.id.radioUpcoming) return "upcoming";
        return "all";
    }

    private void updateActiveFiltersDisplay() {
        String filterText = "";

        if (!sortBy.equals("newest")) {
            filterText += "Sort: " + sortBy + " ";
        }
        if (!filterBy.equals("all")) {
            filterText += "• Filter: " + filterBy;
        }

        if (!filterText.isEmpty()) {
            tvActiveFilters.setText("Active filters: " + filterText);
            tvActiveFilters.setVisibility(View.VISIBLE);
        } else {
            tvActiveFilters.setVisibility(View.GONE);
        }
    }

    private void showCancelDialog(Reservation res) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                boolean success = dbHelper.deleteReservation(res.getId());
                if (success) {
                    Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                    loadReservations();
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
