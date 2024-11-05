package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailField = findViewById(R.id.email);
        Button sendCodeButton = findViewById(R.id.send_code_button);

        sendCodeButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (!email.isEmpty()) {
                // Send verification code to email
                sendVerificationCode(email);
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationCode(String email) {
        // Placeholder for sending code to user's email
        Toast.makeText(this, "Verification code sent to " + email, Toast.LENGTH_SHORT).show();

        // Redirect to VerificationActivity to enter the code
        Intent intent = new Intent(ResetPasswordActivity.this, VerificationActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}