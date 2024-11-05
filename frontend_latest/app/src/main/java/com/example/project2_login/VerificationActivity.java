package com.example.project2_login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {

    private EditText verificationCodeField;
    private EditText newPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verificationCodeField = findViewById(R.id.verification_code);
        newPasswordField = findViewById(R.id.new_password);
        Button resetPasswordButton = findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(v -> {
            String code = verificationCodeField.getText().toString().trim();
            String newPassword = newPasswordField.getText().toString().trim();

            if (!code.isEmpty() && !newPassword.isEmpty()) {
                // Verify code and reset password
                resetPassword(code, newPassword);
            } else {
                Toast.makeText(VerificationActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword(String code, String newPassword) {
        // Placeholder for resetting password logic
        Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
        finish(); // Return to the login screen
    }
}
