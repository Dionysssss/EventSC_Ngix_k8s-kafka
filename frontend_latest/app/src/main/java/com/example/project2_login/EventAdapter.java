package com.example.project2_login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project2_login.model.Event;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the event item at this position
        Event event = getItem(position);

        // Check if an existing view is being reused, otherwise inflate a new view from custom layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        // Lookup views for data population
        TextView tvEventName = convertView.findViewById(R.id.tvEventName);
        TextView tvEventLocation = convertView.findViewById(R.id.tvEventLocation);
        TextView tvEventTime = convertView.findViewById(R.id.tvEventTime);
        TextView tvEventDescription = convertView.findViewById(R.id.tvEventDescription);
        TextView tvEventCreator = convertView.findViewById(R.id.tvEventCreator);

        // Populate the data into the template view using the event object
        tvEventName.setText(event.getEventName());
        tvEventLocation.setText("Location: " + event.getEventLocation());
        tvEventTime.setText("Time: " + event.getEventTime());
        tvEventDescription.setText("Description: " + event.getEventDescription());
        tvEventCreator.setText("Creator: " + event.getEventId());

        // Return the completed view to render on screen
        return convertView;
    }
}