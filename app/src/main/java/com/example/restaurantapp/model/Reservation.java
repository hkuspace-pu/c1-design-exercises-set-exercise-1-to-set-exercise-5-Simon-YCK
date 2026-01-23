package com.example.restaurantapp.model;

public class Reservation {
    private int id;
    private String guestName;
    private String date;
    private String time;
    private int guestCount;

    public Reservation(int id, String guestName, String date, String time, int guestCount) {
        this.id = id;
        this.guestName = guestName;
        this.date = date;
        this.time = time;
        this.guestCount = guestCount;
    }

    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getGuestCount() { return guestCount; }
}
