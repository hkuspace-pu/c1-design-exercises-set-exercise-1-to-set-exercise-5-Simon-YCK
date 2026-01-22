package com.example.restaurantapp.model;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category; // e.g., "Main", "Drink"

    public MenuItem(int id, String name, String description, double price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}
