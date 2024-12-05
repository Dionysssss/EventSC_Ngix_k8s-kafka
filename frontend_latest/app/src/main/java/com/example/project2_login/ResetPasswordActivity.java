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

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private AuthApi authApi;
    private String pw;
    private String email;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailField = findViewById(R.id.email);
        Button sendCodeButton = findViewById(R.id.send_code_button);

        authApi = ApiClient.getAuthApi(); // Initialize authApi

        email = getIntent().getStringExtra("email");

        sendCodeButton.setOnClickListener(v -> {
            pw = emailField.getText().toString().trim();
            if (!email.isEmpty()) {
                // Send verification code to email
                sendPassWord(email,pw);
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPassWord(String email,String code) {
        // Create a request object
        MagicLinkVerificationRequest request = new MagicLinkVerificationRequest(email, code);

        authApi.saveMagicLink(request).enqueue(new Callback<MagicLinkVerificationResponse>() {
            @Override
            public void onResponse(Call<MagicLinkVerificationResponse> call, Response<MagicLinkVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // If verification is successful, proceed to the main activity or log in the user
                    Toast.makeText(ResetPasswordActivity.this, "New password saved successful. Please login in...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginView.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Invalid or expired verification code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MagicLinkVerificationResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    private void sendVerificationCode(String email) {
        // Placeholder for sending code to user's email
        Toast.makeText(this, "Verification code sent to " + email, Toast.LENGTH_SHORT).show();

        // Redirect to VerificationActivity to enter the code
        Intent intent = new Intent(ResetPasswordActivity.this, LoginView.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
    */

}