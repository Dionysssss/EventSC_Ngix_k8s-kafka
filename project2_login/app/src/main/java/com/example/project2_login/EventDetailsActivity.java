package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.Comment;
import com.example.project2_login.model.CommentRequest;
import com.example.project2_login.model.CommentResponse;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventRequest;
import com.example.project2_login.model.EventResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    private static final int MODIFY_EVENT_REQUEST_CODE = 1;

    private EditText eventNameEditText;
    private EditText eventLocationEditText;
    private EditText eventTimeEditText;
    private EditText eventDescriptionEditText;
    private EditText eventCreatorEditText;
    private EditText newCommentEditText;
    private Button editEventButton;
    private Button backToMapButton;
    private Button postCommentButton;

    private ListView commentsListView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentsList = new ArrayList<>();

    private int eventId;
    private int userId;
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventId = getIntent().getIntExtra("event_id", -1); // -1 if not found
        userId = getIntent().getIntExtra("user_id", -1); // Default to -1 if not passed

        // Initialize views
        eventNameEditText = findViewById(R.id.event_name);
        eventLocationEditText = findViewById(R.id.event_location);
        eventTimeEditText = findViewById(R.id.event_time);
        eventDescriptionEditText = findViewById(R.id.event_description);
        eventCreatorEditText = findViewById(R.id.event_creator);
        newCommentEditText = findViewById(R.id.new_comment_edit_text);
        commentsListView = findViewById(R.id.comments_list_view);
        editEventButton = findViewById(R.id.edit_event_button);
        backToMapButton = findViewById(R.id.back_to_map_button);
        postCommentButton = findViewById(R.id.post_comment_button);

        // Get the event data from the Intent
        String eventJson = getIntent().getStringExtra("event_data");
        if (eventJson != null) {
            currentEvent = new Gson().fromJson(eventJson, Event.class);
            displayEventDetails(currentEvent);
        }

        // Set up ListView with adapter
        commentAdapter = new CommentAdapter(this, commentsList);
        commentsListView.setAdapter(commentAdapter);

        // Fetch comments for the event
        fetchComments();

        // Set up Edit button listener to go to ModifyEventActivity
        editEventButton.setOnClickListener(v -> openModifyEventActivity());

        // Set up Back to Map button listener
        backToMapButton.setOnClickListener(v -> finish()); // Closes EventDetailsActivity and goes back to MapViewActivity

        // Post comment button
        postCommentButton.setOnClickListener(v -> postComment());
    }

    private void displayEventDetails(Event event) {
        eventNameEditText.setText(event.getEventName());
        eventLocationEditText.setText("Location: " + event.getEventLocation().getLatitude() + ", " + event.getEventLocation().getLongitude());
        eventTimeEditText.setText("Date & Time: " + event.getEventDate() + " " + event.getEventTime());
        eventDescriptionEditText.setText(event.getEventDescription());
        eventCreatorEditText.setText("Creator ID: " + event.getEventCreatorId());
    }

    private void openModifyEventActivity() {
        Intent intent = new Intent(this, ModifyEventActivity.class);
        String eventJson = new Gson().toJson(currentEvent);
        intent.putExtra("event_data", eventJson);
        intent.putExtra("event_id", eventId); // pass to ModifyEventActivity
        startActivityForResult(intent, MODIFY_EVENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedEventJson = data.getStringExtra("updated_event_data");
            String eventId = data.getStringExtra("event_id");  // Retrieve event ID

            currentEvent = new Gson().fromJson(updatedEventJson, Event.class);
            displayEventDetails(currentEvent);

            // Send the updated event to the backend
            updateEventOnBackend(currentEvent);
        }
    }

    private void updateEventOnBackend(Event updatedEvent) {
        EventRequest eventRequest = new EventRequest(
                updatedEvent.getEventName(),
                updatedEvent.getEventLocation().getLatitude(),
                updatedEvent.getEventLocation().getLongitude(),
                updatedEvent.getEventDescription(),
                updatedEvent.getEventDate(),
                updatedEvent.getEventTime(),
                updatedEvent.getEventCreatorId()
        );

        ApiClient.getAuthApi().updateEvent(eventId, eventRequest).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EventDetailsActivity.this, "Event updated successfully on server!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_event_data", new Gson().toJson(updatedEvent));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to update event: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error updating event: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventDetailsActivity", "Error updating event", t);
            }
        });
    }

    private void fetchComments() {
        ApiClient.getAuthApi().getComments(eventId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentsList.clear();
                    commentsList.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(EventDetailsActivity.this, "No comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error loading comments: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postComment() {
        String commentContent = newCommentEditText.getText().toString().trim();

        if (commentContent.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a timestamp (optional, depending on your needs)
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Prepare the CommentRequest object
        CommentRequest newCommentRequest = new CommentRequest(commentContent, userId, eventId);

        // Send the comment request to the backend
        ApiClient.getAuthApi().createComment(newCommentRequest).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Manually create a new Comment object using data from CommentResponse
                    CommentResponse commentResponse = response.body();
                    Comment newComment = new Comment(
                            commentResponse.getCommentId(),
                            commentContent, // Use original commentContent
                            timestamp,       // Use the generated timestamp
                            userId,
                            eventId
                    );

                    commentsList.add(newComment);
                    commentAdapter.notifyDataSetChanged();
                    newCommentEditText.setText(""); // Clear the input field
                    Toast.makeText(EventDetailsActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

