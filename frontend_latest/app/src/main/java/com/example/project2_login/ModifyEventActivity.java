package com.example.project2_login;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventRequest;
import com.example.project2_login.model.EventResponse;
import com.example.project2_login.model.GeocodingHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editEventName;
    private EditText editEventDescription;
    private EditText editEventLocation;
    private EditText editEventDate;
    private EditText editEventTime;
    private Button saveEventButton;
    private Button goBackButton;
    private MapView editMap;
    private GoogleMap mMap;
    private int eventId;
    private TextView editEventAddress;

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        eventId = getIntent().getIntExtra("event_id", -1);

        // Initialize views
        editEventName = findViewById(R.id.edit_event_name);
        editEventDescription = findViewById(R.id.edit_event_description);
        editEventLocation = findViewById(R.id.edit_event_location);
        editEventDate = findViewById(R.id.edit_event_date);
        editEventTime = findViewById(R.id.edit_event_time);
        saveEventButton = findViewById(R.id.save_event_button);
        editEventAddress = findViewById(R.id.edit_event_address);
        editMap = findViewById(R.id.edit_map);

        // Initialize map
        editMap.onCreate(savedInstanceState);
        editMap.getMapAsync(this);

        // Set up date and time pickers
        setupDateField();
        setupTimeField();

        // Get the event data from the Intent
        String eventJson = getIntent().getStringExtra("event_data");
        if (eventJson != null) {
            currentEvent = new Gson().fromJson(eventJson, Event.class);
            displayEventDetails(currentEvent);
        }

        // Set up Save button listener
        saveEventButton.setOnClickListener(v -> saveEventDetails());

        // Initialize buttons and views
        goBackButton = findViewById(R.id.go_back_button);
        // Set up the button click listener
        goBackButton.setOnClickListener(v -> goBackToDetails());
    }

    private void goBackToDetails() {
        finish(); // Close the current activity and return to the previous one
    }

    private void displayEventDetails(Event event) {
        editEventName.setText(event.getEventName());
        editEventDescription.setText(event.getEventDescription());
        editEventLocation.setText(event.getEventLocation().getLatitude() + ", " + event.getEventLocation().getLongitude());

        // Fetch and display the address
        fetchAddressFromLatLong(event.getEventLocation().getLatitude(), event.getEventLocation().getLongitude());

        editEventDate.setText(event.getEventDate());
        editEventTime.setText(event.getEventTime());
    }

    private void setupDateField() {
        editEventDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Update the EditText with the selected date
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        editEventDate.setText(formattedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupTimeField() {
        editEventTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        // Update the EditText with the selected time
                        String formattedTime = String.format("%02d:%02d:00", selectedHour, selectedMinute);
                        editEventTime.setText(formattedTime);
                    }, hour, minute, true);

            timePickerDialog.show();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (currentEvent != null) {
            LatLng initialLocation = new LatLng(currentEvent.getEventLocation().getLatitude(), currentEvent.getEventLocation().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
        }

        // Set map click listener to update location
        mMap.setOnMapClickListener(latLng -> {
            editEventLocation.setText(latLng.latitude + ", " + latLng.longitude);
            currentEvent.getEventLocation().setLatitude(latLng.latitude);
            currentEvent.getEventLocation().setLongitude(latLng.longitude);

            fetchAddressFromLatLong(latLng.latitude, latLng.longitude);
        });
    }

    private void fetchAddressFromLatLong(double latitude, double longitude) {
        GeocodingHelper.fetchAddress(latitude, longitude, new GeocodingHelper.GeocodingCallback() {
            @Override
            public void onAddressFetched(String address) {
                runOnUiThread(() -> editEventAddress.setText("Address: " + address));
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(ModifyEventActivity.this, "Error fetching address: " + errorMessage, Toast.LENGTH_SHORT).show();
                    editEventAddress.setText("Address: Not available");
                });
            }
        });
    }


    private void saveEventDetails() {
        // Update the currentEvent object with new details
        currentEvent.setEventName(editEventName.getText().toString());
        currentEvent.setEventDescription(editEventDescription.getText().toString());
        currentEvent.setEventDate(editEventDate.getText().toString());
        currentEvent.setEventTime(editEventTime.getText().toString());

        // Create an EventRequest object to send to the backend
        EventRequest eventRequest = new EventRequest(
                currentEvent.getEventName(),
                currentEvent.getEventLocation().getLatitude(),
                currentEvent.getEventLocation().getLongitude(),
                currentEvent.getEventDescription(),
                currentEvent.getEventDate(),
                currentEvent.getEventTime(),
                currentEvent.getEventCreatorId() // Assuming you have this in your Event model
        );

        // Make the API call to update the event on the backend
        ApiClient.getAuthApi().updateEvent(eventId, eventRequest).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ModifyEventActivity.this, "Event updated successfully!", Toast.LENGTH_SHORT).show();

                    // Send the updated event back to MapViewActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("event_id", currentEvent.getEventId());
                    resultIntent.putExtra("updated_event_data", new Gson().toJson(currentEvent));
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close this activity
                } else {
                    Toast.makeText(ModifyEventActivity.this, "Failed to update event: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(ModifyEventActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        editMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        editMap.onLowMemory();
    }
}
