package com.example.restaurantapp.model;

public class User {
    private String username;
    private String password; // hashed in real apps, plain for this assessment
    private String firstname;
    private String lastname;
    private String email;
    private String usertype; // "staff" or "guest"

    public User(String username, String password, String firstname, String lastname, String email, String usertype) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.usertype = usertype;
    }

    public String getUsername() { return username; }
    public String getUsertype() { return usertype; }
    public String getPassword() { return password; }
}
