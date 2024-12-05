package com.example.project2_login;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.Comment;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventRequest;
import com.example.project2_login.model.EventResponse;
import com.google.gson.Gson;
import com.example.project2_login.model.GeocodingHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitleEditText;
    private EditText eventDescriptionEditText;
    private TextView eventLocationTextView;
    private TextView eventDateTimeTextView;
    private double latitude;
    private double longitude;
    private String selectedDateTime;
    private int userId; // passed from MapViewActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Retrieve userId from Intent
        userId = getIntent().getIntExtra("userId", -1);
        Log.d("CreateEventActivity", "Received userId: " + userId);// Default to -1 if not passed

        // Initialize views
        eventTitleEditText = findViewById(R.id.event_title);
        eventDescriptionEditText = findViewById(R.id.event_description);
        eventLocationTextView = findViewById(R.id.event_location);
        eventDateTimeTextView = findViewById(R.id.event_date_time);
        Button saveEventButton = findViewById(R.id.save_event_button);
        Button goBackToMapButton = findViewById(R.id.go_back_to_map_button);

        // Retrieve latitude and longitude from the Intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        // Display location in the Location field
        eventLocationTextView.setText(String.format("Location: %.6f, %.6f", latitude, longitude));

        // Fetch and display the address
        fetchAddressAndDisplay(latitude, longitude);


        // Set up Save button click listener
        saveEventButton.setOnClickListener(v -> saveEvent());

        // Set up Go Back to Map button click listener
        goBackToMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreateEventActivity.this, MapViewActivity.class);
            intent.putExtra("userId", userId); // Pass userId back to MapViewActivity if needed
            startActivity(intent);
            finish(); // Close the current activity and return to MapView
        });

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

        // Split date and time
        String eventDate = dateTime.split(" ")[0]; // Date part
        String eventTime = dateTime.split(" ")[1]; // Time part

        // Initialize an empty list for comments
        List<Comment> comments = new ArrayList<>();

        // Create an EventRequest object to send to the backend
        EventRequest eventRequest = new EventRequest(
                title,
                latitude,
                longitude,
                description,
                eventDate,
                eventTime,
                userId // Use userId retrieved from Intent
        );
        eventRequest.setComments(comments); // Set empty comments list

        // Send the request to the backend
        ApiClient.getAuthApi().createEvent(eventRequest).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateEventActivity.this, "Event created successfully!", Toast.LENGTH_SHORT).show();

                    // Convert the response to JSON format for debugging
                    String newEventDataJson = new Gson().toJson(response.body().toEvent());

                    // Prepare intent to navigate to MapViewActivity
                    Intent intent = new Intent(CreateEventActivity.this, MapViewActivity.class);
                    intent.putExtra("new_event_data", newEventDataJson); // Pass new event data if needed
                    intent.putExtra("userId", userId); // Pass userId to MapViewActivity
                    startActivity(intent);

                    // Close the activity and return to the map
                    finish();
                } else {
                    Toast.makeText(CreateEventActivity.this, "Failed to create event: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(CreateEventActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAddressAndDisplay(double latitude, double longitude) {
        GeocodingHelper.fetchAddress(latitude, longitude, new GeocodingHelper.GeocodingCallback() {
            @Override
            public void onAddressFetched(String address) {
                runOnUiThread(() -> {
                    // Display both coordinates and address
                    eventLocationTextView.setText(
                            String.format("Coordinates: %.6f, %.6f\nAddress: %s", latitude, longitude, address)
                    );
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    // Display coordinates only if the address fetch fails
                    eventLocationTextView.setText(String.format("Coordinates: %.6f, %.6f", latitude, longitude));
                    Toast.makeText(CreateEventActivity.this, "Failed to fetch address: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

