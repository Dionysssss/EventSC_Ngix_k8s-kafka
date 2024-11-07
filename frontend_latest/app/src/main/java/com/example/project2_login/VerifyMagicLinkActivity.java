package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.MagicLinkVerificationRequest;
import com.example.project2_login.model.MagicLinkVerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMagicLinkActivity extends AppCompatActivity {

    private EditText verificationCodeEditText;
    private Button verifyButton;
    private AuthApi authApi;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_magic_link);

        // Initialize fields
        verificationCodeEditText = findViewById(R.id.verification_code);
        verifyButton = findViewById(R.id.verify_button);
        authApi = ApiClient.getAuthApi(); // Initialize authApi

        email = getIntent().getStringExtra("email");

        // Set up the verification button
        verifyButton.setOnClickListener(v -> {
            String verificationCode = verificationCodeEditText.getText().toString().trim();
            if (!verificationCode.isEmpty()) {
                verifyMagicLink(verificationCode);
            } else {
                Toast.makeText(VerifyMagicLinkActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyMagicLink(String verificationCode) {
        // Create a request object
        MagicLinkVerificationRequest request = new MagicLinkVerificationRequest(email, verificationCode);

        authApi.verifyMagicLink(request).enqueue(new Callback<MagicLinkVerificationResponse>() {
            @Override
            public void onResponse(Call<MagicLinkVerificationResponse> call, Response<MagicLinkVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // If verification is successful, proceed to the main activity or log in the user
                    Toast.makeText(VerifyMagicLinkActivity.this, "Verification successful. Logging in...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyMagicLinkActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyMagicLinkActivity.this, "Invalid or expired verification code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MagicLinkVerificationResponse> call, Throwable t) {
                Toast.makeText(VerifyMagicLinkActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}