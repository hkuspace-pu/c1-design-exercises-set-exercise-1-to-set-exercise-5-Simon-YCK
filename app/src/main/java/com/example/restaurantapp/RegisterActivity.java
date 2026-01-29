package com.example.restaurantapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantapp.network.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etFirstName, etLastName, etEmail, etContact, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private View btnBack;
    private static final String STUDENT_ID = "YangChunKit_20177089";
    private static final String API_BASE_URL = "http://10.240.72.69/comp2000/coursework/create_user/" + STUDENT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // Register button
        btnRegister.setOnClickListener(v -> handleRegistration());

        // Enter key support
        etPasswordConfirm.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleRegistration();
                return true;
            }
            return false;
        });

        // Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void handleRegistration() {
        String username = etUsername.getText().toString().trim();
        String firstname = etFirstName.getText().toString().trim();
        String lastname = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        // Validation
        if (username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||
                email.isEmpty() || contact.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = API_BASE_URL;
        Log.d("REGISTER_URL", "URL: " + url);

        // Username goes in the JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("firstname", firstname);
            jsonBody.put("lastname", lastname);
            jsonBody.put("email", email);
            jsonBody.put("contact", contact);
            jsonBody.put("usertype", "guest");

            Log.d("REGISTER_BODY", jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating request", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make POST request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {
                    Log.e("REGISTER_ERROR", "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("REGISTER_ERROR", "Status Code: " + error.networkResponse.statusCode);
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e("REGISTER_ERROR", "Response: " + errorBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(this, "Registration failed. Username may already exist.", Toast.LENGTH_LONG).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
