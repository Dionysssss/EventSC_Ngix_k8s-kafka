package com.example.project2_login;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.AuthRequest;
import com.example.project2_login.model.AuthResponse;
import com.example.project2_login.model.ForgotPasswordRequest;
import com.example.project2_login.model.ForgotPasswordResponse;
import com.example.project2_login.model.MagicLinkRequest;
import com.example.project2_login.model.MagicLinkResponse;
import com.example.project2_login.model.RegisterRequest;
import com.example.project2_login.model.RegisterResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginView extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButton;
    private Button forgotPasswordButton;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        forgotPasswordButton = findViewById(R.id.forgot_password_button);
        authApi = ApiClient.getAuthApi();

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginView.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();


            if (!email.isEmpty() && !password.isEmpty() ) {
                registerUser(email, password);
            } else {
                Toast.makeText(LoginView.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPasswordButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (!email.isEmpty()) {
                MagicLinkRequest request = new MagicLinkRequest(email);
                ApiClient.getAuthApi().sendMagicLink(request).enqueue(new Callback<MagicLinkResponse>() {
                    @Override
                    public void onResponse(Call<MagicLinkResponse> call, Response<MagicLinkResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginView.this, "Magic link sent to your email!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginView.this, "Failed to send magic link: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MagicLinkResponse> call, Throwable t) {
                        Toast.makeText(LoginView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginView.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        AuthRequest authRequest = new AuthRequest(email, password);
        authApi.login(authRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Success response (200 OK)
                    Toast.makeText(LoginView.this, "Login successful", Toast.LENGTH_SHORT).show();
//                    // Navigate to the next screen or handle login success as needed
//                    Long userId = response.body().getUserId();
//                    Intent intent = new Intent(LoginView.this, MapViewActivity.class);
//                    intent.putExtra("userId", userId); // Pass user ID
//                    startActivity(intent);

                    // Navigate to EventView instead of MapViewActivity
                    Long userId = response.body().getUserId();
                    Intent intent = new Intent(LoginView.this, EventList.class);
                    intent.putExtra("userId", userId); // Pass user ID
                    startActivity(intent);

                } else {
                    try {
                        // Handle error cases
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);

                            if (response.code() == 401) {
                                // Incorrect username or password
                                String errorMessage = errorJson.getJSONObject("error").getString("message");
                                Toast.makeText(LoginView.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 400) {
                                // Missing or invalid fields
                                String errorMessage = errorJson.getJSONObject("error").getString("message");
                                Toast.makeText(LoginView.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Generic error for other cases
                                Toast.makeText(LoginView.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                    Toast.makeText(LoginView.this, "Login failed. Unknown error.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginView.this, "Error processing login response.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Handle failure to reach the server or other unexpected issues
                Toast.makeText(LoginView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser(String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest(email, password);
        authApi.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginView.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // Navigate to login or home screen after registration
                } else {
                    Toast.makeText(LoginView.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    // Track HTTP status code and error
                    int statusCode = response.code();
                    String errorBody;
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorBody = "Error reading error body";
                    }
                    Log.e("RegisterError", "Error " + statusCode + ": " + errorBody);
                    Toast.makeText(LoginView.this, "Registration failed: " + errorBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(LoginView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMagicLink(String email) {
        MagicLinkRequest request = new MagicLinkRequest(email); // Create MagicLinkRequest object
        ApiClient.getAuthApi().sendMagicLink(request).enqueue(new Callback<MagicLinkResponse>() {
            @Override
            public void onResponse(Call<MagicLinkResponse> call, Response<MagicLinkResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginView.this, "Magic link sent to your email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginView.this, "Failed to send magic link: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MagicLinkResponse> call, Throwable t) {
                Toast.makeText(LoginView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}