package com.example.project2_login;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventList extends AppCompatActivity {

    private ListView lvEvents;
    private EventAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        lvEvents = findViewById(R.id.lvEvents);
        adapter = new EventAdapter(this, eventList);
        lvEvents.setAdapter(adapter);

        // Fetch all events when the activity starts
        fetchAllEvents();
    }

    private void fetchAllEvents() {
        ApiClient.getAuthApi().getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventList.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Refresh the ListView with new data
                } else {
                    Toast.makeText(EventList.this, "Failed to retrieve events", Toast.LENGTH_SHORT).show();
                    Log.e("EventView", "Response Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(EventList.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventView", "Fetch failed", t);
            }
        });
    }
}