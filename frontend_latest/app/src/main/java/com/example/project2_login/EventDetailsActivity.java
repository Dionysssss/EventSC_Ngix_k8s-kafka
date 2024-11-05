package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_login.model.Comment;
import com.example.project2_login.model.CommentRequest;
import com.example.project2_login.model.CommentResponse;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventRequest;
import com.example.project2_login.model.EventResponse;
import com.example.project2_login.model.UserIdRequest;
import com.example.project2_login.model.UserResponse;
import com.example.project2_login.model.ValidationCountResponse;
import com.example.project2_login.model.ValidationStatusResponse;
import com.google.gson.Gson;

import java.io.IOException;
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
    private TextView thumbsUpCount;
    private TextView thumbsDownCount;
    private Button editEventButton;
    private Button backToMapButton;
    private Button postCommentButton;
    private Button thumbsUpButton;
    private Button thumbsDownButton;

    private boolean isConfirmed;
    private boolean hasReportedAsFalse = false;


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

        // Initialize buttons and count TextViews
        thumbsUpButton = findViewById(R.id.thumbs_up_button);
        thumbsDownButton = findViewById(R.id.thumbs_down_button);
        thumbsUpCount = findViewById(R.id.thumbs_up_count);
        thumbsDownCount = findViewById(R.id.thumbs_down_count);

        eventId = getIntent().getIntExtra("event_id", -1);
        userId = getIntent().getIntExtra("user_id", -1);

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

        // Load event details and validation counts
        fetchEventDetails();
        fetchValidationStatus(); // Add this call to initialize button states

        // Set up ListView with adapter
        commentAdapter = new CommentAdapter(this, commentsList);
        commentsListView.setAdapter(commentAdapter);
        fetchComments();

        // Set up Edit button listener to go to ModifyEventActivity
        editEventButton.setOnClickListener(v -> openModifyEventActivity());

        // Set up Back to Map button listener
        backToMapButton.setOnClickListener(v -> finish()); // Closes EventDetailsActivity and goes back to MapViewActivity

        // Post comment button
        postCommentButton.setOnClickListener(v -> postComment());

        // Set up click listeners for confirmation
        thumbsUpButton.setOnClickListener(v -> handleThumbsUpClick());
        thumbsDownButton.setOnClickListener(v -> handleThumbsDownClick());
    }

    private void displayEventDetails(Event event) {
        eventNameEditText.setText(event.getEventName());
        eventLocationEditText.setText("Location: " + event.getEventLocation().getLatitude() + ", " + event.getEventLocation().getLongitude());
        eventTimeEditText.setText("Date & Time: " + event.getEventDate() + " " + event.getEventTime());
        eventDescriptionEditText.setText(event.getEventDescription());
        fetchUserEmail(event.getEventCreatorId());
    }


    private void openModifyEventActivity() {
        if (currentEvent.getEventCreatorId() != userId) {
            // Display a message if the user does not have permission to edit the event
            Toast.makeText(this, "You do not have the permission to edit", Toast.LENGTH_SHORT).show();
        } else {
            // User has permission, proceed to open ModifyEventActivity
            Intent intent = new Intent(this, ModifyEventActivity.class);
            String eventJson = new Gson().toJson(currentEvent);
            intent.putExtra("event_data", eventJson);
            intent.putExtra("event_id", eventId); // pass to ModifyEventActivity
            startActivityForResult(intent, MODIFY_EVENT_REQUEST_CODE);
        }
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
                    // Toast.makeText(EventDetailsActivity.this, "Event updated successfully on server!", Toast.LENGTH_SHORT).show();
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
                    // Toast.makeText(EventDetailsActivity.this, "No comments", Toast.LENGTH_SHORT).show();
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

    private void fetchUserEmail(int userId) {
        ApiClient.getAuthApi().getUserById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String email = response.body().getEmail();
                    // Toast.makeText(EventDetailsActivity.this, "User email: " + email, Toast.LENGTH_SHORT).show();

                    // Display the email in the eventCreatorEditText
                    eventCreatorEditText.setText("Created by user: " + email);
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to retrieve email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEventDetails() {
        ApiClient.getAuthApi().getEventById(eventId).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentEvent = response.body();

                    // Display event details
                    displayEventDetails(currentEvent);

                    if (currentEvent.getEventCreatorId() == userId) {
                        editEventButton.setVisibility(View.VISIBLE);
                    } else {
                        editEventButton.setVisibility(View.GONE);
                    }

                    fetchValidationCounts();

                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to load event details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error loading event details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchValidationStatus() {
        ApiClient.getAuthApi().getValidationStatus(eventId, userId).enqueue(new Callback<ValidationStatusResponse>() {
            @Override
            public void onResponse(Call<ValidationStatusResponse> call, Response<ValidationStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getValidationStatus();
                    Toast.makeText(EventDetailsActivity.this, "Fetched Validation Status: " + status, Toast.LENGTH_SHORT).show();

                    if ("confirmed".equals(status)) {
                        isConfirmed = true;
                        hasReportedAsFalse = false;
                    } else if ("falseReport".equals(status)) {
                        isConfirmed = false;
                        hasReportedAsFalse = true;
                    } else { // "notReport"
                        isConfirmed = false;
                        hasReportedAsFalse = false;
                    }
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Event or user not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValidationStatusResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error fetching validation status: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchValidationCounts() {
        ApiClient.getAuthApi().getValidationCounts(eventId).enqueue(new Callback<ValidationCountResponse>() {
            @Override
            public void onResponse(Call<ValidationCountResponse> call, Response<ValidationCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int confirmedCount = response.body().getConfirmedCount();
                    int falseReportsCount = response.body().getFalseReportsCount();

                    // Update thumbs up and thumbs down counts
                    thumbsUpCount.setText(String.valueOf(confirmedCount));
                    thumbsDownCount.setText(String.valueOf(falseReportsCount));
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to load validation counts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValidationCountResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error loading validation counts: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmEvent() {
        ApiClient.getAuthApi().confirmEvent(eventId, new UserIdRequest(userId)).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                isConfirmed = true;
                hasReportedAsFalse = false;
                fetchValidationCounts();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error confirming event: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void withdrawConfirmation() {
        ApiClient.getAuthApi().withdrawConfirmation(eventId, new UserIdRequest(userId)).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                isConfirmed = false; // Update the confirmation flag
                hasReportedAsFalse = false;
                fetchValidationCounts();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error withdrawing confirmation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventDetailsActivity", "Error withdrawing confirmation", t);
            }
        });
    }

    private void reportFalseEvent() {
        ApiClient.getAuthApi().reportEventAsFalse(eventId, new UserIdRequest(userId)).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                isConfirmed = false; // Ensure isConfirmed is false
                hasReportedAsFalse = true; // Set hasReportedAsFalse to true
                fetchValidationCounts(); // Refresh counts to reflect the change
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error reporting event as false: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventDetailsActivity", "Error reporting event as false", t);
            }
        });
    }

    private void withdrawFalseReport() {
        ApiClient.getAuthApi().withdrawFalseReport(eventId, new UserIdRequest(userId)).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                hasReportedAsFalse = false;
                isConfirmed = false;
                fetchValidationCounts();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error withdrawing false report: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("EventDetailsActivity", "Error withdrawing false report", t);
            }
        });
    }

    private void handleThumbsUpClick() {
        if (isConfirmed && !hasReportedAsFalse) {
            // User has confirmed the event already, so clicking again will withdraw the confirmation
            withdrawConfirmation();
        } else if (!isConfirmed && !hasReportedAsFalse) {
            // User hasn't reported the event as false and hasn't confirmed it, so we allow confirmation
            confirmEvent();
        } else {
            // User has reported the event as false, so they cannot confirm it
            Toast.makeText(this, "Cannot confirm as you have reported this event as false", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleThumbsDownClick() {

        if (!isConfirmed && hasReportedAsFalse) {
            withdrawFalseReport();
        } else if (!isConfirmed && !hasReportedAsFalse) {
            reportFalseEvent();
        } else {
            Toast.makeText(this, "Cannot report as false as you have confirmed this event", Toast.LENGTH_SHORT).show();
        }
    }
}