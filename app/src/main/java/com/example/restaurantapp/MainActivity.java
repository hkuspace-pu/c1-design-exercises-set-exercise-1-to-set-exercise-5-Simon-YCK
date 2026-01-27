package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantapp.model.User;
import com.example.restaurantapp.network.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    private static final String STUDENT_ID = "YangChunKit_20177089";
    private static final String API_BASE_URL = "http://10.240.72.69/comp2000/coursework/read_user/" + STUDENT_ID + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Login Button Click
        loginButton.setOnClickListener(v -> handleApiLogin());

        // ENTER KEY LOGIN (NEW FEATURE)
        //if (passwordInput != null) {
        //    passwordInput.setOnEditorActionListener((v, actionId, event) -> {
        //        if (actionId == EditorInfo.IME_ACTION_DONE ||
        //                (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
        //                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
        //            handleApiLogin(); // Trigger same login method
        //            return true;
        //        }
        //        return false;
        //    });
        //}
        // Sign Up Click
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(v -> {
                startActivity(new Intent(this, RegisterActivity.class));
            });
        }
    }

    private void handleApiLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Construct API URL
        String url = API_BASE_URL + username;

        // 2. Create Volley Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // The API returns: { "user": { ... } }
                        if (response.has("user")) {
                            JSONObject userJson = response.getJSONObject("user");

                            // Parse JSON
                            Gson gson = new Gson();
                            User user = gson.fromJson(userJson.toString(), User.class);

                            // Verify Password
                            if (user.getPassword().equals(password)) {
                                navigateToDashboard(user.getUsertype(), user.getUsername());
                            } else {
                                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Check logic: if error is 404, user doesn't exist
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Connection Error. Check VPN/Wifi.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 3. Add retry policy and send request
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, // 10 seconds timeout
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void navigateToDashboard(String userType, String userName) {
        Intent intent;
        if ("staff".equalsIgnoreCase(userType) || "admin".equalsIgnoreCase(userType)) {
            intent = new Intent(this, StaffDashboardActivity.class);
            Toast.makeText(this, "Login Successful: Staff", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(this, GuestDashboardActivity.class);
            // Pass guest name to dashboard
            intent.putExtra("guestName", userName);
            Toast.makeText(this, "Login Successful: Guest", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
        finish(); // Close login screen
    }
}
