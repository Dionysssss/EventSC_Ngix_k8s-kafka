package com.example.project2_login;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitleEditText;
    private EditText eventDescriptionEditText;
    private TextView eventLocationTextView;
    private TextView eventDateTimeTextView;
    private double latitude;
    private double longitude;
    private String selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize views
        eventTitleEditText = findViewById(R.id.event_title);
        eventDescriptionEditText = findViewById(R.id.event_description);
        eventLocationTextView = findViewById(R.id.event_location);
        eventDateTimeTextView = findViewById(R.id.event_date_time);
        Button saveEventButton = findViewById(R.id.save_event_button);

        // Retrieve latitude and longitude from the Intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        // Display location in the Location field
        eventLocationTextView.setText(String.format("Location: %.6f, %.6f", latitude, longitude));

        // Set up Save button click listener
        saveEventButton.setOnClickListener(v -> saveEvent());

        // Set up Date and Time picker
        eventDateTimeTextView.setOnClickListener(v -> openDateTimePicker());
    }

    private void openDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        // Open DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    // Update calendar with selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Open TimePickerDialog after selecting date
                    openTimePicker(calendar);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void openTimePicker(Calendar calendar) {
        // Open TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    // Update calendar with selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    // Format date and time as a string and set it on the TextView
                    selectedDateTime = String.format("%04d-%02d-%02d %02d:%02d",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE));
                    eventDateTimeTextView.setText(selectedDateTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }

    private void saveEvent() {
        // Get event details from input fields
        String title = eventTitleEditText.getText().toString();
        String description = eventDescriptionEditText.getText().toString();
        String dateTime = selectedDateTime;

        // Check if essential fields are filled
        if (title.isEmpty() || dateTime == null || dateTime.isEmpty()) {
            Toast.makeText(this, "Please enter the event name and date/time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a confirmation message with event details
        String confirmationMessage = String.format(
                "Event saved:\nTitle: %s\nLocation: %.6f, %.6f\nDate/Time: %s",
                title, latitude, longitude, dateTime);
        Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show();

        // Close the activity and return to the map
        finish();
    }
}
