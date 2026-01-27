package com.example.restaurantapp.model;

public class Reservation {
    private int id;
    private String guestName;
    private String date;
    private String time;
    private int guestCount;
    private String specialRequests; // ✅ ADD THIS

    // ✅ UPDATE constructor
    public Reservation(int id, String guestName, String date, String time, int guestCount, String specialRequests) {
        this.id = id;
        this.guestName = guestName;
        this.date = date;
        this.time = time;
        this.guestCount = guestCount;
        this.specialRequests = specialRequests;
    }

    // Existing getters
    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getGuestCount() { return guestCount; }

    // ✅ ADD getter
    public String getSpecialRequests() { return specialRequests; }
}
