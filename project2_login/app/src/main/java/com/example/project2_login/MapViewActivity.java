package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.project2_login.model.Event;
import com.example.project2_login.model.ReceivedEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private int userId; // passed from LoginView

    private static final int EVENT_DETAILS_REQUEST_CODE = 1;
    private static final int CREATE_EVENT_REQUEST_CODE = 2;  // New request code for creating an event

    private GoogleMap mMap;
    private List<Event> eventsList = new ArrayList<>();
    private Gson gson = new Gson();
    private HashMap<Marker, Event> markerEventMap = new HashMap<>();

    // Receiver class from backEnd
    private List<ReceivedEvent> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // Retrieve userId from the Intent
        userId = getIntent().getIntExtra("userId", -1); // -1 as default if userId is not passed
//        Log.d("MapViewActivity", "Received userId: " + userId);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Load sample event data from JSON string
        fetchAllEvents();
//        String sampleJsonData = getSampleJsonData();

//        parseEventData(sampleJsonData);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location (e.g., USC campus) and move the camera
        LatLng uscLocation = new LatLng(34.02116, -118.287132);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uscLocation, 15));

        // Add markers for each event
        addEventMarkers();

        // Set up marker click listener
        mMap.setOnMarkerClickListener(this::onMarkerClick);

        // Set up map click listener to open CreateEventActivity on map click
        mMap.setOnMapClickListener(latLng -> openCreateEventActivity(latLng));
    }

//    private void parseEventData(String jsonData) {
//        Type eventListType = new TypeToken<List<Event>>() {}.getType();
//        eventsList = gson.fromJson(jsonData, eventListType);
//    }

    private List<Event> convertToEventList(List<ReceivedEvent> receivedEvents) {
        List<Event> events = new ArrayList<>();
        for (ReceivedEvent receivedEvent : receivedEvents) {
            events.add(receivedEvent.toEvent());
        }
        return events;
    }

    private void fetchAllEvents() {
        ApiClient.getAuthApi().getAllEvents().enqueue(new Callback<List<ReceivedEvent>>() {
            @Override
            public void onResponse(Call<List<ReceivedEvent>> call, Response<List<ReceivedEvent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventList.clear();
                    eventList.addAll(response.body());

                    // Convert to Event objects and update eventsList
                    eventsList = convertToEventList(eventList);
                    Log.v("MapView", "Fetched updated events from server");
                    addEventMarkers();  // Add updated markers on the map
                } else {
                    Toast.makeText(MapViewActivity.this, "Failed to retrieve events", Toast.LENGTH_SHORT).show();
                    Log.e("EventView", "Response Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ReceivedEvent>> call, Throwable t) {
                Toast.makeText(MapViewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventView", "Fetch failed", t);
            }
        });
    }

    private void addEventMarkers() {
        // Clear existing markers from the map and HashMap
        mMap.clear();
        markerEventMap.clear();

        for (Event event : eventsList) {
            LatLng eventLocation = new LatLng(
                    event.getEventLocation().getLatitude(),
                    event.getEventLocation().getLongitude()
            );

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(eventLocation)
                    .title(event.getEventName()));
            marker.setTag(event); // Attach event object to marker
            markerEventMap.put(marker, event);  // Update marker map with new marker
        }
    }

    private boolean onMarkerClick(Marker marker) {
        Event event = (Event) marker.getTag();
        if (event != null) {
            openEventDetailsActivity(event);
        }
        return true;
    }

    private void openEventDetailsActivity(Event event) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        String eventJson = gson.toJson(event);
        intent.putExtra("event_data", eventJson);
        intent.putExtra("event_id", Integer.parseInt(event.getEventId())); // pass event_Id to EventDetailsActivity
        intent.putExtra("user_id", userId); // pass userId to EventDetailsActivity
        startActivityForResult(intent, EVENT_DETAILS_REQUEST_CODE);
    }



    private void openCreateEventActivity(LatLng latLng) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra("latitude", latLng.latitude);
        intent.putExtra("longitude", latLng.longitude);
        intent.putExtra("userId", userId); // Pass userId to CreateEventActivity
        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Re-fetch all events to get the latest data from the server
            fetchAllEvents();
        }
    }

    private void updateEventInList(Event updatedEvent, String eventId) {
        // Find and update the event in eventsList by its ID
        for (int i = 0; i < eventsList.size(); i++) {
            Event event = eventsList.get(i);
            if (event.getEventId().equals(eventId)) {
                // Update the event in the list
                eventsList.set(i, updatedEvent);

                // Find the original marker in markerEventMap and update its position if the location changed
                Marker originalMarker = null;
                for (Marker marker : markerEventMap.keySet()) {
                    if (markerEventMap.get(marker).getEventId().equals(eventId)) {
                        originalMarker = marker;
                        break;
                    }
                }
                if (originalMarker != null) {
                    // Remove the old marker and update the mapping
                    originalMarker.remove();
                    markerEventMap.remove(originalMarker);

                    // Add a new marker for the updated event location
                    LatLng updatedLocation = new LatLng(
                            updatedEvent.getEventLocation().getLatitude(),
                            updatedEvent.getEventLocation().getLongitude()
                    );
                    Marker newMarker = mMap.addMarker(new MarkerOptions()
                            .position(updatedLocation)
                            .title(updatedEvent.getEventName()));  // Optionally update the title as well
                    newMarker.setTag(updatedEvent);  // Set the updated event as the tag

                    // Update markerEventMap with the new marker
                    markerEventMap.put(newMarker, updatedEvent);
                }
                break;
            }
        }
    }

    private void addNewEvent(Event newEvent) {
        eventsList.add(newEvent);  // Add the new event to the list

        // Add a marker for the new event
        LatLng newEventLocation = new LatLng(
                newEvent.getEventLocation().getLatitude(),
                newEvent.getEventLocation().getLongitude()
        );

        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(newEventLocation)
                .title(newEvent.getEventName()));
        newMarker.setTag(newEvent);  // Attach new event to the marker
        markerEventMap.put(newMarker, newEvent);  // Add to markerEventMap
    }

    private String getSampleJsonData() {
        // Sample JSON data with updated format
        return "[\n" +
                "  {\n" +
                "    \"eventId\": \"1\",\n" +
                "    \"eventName\": \"USC Event\",\n" +
                "    \"eventLocation\": {\"latitude\": 34.0201, \"longitude\": -118.2858},\n" +
                "    \"eventDescription\": \"Details of the event\",\n" +
                "    \"eventDate\": \"2024-10-15\",\n" +
                "    \"eventTime\": \"18:00:00\",\n" +
                "    \"eventCreatorId\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"eventId\": \"2\",\n" +
                "    \"eventName\": \"USC Event\",\n" +
                "    \"eventLocation\": {\"latitude\": 34.0201, \"longitude\": -118.2858},\n" +
                "    \"eventDescription\": \"Details of the event\",\n" +
                "    \"eventDate\": \"2024-10-15\",\n" +
                "    \"eventTime\": \"18:00:00\",\n" +
                "    \"eventCreatorId\": 1\n" +
                "  }\n" +
                "]";
    }
}
